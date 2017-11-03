package com.pwned.line.service;

import com.linecorp.bot.model.message.TextMessage;
import com.mongodb.BasicDBObject;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.http.HTTP;
import com.pwned.line.web.MongoDB;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/***
 * Service for course information.
 * Required params: [uid]
 * Reserved tokens:
 * Resolved params: []
 * @author Timothy Pak
 */

public class PushWeather implements Job {
	public static String weatherForecast = "";
	public static ArrayList<org.bson.Document> usersArrayList;
	public static ArrayList<org.bson.Document> weatherArrayList;

	public PushWeather() {

	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		PushWeather.updateWeather();
	}

	public static void updateWeather() {
		usersArrayList = MongoDB.get(new MongoDB(System.getenv("MONGODB_URI")).getCollection("user").find());
		weatherArrayList = MongoDB.get(new MongoDB(System.getenv("MONGODB_URI")).getCollection("weather").find());

		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));

		BasicDBObject SELF = new BasicDBObject().append("forecast", weatherForecast);
		ArrayList<org.bson.Document> weather = MongoDB.get(mongo.getCollection("weather").find(SELF));

		if (weather.size() == 0) {
			weatherForecast = getWeather();
			org.bson.Document data = new org.bson.Document();
			data.append("forecast", weatherForecast);
			mongo.getCollection("weather").insertOne(data);
			pushWeather(weatherForecast);
		} else {
			ArrayList<org.bson.Document> weatherArray = mongo.get(mongo.getCollection("weather").find());
			try {
				if (new JSONObject(weatherArray.get(0).toJson()).getString("forecast") != getWeather()) {
					pushWeather(getWeather() + "duplicated");
					mongo.getCollection("weather").deleteOne(new org.bson.Document("forecast", new JSONObject(weatherArray.get(0).toJson()).getString("forecast")));
					weatherForecast = getWeather();
					org.bson.Document data = new org.bson.Document();
					data.append("forecast", weatherForecast);
					mongo.getCollection("weather").insertOne(data);
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