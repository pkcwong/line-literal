/*
package com.pwned.line.job;

import com.mongodb.BasicDBObject;
import com.pwned.line.service.Thanksgiving;
import com.pwned.line.web.MongoDB;
import org.junit.BeforeClass;
import org.junit.Test;
import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class PushThanksgivingTest{

	@BeforeClass
	public static void before() {
		new MongoDB(System.getenv("MONGODB_URI")).drop("user");
		new MongoDB(System.getenv("MONGODB_URI")).drop("party");
	}

	@Test
	public void payload() throws Exception{
		Job job = new PushThanksgiving();
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		org.bson.Document data0 = new org.bson.Document();
		data0.append("uid", "junit0");
		data0.append("bind", "junit0");
		data0.append("buff", new BasicDBObject("cmd", "master"));
		mongo.getCollection("user").insertOne(data0);
		org.bson.Document data1 = new org.bson.Document();
		data1.append("uid", "junit1");
		data1.append("bind", "junit1");
		data1.append("buff", new BasicDBObject("cmd", "master"));
		mongo.getCollection("user").insertOne(data1);
		org.bson.Document data2 = new org.bson.Document();
		data2.append("uid", "junit2");
		data2.append("bind", "junit2");
		data2.append("buff", new BasicDBObject("cmd", "master"));
		mongo.getCollection("user").insertOne(data2);
		org.bson.Document data3 = new org.bson.Document();
		data3.append("uid", "Uda3b492d0d9349af4cb356e6d7d3c8d7");
		data3.append("name", "Timothy Pak");
		data3.append("Accept", "Y");
		mongo.getCollection("party").insertOne(data3);
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
		PushThanksgiving.pushThanksgiving();
		PushThanksgiving.addUserToParty(mongo);
		PushThanksgiving.buildJob(PushThanksgiving.class);
		PushThanksgiving.buildTrigger(24);
		java.util.Calendar today = java.util.Calendar.getInstance();
		java.util.Calendar partyDate = java.util.Calendar.getInstance();
		java.util.Calendar partyNext = java.util.Calendar.getInstance();
		partyDate.set(2017, java.util.Calendar.NOVEMBER,22);
		partyDate.set(2017, java.util.Calendar.NOVEMBER,23);
		today.add(java.util.Calendar.HOUR,8);
		partyDate.add(java.util.Calendar.HOUR,8);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = format1.format(today.getTime());
		PushThanksgiving.checkPushed(mongo, "Uda3b492d0d9349af4cb356e6d7d3c8d7", today);
		assertEquals(true, Thanksgiving.getName("Uda3b492d0d9349af4cb356e6d7d3c8d7").equals("Timothy Pak"));
		PushThanksgiving.checkSameDate(partyDate, partyDate);
		PushThanksgiving.checkSameDate(partyDate, partyNext);
	}

}
*/