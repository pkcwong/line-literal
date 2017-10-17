package com.pwned.line.service;

import java.util.concurrent.CompletableFuture;

public interface Service {

	CompletableFuture<String> resolve();
	void setParam(String key, Object value);
	Object getParam(String key);

}
