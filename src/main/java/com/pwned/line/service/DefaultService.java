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

	public DefaultService(Service service) {
		this.fulfillment = service.getFulfillment();
		this.args = service.getArgs();
	}

	@Override
	public CompletableFuture<Service> resolve() {
		return CompletableFuture.supplyAsync(() -> {
			this.fulfillment = "handled(" + this.fulfillment + ")";
			return this;
		});
	}

	@Override
	public void setParam(String key, Object value) {
		this.args.put(key, value);
	}

	@Override
	public Object getParam(String key) {
		return this.args.get(key);
	}

	@Override
	public String getFulfillment() {
		return this.fulfillment;
	}

	@Override
	public Map<String, Object> getArgs() {
		return this.args;
	}

}
