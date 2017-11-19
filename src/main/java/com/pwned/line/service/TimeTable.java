package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Store course timeslot in MongoDB.
 * Required params: []
 * Reserved tokens: []
 * Resolved params: []
 * @author Eric Kwan
 */

public class TimeTable extends DefaultService {

    public TimeTable(Service service) {
        super(service);
    }

    /**
     * The database - timetable will store the following element:
     * 1. department
     * 2. coursecode
     * 3. classID
     * 4. timeslot+venue(
     *     4a. timeslot1
     *     4b. timeslot2(if any)
     *     4c. timeslot3(if any)
     *
     *
     */
    @Override
    public void payload() throws Exception {
        MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
        JSONObject apiParam = new JSONObject(this.getParam("parameters").toString());
        String add = apiParam.getString("ReviewAdd");
        //find
        if(!add.equals("add")){
            this.fulfillment = "";
            ArrayList<Document> courseReview = MongoDB.get(mongo.getCollection("courseReview").find());
            if (this.fulfillment.equals("")) {
                this.fulfillment = "Sorry, no timetable yet. Please login your Student Center, and then go to class schedule " +
                        "to copy your timetable! :)";
            }
        }
        else{
            BasicDBObject SELF = new BasicDBObject("uid", this.getParam("uid").toString());
            mongo.getCollection("user").updateOne(SELF, new BasicDBObject("$set", new BasicDBObject("buff", new BasicDBObject().append("cmd", "timetable::add"))));
            this.fulfillment = "You can copy your timetable here: ";

        }

    }



    @Override
    public Service chain() throws Exception {
        return this;
    }

}
