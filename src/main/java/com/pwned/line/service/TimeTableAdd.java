package com.pwned.line.service;

//import com.pwned.line.web.MongoDB;

import org.json.JSONArray;
import org.json.JSONObject;

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
        String[] arr = new JSONObject(this.getParam("parameters").toString()).toString().split("\n");
        String[] key = {"Lecture", "Laboratory", "Tutorial", "Others"};
        //String[] arr = timetable.split("\n");
        ArrayList<String> timetableArr = new ArrayList<>();
        for(String s: arr){
            String[] temp = s.split("\t");
            for(String str:temp){
                timetableArr.add(str);
            }
        }

        ArrayList<String> classID = new ArrayList<>();
        for(int i = 0;i<timetableArr.size();i++){
            System.out.println("i="+i+" "+timetableArr.get(i));
            for(String s:key){
                if(timetableArr.get(i).equals(s)){
                    System.out.println("match"+s);
                    classID.add(timetableArr.get(i-2));
                }
            }
        }
        this.fulfillment="the Class ID:";
        System.out.println("after extraction:");
        for(String s:classID){
            System.out.println(s);
            this.fulfillment=this.fulfillment+" "+s;
        }

        //MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));

        //fetch buff -> data from MongoDB
        /**BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
        ArrayList<Document> user = MongoDB.get(mongo.getCollection("user").find(SELF));
        JSONObject USER = new JSONObject(user.get(0).toJson());
        String department = USER.getJSONObject("buff").getJSONObject("data").getString("department");
        String code = USER.getJSONObject("buff").getJSONObject("data").getString("code");
        String classID = USER.getJSONObject("buff").getJSONObject("data").getString("classID");

        // find self, save to arraylist
        BasicDBObject timetable = new BasicDBObject().append("department", department).append("code", code).append("classID", classID);
        ArrayList<Document> timetableuser = MongoDB.get(mongo.getCollection("tablename").find(timetable));
        // if arraylist.size is 0, create new timetable doc, else do nothing
        if(timetableuser.size() == 0){
            Document data = new Document();
            data.append("uid", this.getParam("uid").toString());
            data.append("department", department);
            data.append("code", code);
            data.append("classID", classID);
            mongo.getCollection("").insertOne(data);

        // add review

        mongo.getCollection("tablename").findOneAndUpdate(new BasicDBObject().append("department", department).append("code", code), new BasicDBObject("$addToSet", new BasicDBObject("reviews", this.fulfillment)), new FindOneAndUpdateOptions().upsert(true));

        Document data = new Document();
        data.append("uid", this.getParam("uid").toString());
        data.append("bind", this.getParam("uid").toString());
        data.append("buff", new BasicDBObject("cmd", "master"));
        mongo.getCollection("user").findOneAndUpdate(SELF, new BasicDBObject("$set", data));
        this.fulfillment = "Your course review had been added";
         **/
    }

    @Override
    public Service chain() throws Exception {
        return this;
    }


}
