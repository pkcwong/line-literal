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
        MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
        JSONObject apiParam = new JSONObject(this.getParam("parameters").toString());
        String add = apiParam.getString("ReviewAdd");
        String OneByOne = apiParam.getString("oneByOneTrigger");
        //find
        if(!add.equals("add")){

            this.fulfillment = "";
            //ArrayList<Document> courseReview = MongoDB.get(mongo.getCollection("courseReview").find());
            if (this.fulfillment.equals("")) {
                this.fulfillment = "Sorry, no timetable yet. There are 2 ways to import your timetable:\n1. Please login your Student Center, and then go to class schedule " +
                        "to copy your timetable! :)\n2. add the course with section number one by one.(enter\"add timetable one by one\")";
            }
        }
        else{
            if(OneByOne.equals("onebyone")){
                BasicDBObject SELF = new BasicDBObject("uid", this.getParam("uid").toString());
                mongo.getCollection("user").updateOne(SELF, new BasicDBObject("$set", new BasicDBObject("buff", new BasicDBObject().append("cmd", "timetable::addOneByOne"))));
                this.fulfillment = "You can add your timetable here one by one: \n(Please follow the following format: course code(e.g COMP3111),section number1(e.g. 2632),section number 2,...";
            }else {
                BasicDBObject SELF = new BasicDBObject("uid", this.getParam("uid").toString());
                mongo.getCollection("user").updateOne(SELF, new BasicDBObject("$set", new BasicDBObject("buff", new BasicDBObject().append("cmd", "timetable::add"))));
                this.fulfillment = "You can copy your timetable here: ";
            }

        }

    }



    @Override
    public Service chain() throws Exception {
        return this;
    }

}
