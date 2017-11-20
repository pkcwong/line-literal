package com.pwned.line.service;
import com.pwned.line.web.MongoDB;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;

public class AcceptTest{

	@BeforeClass
	public static void before(){
		new MongoDB(System.getenv("MONGODB_URI")).drop("user");
		new MongoDB(System.getenv("MONGODB_URI")).drop("party");
	}

	@Test
	public void payloadAccept() throws Exception{
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		org.bson.Document data3 = new org.bson.Document();
		data3.append("uid", "junit");
		data3.append("name", "Timothy Pak");
		data3.append("Accept", "N");
		mongo.getCollection("party").insertOne(data3);
		Service service = new DialogFlowAccept(new DefaultService("Accept"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("@thanksgiving::accept", service.getFulfillment());
		assertEquals("accept", new JSONObject(service.getParam("parameters").toString()).getString("accept"));
		service = new Accept(service);
		service.payload();
		assertEquals(true, service.getFulfillment().contains("Thank you for joining,"));
		assertEquals(Accept.class, service.chain().getClass());
		Service newService = new DialogFlowAccept(new DefaultService("Accept"));
		newService.setParam("uid", "junit");
		newService.payload();
		newService = new Accept(newService);
		newService.payload();
		assertEquals(true, newService.getFulfillment().contains("You have already accepted the invitation to the thanksgiving party."));
	}
}
