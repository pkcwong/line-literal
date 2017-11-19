package com.pwned.line.service;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;
import com.pwned.line.entity.Course;
import java.util.ArrayList;


/***
 * Adding Course timeslot to MongoDB.
 * Required params: []
 * Reserved tokens: []
 * Resolved params: []
 * @author Eric
 */

public class TimeTableAdd extends DefaultService {

    public TimeTableAdd(Service service) {
        super(service);
    }

    @Override
    public void payload() throws Exception {
        System.out.println("getting timetable");
        MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
        BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
        ArrayList<Document> user = MongoDB.get(mongo.getCollection("user").find(SELF));
        JSONObject USER = new JSONObject(user.get(0).toJson());
        Document userid = new Document();
        userid.append("uid", this.getParam("uid").toString());
        mongo.getCollection("Timetable").insertOne(userid);

        //String timetable = USER.getJSONObject("timetablebuff").getJSONObject("data").toString();

        String timetable = this.fulfillment;
        String[] key = {"Lecture", "Laboratory", "Tutorial", "Others"};
        String[] arr = timetable.split("\n");
        ArrayList<String> timetableArr = new ArrayList<>();
        for(String s: arr){
            String[] temp = s.split("\t");
            for(String str:temp){
                timetableArr.add(str);
            }
        }
        ArrayList<String> classID = new ArrayList<>();
        ArrayList<Course> Courses = new ArrayList<>();
        for(int i = 0;i<timetableArr.size();i++){
            if(timetableArr.get(i).equals("Status")){
                String[] courseName= timetableArr.get(i-1).split(" ");
                String department = courseName[0];
                String code = courseName[1];
                String courseCode = department+" "+code;
                Course course = new Course(department, code);
                course.query();
                Courses.add(course);

            }
            for(String s:key){
                if(timetableArr.get(i).equals(s)){

                    classID.add(timetableArr.get(i-2));
                }
            }
        }
        for(int i=0;i<classID.size();i++){
            System.out.println(classID.get(i));
        }
        Document CourseList = new Document();
        for(Course c:Courses){
            for(int i=0;i<c.sections.size();i++){
                for(int j=0;j<classID.size();j++){
                    if(c.sections.get(i).code.equals(classID.get(j))){
                        boolean IsDayTBA = c.sections.get(i).dateAndTimes.get(0).day.equals("TBA");
                        System.out.println(IsDayTBA);
                        System.out.println(c.department+c.code+" and date and time size  of this section "+ c.sections.get(i).code+"= "+c.sections.get(i).dateAndTimes.size());
                        if(c.sections.get(i).dateAndTimes.size()==1 && IsDayTBA){}
                        else {
                            for (int k = 0; k < c.sections.get(i).dateAndTimes.size(); k++) {
                                Document timeslot = new Document();
                                timeslot.append("department", c.department);
                                timeslot.append("code", c.code);
                                System.out.println(c.sections.get(i).dateAndTimes.get(k).day+" "+c.sections.get(i).dateAndTimes.get(k).day.length());
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
                                    mongo.getCollection("Timetable").findOneAndUpdate(new BasicDBObject().append("uid", this.getParam("uid").toString()), new BasicDBObject("$addToSet", CourseList));
                                }
                            }
                        }
                    }
                }
            }
        }
        Document data = new Document();
        data.append("uid", this.getParam("uid").toString());
        data.append("bind", this.getParam("uid").toString());
        data.append("buff", new BasicDBObject("cmd", "master"));
        mongo.getCollection("user").findOneAndUpdate(SELF, new BasicDBObject("$set", data));
        this.fulfillment = "Saved your Timetable";


    }

    @Override
    public Service chain() throws Exception {
        return this;
    }


}
