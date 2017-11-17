package com.pwned.line.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultServiceTest {

	@Test
	void payload() throws Exception {
		Service service = new DefaultService("Hello World");
		service.payload();
		assertEquals("Hello World", service.getFulfillment());
	}

}
