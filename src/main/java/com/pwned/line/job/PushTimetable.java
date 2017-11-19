package com.pwned.line.job;

import com.linecorp.bot.model.message.TextMessage;
import com.mongodb.BasicDBObject;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.entity.Course;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.*;
import java.lang.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class PushTimetable extends DefaultJob {
    public static ArrayList<Document> usersArrayList;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            this.pushNextLesson();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public static Trigger buildTrigger(int minutes) {
        return TriggerBuilder
                .newTrigger()
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInMinutes(minutes).repeatForever())
                .build();
    }
    public static JobDetail buildJob(Class <? extends Job> job) {
        return JobBuilder.newJob(PushTimetable.class).build();
    }

    public static void pushNextLesson() throws JSONException, ParseException{
        System.out.println("Pushtimetable\n\n");
        MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
        usersArrayList = MongoDB.get(new MongoDB(System.getenv("MONGODB_URI")).getCollection("user").find());
        ArrayList<String> uid = new ArrayList<>();
        System.out.println("try to get uid");
        for (int i = 0; i < usersArrayList.size(); i++) {
            System.out.println("adding uid to array");
            uid.add(new JSONObject(usersArrayList.get(i).toJson()).getString("uid"));
        }
        if(uid.size()==0){}
        else {
            for(int x=0;x<uid.size();x++){
                System.out.println("has uid"+uid.get(x));
                BasicDBObject userI = new BasicDBObject().append("uid", uid.get(x));
                System.out.println("Getting time table of uid"+uid.get(x));
                ArrayList<Document> userTimetable = MongoDB.get(mongo.getCollection("Timetable").find(userI));
                System.out.println("getting the timeslot of uid");
                System.out.println("userTimetable size = "+userTimetable.size());
                if(userTimetable.size()==0){continue;}
                JSONArray USERT = new JSONObject(userTimetable.get(0).toJson()).getJSONArray("timeslot");
                System.out.println("timeslot got");
                final SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                if (USERT.length() == 0) {System.out.println("no time slot");
                } else {
                    for (int i = 0; i < USERT.length(); i++) {
                        System.out.println("for "+i+" th timeslot");
                        Calendar today = Calendar.getInstance();
                        int dayofweek = today.get(Calendar.DAY_OF_WEEK);
                        int hour = (today.get(Calendar.HOUR) + 8) % 24;
                        int minutes = today.get(Calendar.MINUTE);
                        Date d = format.parse(hour + ":" + minutes);
                        String department = USERT.getJSONObject(i).get("department").toString();
                        String code = USERT.getJSONObject(i).get("code").toString();
                        String day = USERT.getJSONObject(i).get("day").toString();
                        int lessonDay = convertDay(day);
                        if (lessonDay == dayofweek) {
                            System.out.println("day are the same");
                            String startTime = USERT.getJSONObject(i).get("start time").toString();
                            String[] time = {startTime.substring(0, 2), startTime.substring(3, 5), startTime.substring(5)};
                            int lessonHour = Integer.parseInt(time[0]);
                            int lessonMinutes = Integer.parseInt(time[1]);
                            if (time[2].equals("PM") && lessonHour != 12) {
                                lessonHour += 12;
                                System.out.println("is PM: " + department + " " + code + "hour is " + lessonHour);
                            }
                            Date lessontime = format.parse(lessonHour + ":" + lessonMinutes);
                            long diff;
                            if (d.getTime() > lessontime.getTime()) {
                                diff = d.getTime() - lessontime.getTime();
                            } else {
                                diff = lessontime.getTime() - d.getTime();
                            }
                            long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                            String venue = USERT.getJSONObject(i).get("venue").toString();
                            System.out.println(i + "th time:\ndepartment: " + department + "\ncode: "
                                    + code + "\nday: " + day + "\nstart time: " + time[0] + " " + time[1] + " " + time[2] + "\nvenue: " + venue);
                            System.out.println("current Time = " + d.getTime() + ", LessonTime = " + lessontime.getTime() + "\ndiff = " + diff + ", diffMinutes = " + diffMinutes);
                            if (lessontime.getTime() > d.getTime() && diffMinutes <= 30) {
                                System.out.println("you have lesson " + department + " " + code + " :Venue " + venue + "\nwithin " + diffMinutes + " minutes");
                                KitchenSinkController.push(uid.get(x), new TextMessage("You have lesson: \n" + department + " " + code + " at " + venue + "\nwithin " + diffMinutes + " minutes."));
                            }


                        }
                    }
                }
            }
        }


    }

    private static boolean checkSameDate(Calendar cal1, Calendar cal2){
        if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)){
            return true;
        }else{
            return false;
        }


    }
    private static int convertDay(String day){
        int convertedDay=0;
        switch (day){
            case "Mo": convertedDay = Calendar.MONDAY;break;
            case "Tu": convertedDay = Calendar.TUESDAY;break;
            case "We": convertedDay = Calendar.WEDNESDAY;break;
            case "Th": convertedDay = Calendar.THURSDAY;break;
            case "Fr": convertedDay = Calendar.FRIDAY;break;
            case "Sa": convertedDay = Calendar.SATURDAY;break;
            case "Su": convertedDay = Calendar.SUNDAY;break;
        }
        return convertedDay;
    }
}
