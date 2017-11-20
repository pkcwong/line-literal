package com.pwned.line.service;
import com.pwned.line.web.MongoDB;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class BringTest{

	@BeforeClass
	public static void before(){
		new MongoDB(System.getenv("MONGODB_URI")).drop("user");
		new MongoDB(System.getenv("MONGODB_URI")).drop("party");
		new MongoDB(System.getenv("MONGODB_URI")).drop("food");
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
	public void payloadBring() throws Exception{
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		org.bson.Document data3 = new org.bson.Document();
		data3.append("uid", "junit");
		data3.append("name", "Timothy Pak");
		data3.append("Accept", "N");
		mongo.getCollection("party").insertOne(data3);
		Service service = new DialogFlowBring(new DefaultService("Bring chicken wings"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("@thanksgiving::bring", service.getFulfillment());
		assertEquals("bring", new JSONObject(service.getParam("parameters").toString()).getString("bring"));
		assertEquals("chicken wings", new JSONObject(service.getParam("parameters").toString()).getString("food"));
		service = new Bring(service);
		service.payload();
		assertEquals(true, service.getFulfillment().contains("You haven't accept the invitation to the party!"));
		Service newService = new DialogFlowAccept(new DefaultService("Accept"));
		newService.setParam("uid", "junit");
		newService.payload();
		newService = new Accept(newService);
		newService.payload();
		Service oldService = new DialogFlowBring(new DefaultService("Bring chicken wings"));
		oldService.setParam("uid", "junit");
		oldService.payload();
		oldService = new Bring(oldService);
		oldService.payload();
		assertEquals(true, oldService.getFulfillment().contains("That's good!"));
		Service renewService = new DialogFlowBring(new DefaultService("Bring chicken wings"));
		renewService.setParam("uid", "junit");
		renewService.payload();
		renewService = new Bring(renewService);
		renewService.payload();
		assertEquals(true, renewService.getFulfillment().contains("Someone is already bringing "));
	}
}
