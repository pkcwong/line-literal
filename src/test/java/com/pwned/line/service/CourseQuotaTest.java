package com.pwned.line.service;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CourseQuotaTest {

	@Test
	public void payload() throws Exception {
		Service service = new DialogFlowCourseQuota(new DefaultService("Course Quota of COMP 3111"));
		service.setParam("uid", "junit");
		service.setParam("replyToken", "junit");
		service.setParam("timestamp", "junit");
		service.payload();

		assertEquals();
	}

}