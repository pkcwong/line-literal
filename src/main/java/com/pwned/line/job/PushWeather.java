package com.pwned.line.job;

import com.linecorp.bot.model.message.TextMessage;
import com.mongodb.BasicDBObject;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.service.Weather;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PushWeather extends DefaultJob{

    public static String weatherForecast = "";
    public static ArrayList<Document> usersArrayList;
    public static ArrayList<org.bson.Document> weatherArrayList;

    public PushWeather(){
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException{
        System.out.println("PushWeather");
        try {
            this.updateWeather();
        }
        catch (JSONException e){
            throw new JobExecutionException();
        }
    }


    public static JobDetail buildJob(Class <? extends Job> job){
        return JobBuilder.newJob(PushWeather.class).build();
    }

    public static Trigger buildTrigger(int seconds){
        return TriggerBuilder
                .newTrigger()
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(seconds).repeatForever())
                .build();
    }

    public static void updateWeather() throws JSONException{
        MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
        usersArrayList = MongoDB.get(mongo.getCollection("user").find());
        weatherArrayList = MongoDB.get(mongo.getCollection("weather").find());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        if (weatherArrayList.isEmpty()) {
            weatherForecast = Weather.getWeather();
            org.bson.Document data = new org.bson.Document();
            data.put("forecast", weatherForecast);
            data.put("time", dateFormat.format(date));
            mongo.getCollection("weather").insertOne(data);
            pushWeather(weatherForecast);
        } else {
            if (!new JSONObject(weatherArrayList.get(0).toJson()).getString("forecast").toString().equals(Weather.getWeather())) {
                BasicDBObject query = new BasicDBObject();
                query.put("forecast", new JSONObject(weatherArrayList.get(0).toJson()).getString("forecast"));
                BasicDBObject newSet = new BasicDBObject();
                newSet.put("forecast", Weather.getWeather());
                BasicDBObject newValue = new BasicDBObject();
                newValue.put("$set", newSet);
                mongo.getCollection("weather").updateOne(query, newValue);
                query.put("time", new JSONObject(weatherArrayList.get(0).toJson()).getString("time"));
                newSet.put("time", dateFormat.format(date));
                newValue.put("$set", newSet);
                mongo.getCollection("weather").updateOne(query, newValue);
                weatherForecast = Weather.getWeather();
                pushWeather(weatherForecast);
            }
        }
    }

    public static void pushWeather(String weatherForecast) {
        for (int i = 0; i < usersArrayList.size(); i++) {
            try {
                KitchenSinkController.push(new JSONObject(usersArrayList.get(i).toJson()).getString("uid"), new TextMessage(weatherForecast + "\n" + Weather.getDate()));
                if(weatherForecast.contains("rain")){
                    KitchenSinkController.push(new JSONObject(usersArrayList.get(i).toJson()).getString("uid"), new TextMessage("It will be raining. Please remember to bring an umbrella."));
                }
                if(weatherForecast.contains("range between 1") || weatherForecast.contains("will be about 1") || weatherForecast.contains("around 1")){
                    KitchenSinkController.push(new JSONObject(usersArrayList.get(i).toJson()).getString("uid"), new TextMessage("It will be cold. Please remember to put on enough clothes."));
                }
                if(weatherForecast.contains("range between 3") || weatherForecast.contains("will be about 3") || weatherForecast.contains("around 3")){
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
