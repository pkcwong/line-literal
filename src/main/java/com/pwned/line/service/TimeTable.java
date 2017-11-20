package com.pwned.line.service;

import com.linecorp.bot.model.message.TextMessage;
import com.mongodb.BasicDBObject;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/***
 * Store/show the timetable into MongoDB.
 * Required params: [uid]
 * Reserved tokens: [@review::add, @oneByOneTrigger]
 * Resolved params: []
 * @author Eric Kwan
 */

public class TimeTable extends DefaultService {

    /**
     * Constructor
     * @param service
     */
    public TimeTable(Service service) {
        super(service);
    }


    /**
     * query the user imported timetable from SIS Class schedule
     * if there is user's timetable in MongoDB
     * the bot will suggest user to import it in two ways:
     * 1. Copy from SIS class schedule
     * 2. import Course code and section number one by one
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
            BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
            ArrayList<Document> userTimetable = MongoDB.get(mongo.getCollection("Timetable").find(SELF));
            System.out.println("getting the timeslot of uid");
            System.out.println("userTimetable size = "+userTimetable.size());
            if(userTimetable.size()==0){}else {
                JSONArray USERT = new JSONObject(userTimetable.get(0).toJson()).getJSONArray("timeslot");
                System.out.println("timeslot got");
                if (USERT.length() == 0) {
                    System.out.println("no time slot");
                } else {
                    this.fulfillment = "Here is your timetable:";
                    for (int i = 0; i < USERT.length(); i++) {
                        System.out.println("for " + i + " th timeslot");
                        String department = USERT.getJSONObject(i).get("department").toString();
                        String code = USERT.getJSONObject(i).get("code").toString();
                        String day = USERT.getJSONObject(i).get("day").toString();
                        String convertedDay = convertToFullDay(day);
                        String venue = USERT.getJSONObject(i).get("venue").toString();
                        String startTime = USERT.getJSONObject(i).get("start time").toString();
                        String endTime = USERT.getJSONObject(i).get("end time").toString();
                        this.fulfillment = this.fulfillment + "\n\n" + department + " " + code + "\nVenue: " + venue + "\nDay: " + convertedDay + "\nStart Time: " + startTime + "\nEnd Time: " + endTime;
                    }
                }
            }
            if (this.fulfillment.equals("")) {
                this.fulfillment = "Sorry, no timetable yet. There are 2 ways to import your timetable:\n1. Please login your Student Center, and then go to class schedule " +
                        "to copy your timetable! :)\n2. add the course with section number one by one.(enter\"add timetable one by one\")";
            }
        }
        else{
            if(OneByOne.equals("onebyone")){
                BasicDBObject SELF = new BasicDBObject("uid", this.getParam("uid").toString());
                mongo.getCollection("user").updateOne(SELF, new BasicDBObject("$set", new BasicDBObject("buff", new BasicDBObject().append("cmd", "timetable::addOneByOne"))));
                this.fulfillment = "You can add your timetable here one by one: \n***Please follow the following format:\ncourse code(e.g COMP3111),section number1(e.g. 2632),section number 2,...";
            }else {
                BasicDBObject SELF = new BasicDBObject("uid", this.getParam("uid").toString());
                mongo.getCollection("user").updateOne(SELF, new BasicDBObject("$set", new BasicDBObject("buff", new BasicDBObject().append("cmd", "timetable::add"))));
                this.fulfillment = "You can copy your timetable here: ";
            }

        }

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

    /**
     * Convert to Full Day String Methods
     * @param day
     * @return int convertedDay
     */
    private static String convertToFullDay(String day){
        String convertedDay=" ";
        switch (day){
            case "Mo": convertedDay = "Monday";break;
            case "Tu": convertedDay = "Tuesday";break;
            case "We": convertedDay = "Wednesday";break;
            case "Th": convertedDay = "Thursday";break;
            case "Fr": convertedDay = "Friday";break;
            case "Sa": convertedDay = "Saturday";break;
            case "Su": convertedDay = "Sunday";break;
        }
        return convertedDay;
    }

}
