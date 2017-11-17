package com.pwned.line.service;

import com.pwned.line.web.MongoDB;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class AnonymousChatTest {

	@BeforeClass
	public static void before() {
		new MongoDB(System.getenv("MONGODB_URI")).drop("user");
		new MongoDB(System.getenv("MONGODB_URI")).drop("anonymous");
		try {
			Service service = new MasterController(new DefaultService("anonymous"));
			service.setParam("uid", "junit0");
			service.setParam("replyToken", "junit0");
			service.setParam("timestamp", "junit0");
			service.resolve().get();
		} catch (Exception e) {
			e.printStackTrace();
			assert false;
		}
		try {
			Service service = new MasterController(new DefaultService("anonymous"));
			service.setParam("uid", "junit1");
			service.setParam("replyToken", "junit1");
			service.setParam("timestamp", "junit1");
			service.resolve().get();
		} catch (Exception e) {
			e.printStackTrace();
			assert false;
		}
	}

	@Test
	public void payload0() throws Exception {
		{
			Service service = new AnonymousChat(new DefaultService("anonymous::connect"));
			service.setParam("uid", "junit0");
			service.setParam("replyToken", "junit0");
			service.setParam("timestamp", "junit0");
			service.resolve().get();
		}
		{
			Service service = new AnonymousChat(new DefaultService("anonymous::connect"));
			service.setParam("uid", "junit1");
			service.setParam("replyToken", "junit1");
			service.setParam("timestamp", "junit1");
			service.resolve().get();
		}
		assertEquals(2, MongoDB.get(new MongoDB(System.getenv("MONGODB_URI")).getCollection("anonymous").find()).size());
		AnonymousChat.run();
		assertEquals(0, MongoDB.get(new MongoDB(System.getenv("MONGODB_URI")).getCollection("anonymous").find()).size());
		Service service = new MasterController(new DefaultService(""));
		service.setParam("uid", "junit0");
		service.setParam("replyToken", "junit0");
		service.setParam("timestamp", "junit0");
		service.resolve().get();
		assertEquals("junit1", service.getParam("bind").toString());
	}

	@Test
	public void payload1() throws Exception {
		{
			Service service = new AnonymousChat(new DefaultService("anonymous::terminate"));
			service.setParam("uid", "junit0");
			service.setParam("replyToken", "junit0");
			service.setParam("timestamp", "junit0");
			service.resolve().get();
		}
		Service service = new MasterController(new DefaultService("anonymous"));
		service.setParam("uid", "junit0");
		service.setParam("replyToken", "junit0");
		service.setParam("timestamp", "junit0");
		service.resolve().get();
		assertEquals("junit0", service.getParam("bind").toString());
	}

}
