package com.pwned.line.service;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/***
 * Default text message handler class.
 * @author Christopher Wong
 */
public class DefaultService implements Service {

	protected String fulfillment = null;
	protected Map<String, Object> args = null;

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

	/***
	 * Default handler for text messages.
	 * @return instance
	 */
	@Override
	public CompletableFuture<Service> resolve() throws Exception {
		this.dump();
		return CompletableFuture.supplyAsync(() -> this);
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

	@Override
	public void dump() {
		try {
			JSONObject dump = new JSONObject();
			JSONObject mem = new JSONObject(this.getArgs());
			dump.put("class", this.getClass().getSimpleName());
			dump.put("fulfillment", this.getFulfillment());
			dump.put("args", mem);
			System.out.println(dump.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
