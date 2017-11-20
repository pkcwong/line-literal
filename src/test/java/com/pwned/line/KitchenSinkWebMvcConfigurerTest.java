package com.pwned.line;

import org.junit.Test;

public class KitchenSinkWebMvcConfigurerTest {

	@Test(expected = NullPointerException.class)
	public void addResourceHandlers() throws Exception {
		new KitchenSinkWebMvcConfigurer().addResourceHandlers(null);
	}

}
