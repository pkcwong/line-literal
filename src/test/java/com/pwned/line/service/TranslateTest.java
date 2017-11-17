package com.pwned.line.service;

import org.json.JSONObject;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class TranslateTest {

	@Test
	public void payload() throws Exception {
		Service service = new DialogFlowTranslate(new DefaultService("translate good morning to Indonesian"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("@translate", service.getFulfillment());
		assertEquals("id", new JSONObject(service.getParam("parameters").toString()).getString("lang-to"));
		assertEquals("good morning", new JSONObject(service.getParam("parameters").toString()).getString("phrase"));
		assertEquals(Translate.class, service.chain().getClass());
		service = new Translate(service);
		service.payload();
		assertEquals("selamat pagi", service.getFulfillment());
		assertEquals(Translate.class, service.chain().getClass());
	}

}
