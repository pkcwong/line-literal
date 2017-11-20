package com.pwned.line.service;
import com.pwned.line.web.MongoDB;
import org.junit.BeforeClass;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;

public class StopNotifyTest{

	@BeforeClass
	public static void before(){
		new MongoDB(System.getenv("MONGODB_URI")).drop("kmb");
	}

	@Test
	public void payloadStop() throws Exception{
		Service service = new KMBNotify(new DefaultService("Notify"));
		service.setParam("uid", "junit");
		service.payload();
		service = new KMBStopNotify(new DefaultService("Stop"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals(KMBStopNotify.class, service.chain().getClass());
		assertEquals("Ok. We will stop notify you about bus arrival time.", service.getFulfillment());
		service.payload();
		assertEquals("You are not in our list of notification.", service.getFulfillment());
	}

}