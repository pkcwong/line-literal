package com.pwned.line.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface Service {

	CompletableFuture<Service> resolve();

	void setParam(String key, Object value);

	Object getParam(String key);

	String getFulfillment();

	Map<String, Object> getArgs();

	void dump();

}
