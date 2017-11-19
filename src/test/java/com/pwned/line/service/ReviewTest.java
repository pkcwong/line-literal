package com.pwned.line.service;

import com.pwned.line.web.MongoDB;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReviewTest {
	@Before
	public void setUp() throws Exception {
		new MongoDB(System.getenv("MONGODB_URI")).drop("CourseReview");
		Service service = new MasterController(new DefaultService("review"));
		service.setParam("uid", "junit");
		service.setParam("replyToken", "junit");
		service.setParam("timestamp", "junit");
		service.resolve().get();
	}

	@Test
	public void DialogFlowReviewPayload() throws Exception {
		Service service = new DialogFlowReview(new DefaultService("Give me a review for COMP 3111"));
		service.setParam("uid", "junit");
		service.setParam("replyToken", "junit");
		service.setParam("timestamp", "junit");
		service.payload();
		assertEquals ("COMP", new JSONObject(service.getParam("parameters").toString()).getString("department"));
		assertEquals ("3111", new JSONObject(service.getParam("parameters").toString()).getString("number"));
		assertEquals ("", new JSONObject(service.getParam("parameters").toString()).getString("ReviewAdd"));
	}

	@Test
	public void ReviewAddPayload() throws Exception {
		{
			Service service = new DialogFlowReview(new DefaultService("Add review for COMP 3111"));
			service.setParam("uid", "junit");
			service.setParam("replyToken", "junit");
			service.setParam("timestamp", "junit");
			service.payload();
			service = new Review(service);
			service.payload();
			assertEquals("You can type your detail review here: ", service.getFulfillment());
		}
		{
			Service service = new ReviewAdd(new DefaultService("I LOVE DEBAGA <3"));
			service.setParam("uid", "junit");
			service.setParam("replyToken", "junit");
			service.setParam("timestamp", "junit");
			service.payload();
			assertEquals("Your course review had been added", service.getFulfillment());
		}
		{
			Service service = new DialogFlowReview(new DefaultService("Review of COMP 3111"));
			service.setParam("uid", "junit");
			service.setParam("replyToken", "junit");
			service.setParam("timestamp", "junit");
			service.payload();
			service = new Review(service);
			service.payload();
			assertEquals("Here is a random review of COMP3111:\nReview: I LOVE DEBAGA <3", service.getFulfillment());
		}

	}

	@Test
	public void ReviewAddChain() throws Exception {
		Service service = new ReviewAdd(new DefaultService(""));
		assertEquals(service, service.chain());
	}

}