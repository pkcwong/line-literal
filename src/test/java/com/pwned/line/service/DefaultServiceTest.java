package com.pwned.line.service;

import org.junit.Test;

import java.util.Random;

import static junit.framework.Assert.assertEquals;

public class DefaultServiceTest {

	@Test
	public final void resolve() throws Exception {
		byte[] block = new byte[128];
		new Random().nextBytes(block);
		Service service = new DefaultService(new String(block));
		service.resolve().get();
	}

	@Test
	public void payload() throws Exception {
		byte[] block = new byte[128];
		new Random().nextBytes(block);
		Service service = new DefaultService(new String(block));
		service.payload();
		assertEquals(new String(block), service.getFulfillment());
	}

	@Test
	public void chain() throws Exception {
		byte[] block = new byte[128];
		new Random().nextBytes(block);
		Service service = new DefaultService(new String(block));
		assertEquals(service, service.chain());
	}

	@Test
	public void dump() throws Exception {
		byte[] block = new byte[128];
		new Random().nextBytes(block);
		Service service = new DefaultService(new String(block));
		service.dump();
	}

}

