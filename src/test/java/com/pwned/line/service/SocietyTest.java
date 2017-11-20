/*
package com.pwned.line.service;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class SocietyTest {

	@Test
	public void payloadMSA() throws Exception {
		Service service = new DialogFlowSociety(new DefaultService("MSA"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("@society::name", service.getFulfillment());
		assertEquals(Society.class, service.chain().getClass());
		service = new Society(service);
		service.payload();
		assertEquals(true, service.getFulfillment().contains("I have found your desired society:"));
		assertEquals(Society.class, service.chain().getClass());
	}

	@Test
	public void payloadCoding() throws Exception {
		Service service = new DialogFlowSociety(new DefaultService("society coding"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("@society::name", service.getFulfillment());
		assertEquals(Society.class, service.chain().getClass());
		service = new Society(service);
		service.payload();
		assertEquals("I am sorry, the society that you entered is not in our database.", service.getFulfillment());
		assertEquals(Society.class, service.chain().getClass());
	}

	@Test
	public void payloadCanDo() throws Exception {
		Service service = new DialogFlowSociety(new DefaultService("can do"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("@society::name", service.getFulfillment());
		assertEquals(Society.class, service.chain().getClass());
		service = new Society(service);
		service.payload();
		assertEquals("I am sorry, the society that you entered is not valid.", service.getFulfillment());
		assertEquals(Society.class, service.chain().getClass());
	}

}
*/