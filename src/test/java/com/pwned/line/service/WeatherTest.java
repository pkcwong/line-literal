/*
package com.pwned.line.service;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class WeatherTest {

	@Test
	public void payloadWeather() throws Exception {
		Service service = new DialogFlowWeather(new DefaultService("Weather"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("@weather::weather", service.getFulfillment());
		assertEquals(Weather.class, service.chain().getClass());
		service = new Weather(service);
		service.payload();
		assertEquals(true, service.getFulfillment().contains("Weather forecast"));
	}

}
*/