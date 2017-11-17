package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;

/***
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

    /***
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
        /*
        if(this.fulfillment.equals("timetable"){

            MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));

            //fetch buff -> data from MongoDB
            BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
            ArrayList<Document> user = MongoDB.get(mongo.getCollection("user").find(SELF));
            JSONObject USER = new JSONObject(user.get(0).toJson());

        }
        */

    }



    @Override
    public Service chain() throws Exception {
        return this;
    }

}
