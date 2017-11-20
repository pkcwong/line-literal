package com.pwned.line.service;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;

public class NineDaysWeatherTest{

	@Test
	public void payloadWeather() throws Exception{
		Service service = new DialogFlowNineDaysWeather(new DefaultService("Next Nine Days"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("@weather::ninedaysweather", service.getFulfillment());
		assertEquals(NineDaysWeather.class, service.chain().getClass());
		service = new NineDaysWeather(service);
		service.payload();
		assertEquals("The above is the weather forecast for next 9 days.", service.getFulfillment());
	}

}
