package com.pwned.line.service;

import com.mongodb.BasicDBObject;
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
		mongo.getCollection("party").findOneAndUpdate(new BasicDBObject().append("uid", "junit"), new BasicDBObject("$set", new BasicDBObject().append("Accept", "Y")));
		service.payload();
		assertEquals("That's good!", service.getFulfillment());
		assertEquals(Bring.class, service.chain().getClass());
		service.payload();
		assertEquals("Someone is already bringing ", service.getFulfillment());
	}
}
