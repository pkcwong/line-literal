package com.pwned.line.service;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LiftAdvisorTest {

	@Test
	void payload() throws Exception {
		Service service = new DialogFlowLiftAdvisor(new DefaultService("where is room 3211?"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("room 3211 is near @LiftAdvisor", service.getFulfillment());
		assertEquals("room 3211", new JSONObject(service.getParam("parameters").toString()).getString("location"));
		service = new LiftAdvisor(service);
		service.payload();
		assertEquals("room 3211 is near Lift 19", service.getFulfillment());
	}

}
