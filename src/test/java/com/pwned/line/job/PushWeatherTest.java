package com.pwned.line.job;

import com.mongodb.BasicDBObject;
import com.pwned.line.web.MongoDB;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.quartz.*;

import java.util.ArrayList;
import java.util.Date;

public class PushWeatherTest{

	@BeforeClass
	public static void before() {
		new MongoDB(System.getenv("MONGODB_URI")).drop("weather");
	}

	@Test
	public void payloadWeather() throws Exception {
		Job job = new PushWeather();
		job.execute(new JobExecutionContext() {
			@Override
			public Scheduler getScheduler() {
				return null;
			}

			@Override
			public Trigger getTrigger() {
				return null;
			}

			@Override
			public Calendar getCalendar() {
				return null;
			}

			@Override
			public boolean isRecovering() {
				return false;
			}

			@Override
			public TriggerKey getRecoveringTriggerKey() throws IllegalStateException {
				return null;
			}

			@Override
			public int getRefireCount() {
				return 0;
			}

			@Override
			public JobDataMap getMergedJobDataMap() {
				return null;
			}

			@Override
			public JobDetail getJobDetail() {
				return null;
			}

			@Override
			public Job getJobInstance() {
				return null;
			}

			@Override
			public Date getFireTime() {
				return null;
			}

			@Override
			public Date getScheduledFireTime() {
				return null;
			}

			@Override
			public Date getPreviousFireTime() {
				return null;
			}

			@Override
			public Date getNextFireTime() {
				return null;
			}

			@Override
			public String getFireInstanceId() {
				return null;
			}

			@Override
			public Object getResult() {
				return null;
			}

			@Override
			public void setResult(Object result) {

			}

			@Override
			public long getJobRunTime() {
				return 0;
			}

			@Override
			public void put(Object key, Object value) {

			}

			@Override
			public Object get(Object key) {
				return null;
			}
		});
		PushWeather.updateWeather();
		PushWeather.updateWeather();
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		ArrayList<org.bson.Document> weatherArrayList = MongoDB.get(mongo.getCollection("weather").find());
		BasicDBObject query = new BasicDBObject();
		query.put("forecast", new JSONObject(weatherArrayList.get(0).toJson()).getString("forecast"));
		BasicDBObject newSet = new BasicDBObject();
		newSet.put("forecast", "Test");
		BasicDBObject newValue = new BasicDBObject();
		newValue.put("$set", newSet);
		mongo.getCollection("weather").updateOne(query, newValue);
		PushWeather.updateWeather();
		PushWeather.pushWeather("rain, around 1, around 3, strong offshore");
		PushWeather.buildJob(PushWeather.class);
		PushWeather.buildTrigger(0);
	}

}
