package com.pwned.line.service;

import com.pwned.line.web.MongoDB;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class EventMakerTest {

	@Before
	public void setUp() throws Exception {
		new MongoDB(System.getenv("MONGODB_URI")).drop("Event");
		new MongoDB(System.getenv("MONGODB_URI")).drop("TimeSlot");
		new MongoDB(System.getenv("MONGODB_URI")).drop("user");
		Service service = new MasterController(new DefaultService("hi"));
		service.setParam("uid", "junit");
		service.setParam("replyToken", "junit");
		service.setParam("timestamp", "junit");
		service.setParam("groupId", "junitTest");
		service.resolve().get();
	}

	@Test
	public void payloadTest1() throws Exception {
		Service service = new MasterController(new DefaultService("Event haha"));
		service.setParam("uid", "junit");
		service.setParam("replyToken", "junit");
		service.setParam("timestamp", "junit");
		service.setParam("groupId", "junit");
		Service result = service.resolve().get();
		Assert.assertEquals ("Sorry, you can only use this function in a group chat",result.getFulfillment());
		service = new MasterController(new DefaultService("Event"));
		service.setParam("uid", "junit");
		service.setParam("replyToken", "junit");
		service.setParam("timestamp", "junit");
		service.setParam("groupId", "junitTest");
		result = service.resolve().get();
		Assert.assertEquals ("Please state the event name, type help if any help is needed",result.getFulfillment());
	}

	@Test
	public void AddEventPayloadTest() throws Exception {
		{
			Service service = new MasterController(new DefaultService("Event haha"));
			service.setParam("uid", "junit");
			service.setParam("replyToken", "junit");
			service.setParam("timestamp", "junit");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Event not found. Please create the event by format {EventName}@yyyy/mm/dd\n" +
					"e.g. Milestone 3 submit@2017/11/20", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("haha@11:11:11"));
			service.setParam("uid", "junit");
			service.setParam("replyToken", "junit");
			service.setParam("timestamp", "junit");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Please input correct date by yyyy/mm/dd", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("haha@2017/11/37"));
			service.setParam("uid", "junit");
			service.setParam("replyToken", "junit");
			service.setParam("timestamp", "junit");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Invalid day", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("haha@2017/1a/37"));
			service.setParam("uid", "junit");
			service.setParam("replyToken", "junit");
			service.setParam("timestamp", "junit");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Please enter correct date.", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("haha@"));
			service.setParam("uid", "junit");
			service.setParam("replyToken", "junit");
			service.setParam("timestamp", "junit");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Please follow that format {EventName}@yyyy/mm/dd\ne.g. Milestone 3 submit@2017/11/20", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("haha@2017/11/27"));
			service.setParam("uid", "junit");
			service.setParam("replyToken", "junit");
			service.setParam("timestamp", "junit");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Your event had been added", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("edit timeslot"));
			service.setParam("uid", "junit1");
			service.setParam("replyToken", "junit1");
			service.setParam("timestamp", "junit1");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Please give your command by: \n" +
					"1. Add new available time for the day by: e.g. add 18:00-19:30@2017/11/27\n" +
					"2. Drop existing  available time for the day by: e.g. drop 18:00-19:30@2017/11/27\n", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("add 18:00-19:30@2017/11/27"));
			service.setParam("uid", "junit1");
			service.setParam("replyToken", "junit1");
			service.setParam("timestamp", "junit1");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Your available timeslot are successfully added", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("edit timeslot"));
			service.setParam("uid", "junit1");
			service.setParam("replyToken", "junit1");
			service.setParam("timestamp", "junit1");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Please give your command by: \n" +
					"1. Add new available time for the day by: e.g. add 18:00-19:30@2017/11/27\n" +
					"2. Drop existing  available time for the day by: e.g. drop 18:00-19:30@2017/11/27\n", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("add 19:30-20:30@2017/11/27"));
			service.setParam("uid", "junit1");
			service.setParam("replyToken", "junit1");
			service.setParam("timestamp", "junit1");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Sorry, you can only create one timeslot for single day", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("add 19:30-20:30@"));
			service.setParam("uid", "junit1");
			service.setParam("replyToken", "junit1");
			service.setParam("timestamp", "junit1");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Please follow the format hh:mm-hh:mm@yyyy/mm/dd", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("add 19:30-20:30@2017/11/28"));
			service.setParam("uid", "junit1");
			service.setParam("replyToken", "junit1");
			service.setParam("timestamp", "junit1");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Your available timeslot are successfully added", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("event haha"));
			service.setParam("uid", "junit");
			service.setParam("replyToken", "junit");
			service.setParam("timestamp", "junit");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Event haha has be found, the common timeslot for all of you are\n18:00-19:30\nPlease be reminded that not editing your available timeslot will be considered as available in whole day", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("edit timeslot"));
			service.setParam("uid", "junit1");
			service.setParam("replyToken", "junit1");
			service.setParam("timestamp", "junit1");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Please give your command by: \n" +
					"1. Add new available time for the day by: e.g. add 18:00-19:30@2017/11/27\n" +
					"2. Drop existing  available time for the day by: e.g. drop 18:00-19:30@2017/11/27\n", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("drop 18:0019:30@2017/11/27"));
			service.setParam("uid", "junit1");
			service.setParam("replyToken", "junit1");
			service.setParam("timestamp", "junit1");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Please follow the format hh:mm-hh:mm@yyyy/mm/dd", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("drop 19:30-20:30@2017/11/28"));
			service.setParam("uid", "junit1");
			service.setParam("replyToken", "junit1");
			service.setParam("timestamp", "junit1");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("The timeslot is removed.", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("edit timeslot"));
			service.setParam("uid", "junit");
			service.setParam("replyToken", "junit");
			service.setParam("timestamp", "junit");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Please give your command by: \n" +
					"1. Add new available time for the day by: e.g. add 18:00-19:30@2017/11/27\n" +
					"2. Drop existing  available time for the day by: e.g. drop 18:00-19:30@2017/11/27\n", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("hello"));
			service.setParam("uid", "junit");
			service.setParam("replyToken", "junit");
			service.setParam("timestamp", "junit");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Sorry, this function for editing timeslot is not supported", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("add 15:30-19:00@2017/11/27"));
			service.setParam("uid", "junit");
			service.setParam("replyToken", "junit");
			service.setParam("timestamp", "junit");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Your available timeslot are successfully added", result.getFulfillment());
		}

		{
			Service service = new MasterController(new DefaultService("event haha"));
			service.setParam("uid", "junit");
			service.setParam("replyToken", "junit");
			service.setParam("timestamp", "junit");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Event haha has be found, the common timeslot for all of you are\n18:00-19:00\nPlease be reminded that not editing your available timeslot will be considered as available in whole day", result.getFulfillment());
		}
		{
			Service service = new MasterController(new DefaultService("check"));
			service.setParam("uid", "junit");
			service.setParam("replyToken", "junit");
			service.setParam("timestamp", "junit");
			service.setParam("groupId", "junitTeam");
			Service result = service.resolve().get();
			Assert.assertEquals("Your timeslot:\n15:30-19:00@2017/11/27\n", result.getFulfillment());
		}



	}
	@Test
	public void ReviewAddChain() throws Exception {
		Service service = new ReviewAdd(new DefaultService(""));
		Assert.assertEquals(service, service.chain());
	}


}
