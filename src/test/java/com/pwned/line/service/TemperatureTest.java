package com.pwned.line.service;
import org.json.JSONObject;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;

public class TemperatureTest {

	@Test
	public void payloadHKUST() throws Exception{
		Service service = new DialogFlowTemperature(new DefaultService("Temperature at HKUST"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("@weather::temperature", service.getFulfillment());
		assertEquals("Sai Kung", new JSONObject(service.getParam("parameters").toString()).getString("Region1"));
		assertEquals(Temperature.class, service.chain().getClass());
		service = new Temperature(service);
		service.payload();
		assertEquals(true, service.getFulfillment().contains("The temperature at"));
		assertEquals(Temperature.class, service.chain().getClass());
	}

	@Test
	public void payloadHK() throws Exception{
		Service service = new DialogFlowTemperature(new DefaultService("Temperature at Hong Kong"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("@weather::temperature", service.getFulfillment());
		assertEquals("Hong Kong", new JSONObject(service.getParam("parameters").toString()).getString("Region1"));
		assertEquals(Temperature.class, service.chain().getClass());
		service = new Temperature(service);
		service.payload();
		assertEquals(true, service.getFulfillment().contains("The temperature at Hong Kong is"));
		assertEquals(Temperature.class, service.chain().getClass());
	}

	@Test
	public void payloadhome() throws Exception{
		Service service = new DialogFlowTemperature(new DefaultService("Temperature at home"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("@weather::temperature", service.getFulfillment());
		assertEquals("", new JSONObject(service.getParam("parameters").toString()).getString("Region1"));
		assertEquals(Temperature.class, service.chain().getClass());
		service = new Temperature(service);
		service.payload();
		assertEquals(true, service.getFulfillment().contains("Hong Kong"));
		assertEquals(Temperature.class, service.chain().getClass());
	}

	@Test
	public void payloadChrishome() throws Exception{
		Service service = new DialogFlowTemperature(new DefaultService("Temperature at Chris' home"));
		service.setParam("uid", "junit");
		service.payload();
		assertEquals("@weather::temperature", service.getFulfillment());
		assertEquals("Chris' home", new JSONObject(service.getParam("parameters").toString()).getString("Region1"));
		assertEquals(Temperature.class, service.chain().getClass());
		service = new Temperature(service);
		service.payload();
		assertEquals(true, service.getFulfillment().contains(" is not available"));
		assertEquals(Temperature.class, service.chain().getClass());
	}
}
