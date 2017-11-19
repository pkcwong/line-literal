package com.pwned.line.service;


import com.mongodb.BasicDBObject;
import com.pwned.line.entity.Course;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;


/***
 * Adding Course timeslot to MongoDB.
 * Required params: []
 * Reserved tokens: []
 * Resolved params: []
 * @author Eric
 */

public class EndTimeTableAdd extends DefaultService {

    public EndTimeTableAdd(Service service) {
        super(service);
    }

    @Override
    public void payload() throws Exception {
        System.out.println("getting timetable");
        MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
        BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
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
