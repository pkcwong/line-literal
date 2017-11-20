package com.pwned.line.job;


import com.pwned.line.service.DefaultService;
import com.pwned.line.service.MasterController;
import com.pwned.line.service.Service;
import com.pwned.line.web.MongoDB;
import org.junit.BeforeClass;
import org.junit.Test;
import org.quartz.*;

import java.util.Date;

public class PushKMBTest{

	@BeforeClass
	public static void before() {
		new MongoDB(System.getenv("MONGODB_URI")).drop("kmb");
		try {
			Service service = new MasterController(new DefaultService("anonymous"));
			service.setParam("uid", "junit");
			service.setParam("replyToken", "junit");
			service.setParam("timestamp", "junit");
			service.resolve().get();
		} catch (Exception e) {
			e.printStackTrace();
			assert false;
		}
	}

	@Test
	public void payloadKMB() throws Exception {
		Job job = new PushKMB();
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
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		org.bson.Document data0 = new org.bson.Document();
		data0.append("uid", "junit0");
		mongo.getCollection("kmb").insertOne(data0);
		PushKMB.updateKMB();
		PushKMB.buildJob(PushKMB.class);
		PushKMB.buildTrigger(0);
	}

}
