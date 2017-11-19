package com.pwned.line.service;

import com.pwned.line.web.MongoDB;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReviewTest {
	@Before
	public void setUp() throws Exception {
		new MongoDB(System.getenv("MONGODB_URI")).drop("department");
		new MongoDB(System.getenv("MONGODB_URI")).drop("number");
		new MongoDB(System.getenv("MONGODB_URI")).drop("ReviewAdd");
		new MongoDB(System.getenv("MONGODB_URI")).drop("user");
		new MongoDB(System.getenv("MONGODB_URI")).drop("anonymous");
		try {
			Service service = new MasterController(new DefaultService("review"));
			service.setParam("uid", "junit");
			service.setParam("replyToken", "junit");
			service.setParam("timestamp", "junit");
			service.resolve().get();
		} catch (Exception e) {
			e.printStackTrace();
			assert false;
		}
	}

//	@After
//	public void tearDown() throws Exception {
//	}

	@Test
	public void DialogFlowReviewPayload() throws Exception {
		Service service = new DialogFlowReview(new DefaultService("Give me a review for COMP 3111"));
		service.setParam("uid", "junit");
		service.setParam("replyToken", "junit");
		service.setParam("timestamp", "junit");
		service.payload();
		assertEquals ("COMP", service.getParam("department").toString());
		assertEquals ("3111", service.getParam("number").toString());
		assertEquals ("", service.getParam("ReviewAdd").toString());
	}

}