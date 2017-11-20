package com.pwned.line.service;


import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.pwned.line.entity.Course;
import com.pwned.line.web.MongoDB;
import org.bson.Document;

import java.util.ArrayList;


/***
 * Adding Course timeslot into MongoDB.
 * Required params: [uid]
 * Reserved tokens: []
 * Resolved params: []
 * @author Eric Kwan
 */

public class TimeTableDelete extends DefaultService {

    /**
     * Constructor
     * @param service
     */
    public TimeTableDelete(Service service) {
        super(service);
    }

    /**
     * query the user's import schedule from SIS
     * @throws Exception
     */
    @Override
    public void payload() throws Exception {
        System.out.println("getting timetable");
        MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
        BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
        MongoCollection<Document> Timetable = mongo.getCollection("Timetable");
        String userid = this.getParam("uid").toString();
        ArrayList<Document> Timetableuser = MongoDB.get(mongo.getCollection("Timetable").find(SELF));
        if(Timetableuser.size()==0){
            this.fulfillment = "You didn't save your timetable!!";
            return;
        }
        String[] deleteKey = {"timetable::delete", "Timetable::Delete", "Timetable::del"};
        String temp1 = this.fulfillment.toLowerCase();
        for(String key: deleteKey){
            if(key.equals(temp1)){
                Timetable.deleteOne(Filters.eq("uid", userid));
                this.fulfillment = "Deleted your Timetable :)";
                return;
            }
        }

        Document data = new Document();
        data.append("uid", this.getParam("uid").toString());
        data.append("bind", this.getParam("uid").toString());
        data.append("buff", new BasicDBObject("cmd", "master"));
        mongo.getCollection("user").findOneAndUpdate(SELF, new BasicDBObject("$set", data));
        this.fulfillment = "Saved your Timetable";

    }

    /**
     * Request processing from next Service module.
     * @return Service state
     * @throws Exception Exception
     */

    @Override
    public Service chain() throws Exception {
        return this;
    }


}
