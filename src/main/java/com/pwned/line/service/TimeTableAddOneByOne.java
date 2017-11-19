package com.pwned.line.service;


import com.mongodb.BasicDBObject;
import com.pwned.line.entity.Course;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;


/***
 * Adding Course timeslot to MongoDB.
 * Required params: [uid]
 * Reserved tokens: []
 * Resolved params: []
 * @author Eric Kwan
 */

public class TimeTableAddOneByOne extends DefaultService {

    /**
     * Constructor
     * @param service
     */
    public TimeTableAddOneByOne(Service service) {
        super(service);
    }

    @Override
    public void payload() throws Exception {
        //System.out.println("getting timetable");
        MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
        BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
        ArrayList<Document> user = MongoDB.get(mongo.getCollection("user").find(SELF));
        JSONObject USER = new JSONObject(user.get(0).toJson());
        ArrayList<Document> Timetableuser = MongoDB.get(mongo.getCollection("Timetable").find(SELF));
        if(Timetableuser.size()==0){
            Document userid = new Document();
            userid.append("uid", this.getParam("uid").toString());
            mongo.getCollection("Timetable").insertOne(userid);
        }
        String[] ExitKey = {"addtimetable::end", "addtimetable::End", "addTimetable::end"};
        String temp = this.fulfillment.toLowerCase();
        for(String key: ExitKey){
            if(key.equals(temp)){
                Document data = new Document();
                data.append("uid", this.getParam("uid").toString());
                data.append("bind", this.getParam("uid").toString());
                data.append("buff", new BasicDBObject("cmd", "master"));
                mongo.getCollection("user").findOneAndUpdate(SELF, new BasicDBObject("$set", data));
                this.fulfillment = "Finish adding course to your Timetable :)";
                return;
            }
        }
        String timetable = this.fulfillment;
        String[] arr = timetable.split(",");
        String[] courseCode = {arr[0].substring(0, 4), arr[0].substring(4)};
        Course c = new Course(courseCode[0], courseCode[1]);
        c.query();
        ArrayList<String> classID = new ArrayList<>();
        for(int i = 1;i<arr.length;i++){
            classID.add(arr[i]);
            }

        for(int i=0;i<classID.size();i++){
            System.out.println(classID.get(i));
        }
        Document CourseList = new Document();
        for(int i=0;i<c.sections.size();i++){
            for(int j=0;j<classID.size();j++){
                if(c.sections.get(i).code.equals(classID.get(j))){
                    boolean IsDayTBA = c.sections.get(i).dateAndTimes.get(0).day.equals("TBA");
                    //System.out.println(IsDayTBA);
                    //System.out.println(c.department+c.code+" and date and time size  of this section "+ c.sections.get(i).code+"= "+c.sections.get(i).dateAndTimes.size());
                    if(c.sections.get(i).dateAndTimes.size()==1 && IsDayTBA){}
                    else {
                        for (int k = 0; k < c.sections.get(i).dateAndTimes.size(); k++) {
                            Document timeslot = new Document();
                            timeslot.append("department", c.department);
                            timeslot.append("code", c.code);
                            //System.out.println(c.sections.get(i).dateAndTimes.get(k).day+" "+c.sections.get(i).dateAndTimes.get(k).day.length());
                            if(c.sections.get(i).dateAndTimes.get(k).day.length()!=2){
                                String[] day = {c.sections.get(i).dateAndTimes.get(k).day.substring(0, 2), c.sections.get(i).dateAndTimes.get(k).day.substring(2)};

                                for(String d:day){
                                    timeslot.append("day", d);
                                    timeslot.append("start time", c.sections.get(i).dateAndTimes.get(k).startTime);
                                    timeslot.append("end time", c.sections.get(i).dateAndTimes.get(k).endTime);
                                    timeslot.append("venue", c.sections.get(i).rooms.get(k));
                                    CourseList.append("timeslot", timeslot);
                                    mongo.getCollection("Timetable").findOneAndUpdate(new BasicDBObject().append("uid", this.getParam("uid").toString()), new BasicDBObject("$addToSet", CourseList));
                                }
                            }else {
                                timeslot.append("day", c.sections.get(i).dateAndTimes.get(k).day);
                                timeslot.append("start time", c.sections.get(i).dateAndTimes.get(k).startTime);
                                timeslot.append("end time", c.sections.get(i).dateAndTimes.get(k).endTime);
                                timeslot.append("venue", c.sections.get(i).rooms.get(k));
                                CourseList.append("timeslot", timeslot);
                                //System.out.println("length=2 append");
                                mongo.getCollection("Timetable").findOneAndUpdate(new BasicDBObject().append("uid", this.getParam("uid").toString()), new BasicDBObject("$addToSet", CourseList));
                            }
                        }
                    }
                }
            }
        }

        Document data = new Document();
        data.append("uid", this.getParam("uid").toString());
        data.append("bind", this.getParam("uid").toString());
        data.append("buff", new BasicDBObject("cmd", "timetable::addOneByOne"));
        mongo.getCollection("user").findOneAndUpdate(SELF, new BasicDBObject("$set", data));
        this.fulfillment = "Saved the course to your timetable, if you want to insert others, please do it again. If not, then type addTimetable::end ";


    }

    @Override
    public Service chain() throws Exception {
        return this;
    }


}
