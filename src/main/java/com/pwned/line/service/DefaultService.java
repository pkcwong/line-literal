package com.pwned.line.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DefaultService implements Service {

	private String fulfillment = null;
	private Map<String, Object> args = null;

	public DefaultService(String query) {
		this.fulfillment = query;
		this.args = new HashMap<>();
	}

	public DefaultService(String query, Map<String, Object> args) {
		this.fulfillment = query;
		this.args = args;
	}

	@Override
	public CompletableFuture<String> resolve() {
		return CompletableFuture.supplyAsync(() -> this.fulfillment);
	}

	@Override
	public void setParam(String key, Object value) {
		this.args.put(key, value);
	}

	@Override
	public Object getParam(String key) {
		return this.args.get(key);
	}

}
