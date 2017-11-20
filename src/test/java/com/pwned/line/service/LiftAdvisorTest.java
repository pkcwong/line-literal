/*
package com.pwned.line.service;

import org.json.JSONObject;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class LiftAdvisorTest {

	@Test
	public void payload() throws Exception {
		Service service = new DialogFlowLiftAdvisor(new DefaultService("where is room 3211?"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("room 3211 is near @LiftAdvisor", service.getFulfillment());
		assertEquals("room 3211", new JSONObject(service.getParam("parameters").toString()).getString("location"));
		assertEquals(LiftAdvisor.class, service.chain().getClass());
		service = new LiftAdvisor(service);
		service.payload();
		assertEquals("room 3211 is near Lift 19", service.getFulfillment());
		assertEquals(LiftAdvisor.class, service.chain().getClass());
	}

}
*/