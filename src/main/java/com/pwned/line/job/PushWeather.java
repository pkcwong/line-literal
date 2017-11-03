package com.pwned.line.job;

import com.linecorp.bot.model.message.TextMessage;
import com.mongodb.BasicDBObject;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.http.HTTP;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PushWeather extends DefaultJob{

    public static String weatherForecast = "";
    public static ArrayList<Document> usersArrayList;
    public static ArrayList<org.bson.Document> weatherArrayList;

    public PushWeather() {

    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        this.updateWeather();
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
                    mongo.getCollection("weather").updateOne(query, updateTime);
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
                Date date = new Date();
                KitchenSinkController.push(new JSONObject(usersArrayList.get(i).toJson()).getString("uid"), new TextMessage(weatherForecast + "\n" + dateFormat.format(date)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
