/*
package com.pwned.line.service;

import com.pwned.line.web.MongoDB;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class NotifyTest {

	@BeforeClass
	public static void before() {
		new MongoDB(System.getenv("MONGODB_URI")).drop("kmb");
	}

	@Test
	public void payloadNotify() throws Exception {
		Service service = new KMBNotify(new DefaultService("Notify"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals(KMBNotify.class, service.chain().getClass());
		assertEquals("We will start notifying you about the arrvial time. Please tell us to stop if you no longer need this service.", service.getFulfillment());
		service.payload();
		assertEquals("You are already in our list of notification.", service.getFulfillment());
	}

}
*/
