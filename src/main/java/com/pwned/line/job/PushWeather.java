package com.pwned.line.job;

import com.linecorp.bot.model.message.TextMessage;
import com.mongodb.BasicDBObject;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.http.HTTP;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class PushWeather extends DefaultJob{

    public static String weatherForecast = "";
    public static ArrayList<Document> usersArrayList;
    public static ArrayList<org.bson.Document> weatherArrayList;

    public PushWeather() {

    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("PushWeather");
        this.updateWeather();
    }


    public static JobDetail buildJob(Class <? extends Job> job) {
        return JobBuilder.newJob(PushWeather.class).build();
    }

    public static Trigger buildTrigger(int seconds) {
        return TriggerBuilder
                .newTrigger()
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(seconds).repeatForever())
                .build();
    }

    public static void updateWeather() {
        usersArrayList = MongoDB.get(new MongoDB(System.getenv("MONGODB_URI")).getCollection("user").find());
        weatherArrayList = MongoDB.get(new MongoDB(System.getenv("MONGODB_URI")).getCollection("weather").find());

        MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));

        BasicDBObject SELF = new BasicDBObject().append("forecast", weatherForecast);
        ArrayList<org.bson.Document> weather = MongoDB.get(mongo.getCollection("weather").find(SELF));

        if (weatherArrayList.isEmpty()) {
            weatherForecast = getWeather();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            org.bson.Document data = new org.bson.Document();
            data.put("forecast", weatherForecast);
            data.put("time", dateFormat.format(date));
            mongo.getCollection("weather").insertOne(data);
            pushWeather(weatherForecast);
        } else {
            ArrayList<org.bson.Document> weatherArray = mongo.get(mongo.getCollection("weather").find());
            try {
                if (!new JSONObject(weatherArray.get(0).toJson()).getString("forecast").toString().equals(getWeather())) {
                    BasicDBObject query = new BasicDBObject();
                    query.put("forecast", new JSONObject(weatherArray.get(0).toJson()).getString("forecast"));
                    BasicDBObject queryTime = new BasicDBObject();
                    query.put("time", new JSONObject(weatherArray.get(0).toJson()).getString("time"));
                    BasicDBObject newForecast = new BasicDBObject();
                    newForecast.put("forecast", getWeather());
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    BasicDBObject newTime = new BasicDBObject();
                    newTime.put("time", dateFormat.format(date));
                    BasicDBObject updateForecast = new BasicDBObject();
                    updateForecast.put("$set", newForecast);
                    BasicDBObject updateTime = new BasicDBObject();
                    updateTime.put("$set", newTime);
                    mongo.getCollection("weather").updateOne(query, updateForecast);
                    mongo.getCollection("weather").updateOne(queryTime, updateTime);
                    pushWeather(getWeather());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getWeather() {
        String link = "http://www.hko.gov.hk/wxinfo/currwx/flw.htm";
        HTTP http = new HTTP(link);
        String weather = http.get();
        String[] messages = {"Weather forecast", "<br/><br/>Outlook"};
        weather = weather.substring(weather.indexOf(messages[0]), weather.indexOf(messages[1]));
        weather = weather.replace("<br/>", "\n");
        while (weather.contains("<")) {
            weather = weather.substring(0, weather.indexOf("<")) + weather.substring(weather.indexOf(">") + 1);
        }
        return weather;
    }

    public static void pushWeather(String weatherForecast) {
        for (int i = 0; i < usersArrayList.size(); i++) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Hong_Kong"));
                Date date = new Date();
                KitchenSinkController.push(new JSONObject(usersArrayList.get(i).toJson()).getString("uid"), new TextMessage(weatherForecast + "\n" + dateFormat.format(date)));
                if(weatherForecast.contains("rain")){
                    KitchenSinkController.push(new JSONObject(usersArrayList.get(i).toJson()).getString("uid"), new TextMessage("It will be raining. Please remember to bring an umbrella."));
                }
                if(weatherForecast.contains("range between 1") || weatherForecast.contains("will be about 1")){
                    KitchenSinkController.push(new JSONObject(usersArrayList.get(i).toJson()).getString("uid"), new TextMessage("It will be cold. Please remember to put on enough clothes."));
                }
                if(weatherForecast.contains("range between 3") || weatherForecast.contains("will be about 3")){
                    KitchenSinkController.push(new JSONObject(usersArrayList.get(i).toJson()).getString("uid"), new TextMessage("It will be hot. Please remember to drink more water and put on appropriate clothes."));
                }
                if(weatherForecast.contains("strong offshore")){
                    KitchenSinkController.push(new JSONObject(usersArrayList.get(i).toJson()).getString("uid"), new TextMessage("It will be windy. Please remember to bring a jacket."));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
