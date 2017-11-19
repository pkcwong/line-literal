package com.pwned.line.service;

import org.json.JSONObject;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class KMBTest {

	@Test
	public void payloadHKUSTSouthGate() throws Exception{
		Service service = new DialogFlowKMB(new DefaultService("Next Bus at HKUST South Gate"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("@kmb::eta", service.getFulfillment());
		assertEquals("HKUST South Gate", new JSONObject(service.getParam("parameters").toString()).getString("busstop"));
		assertEquals(KMB.class, service.chain().getClass());
		service = new KMB(service);
		service.payload();
		assertEquals(true, service.getFulfillment().contains("The following are the arrival time of the buses at HKUST South Gate:"));
		assertEquals(KMB.class, service.chain().getClass());
	}

	@Test
	public void payloadHKUSTNorthGate() throws Exception{
		Service service = new DialogFlowKMB(new DefaultService("Next Bus at HKUST North Gate"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("@kmb::eta", service.getFulfillment());
		assertEquals("HKUST North Gate", new JSONObject(service.getParam("parameters").toString()).getString("busstop"));
		assertEquals(KMB.class, service.chain().getClass());
		service = new KMB(service);
		service.payload();
		assertEquals(true, service.getFulfillment().contains("The following are the arrival time of the buses at HKUST North Gate:"));
		assertEquals(KMB.class, service.chain().getClass());
	}

	@Test
	public void payloadLokFu() throws Exception {
		Service service = new DialogFlowKMB(new DefaultService("Next Bus at Lok Fu"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("@kmb::eta", service.getFulfillment());
		assertEquals("Lok Fu", new JSONObject(service.getParam("parameters").toString()).getString("busstop"));
		assertEquals(KMB.class, service.chain().getClass());
		service = new KMB(service);
		service.payload();
		assertEquals("The busstop in not in our database.", service.getFulfillment());
		assertEquals(KMB.class, service.chain().getClass());
	}

	@Test
	public void payloadNowhere() throws Exception {
		Service service = new DialogFlowKMB(new DefaultService("Next Bus at Nowhere"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("@kmb::eta", service.getFulfillment());
		assertEquals("", new JSONObject(service.getParam("parameters").toString()).getString("busstop"));
		assertEquals(KMB.class, service.chain().getClass());
		service = new KMB(service);
		service.payload();
		assertEquals("Please enter a valid bus stop", service.getFulfillment());
		assertEquals(KMB.class, service.chain().getClass());
	}
}
