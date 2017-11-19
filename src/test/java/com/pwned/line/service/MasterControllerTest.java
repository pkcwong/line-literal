package com.pwned.line.service;

import com.pwned.line.web.MongoDB;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MasterControllerTest {

	@Before
	public void before() {
		new MongoDB(System.getenv("MONGODB_URI")).drop("user");
	}

	@Test
	public void help() throws Exception {
		Service service = new MasterController(new DefaultService("help"));
		service.setParam("uid", "junit");
		service.setParam("timestamp", "junit");
		service.setParam("replyToken", "junit");
		service.payload();
		assertEquals(MasterController.class, service.chain().getClass());
	}

}
