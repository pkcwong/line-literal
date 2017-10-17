package com.pwned.line.service;

import java.util.Map;

public interface Service {

	Service resolve();
	void setParam(String key, Object value);
	Object getParam(String key);

	String getFulfillment();
	Map<String, Object> getArgs();

}
