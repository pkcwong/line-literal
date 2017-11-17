package com.pwned.line.service;

import org.junit.Test;

import java.util.Random;

import static junit.framework.Assert.assertEquals;

public class DefaultServiceTest {

	public DefaultServiceTest() {

	}

	@Test
	public final void resolve() {
		byte[] block = new byte[128];
		new Random().nextBytes(block);
		Service service = new DefaultService(new String(block));
		try {
			service.resolve().get();
			assert true;
		} catch (Exception e) {
			assert false;
		}
		assert true;
	}

	@Test
	public void payload() {
		byte[] block = new byte[128];
		new Random().nextBytes(block);
		Service service = new DefaultService(new String(block));
		try {
			service.payload();
			assert true;
		} catch (Exception e) {
			assert false;
		}
		assertEquals(new String(block), service.getFulfillment());
	}

	@Test
	public void chain() {
		byte[] block = new byte[128];
		new Random().nextBytes(block);
		Service service = new DefaultService(new String(block));
		try {
			assertEquals(service, service.chain());
			assert true;
		} catch (Exception e) {
			assert false;
		}
	}

	@Test
	public void dump() {
		byte[] block = new byte[128];
		new Random().nextBytes(block);
		Service service = new DefaultService(new String(block));
		try {
			service.dump();
			assert true;
		} catch (Exception e) {
			assert false;
		}
	}

}
