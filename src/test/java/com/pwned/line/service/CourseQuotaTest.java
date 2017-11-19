package com.pwned.line.service;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CourseQuotaTest {

	@Test
	public void payload1() throws Exception {
		Service service = new DialogFlowCourseQuota(new DefaultService("Course Quota of COMP 3001"));
		service.setParam("uid", "junit");
		service.setParam("replyToken", "junit");
		service.setParam("timestamp", "junit");
		service.payload();
		CourseQuota courseQuota = new CourseQuota(service);
		courseQuota.payload();
		assertEquals("Sorry COMP3001 could not be found in Course Quota Page", courseQuota.getFulfillment());
	}

	@Test
	public void payload0() throws Exception {
		Service service = new DialogFlowCourseQuota(new DefaultService("Course Quota of COMP 3111"));
		service.setParam("uid", "junit");
		service.setParam("replyToken", "junit");
		service.setParam("timestamp", "junit");
		service.payload();
		CourseQuota courseQuota = new CourseQuota(service);
		courseQuota.payload();
		assertEquals("", courseQuota.getFulfillment());
	}
}