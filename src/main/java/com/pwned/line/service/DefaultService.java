package com.pwned.line.service;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/***
 * Default Service module.
 * Required params: []
 * Reserved tokens: []
 * Resolved params: []
 * @author Christopher Wong
 */
public class DefaultService implements Service {

	protected String fulfillment = null;
	protected Map<String, Object> args = null;

	public DefaultService(String query) {
		this.fulfillment = query;
		this.args = new HashMap<>();
	}

	public DefaultService(Service service) {
		this.fulfillment = service.getFulfillment();
		this.args = service.getArgs();
	}

	@Override
	public CompletableFuture<Service> resolve() {
		this.dump();
		this.payload();
		this.dump();
		return CompletableFuture.supplyAsync(() -> {
			try {
				return this.chain();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		});
	}

	/***
	 * Default Service provides no processing.
	 */
	@Override
	public void payload() {

	}

	/***
	 * Pass query to API.AI
	 * @return Service
	 * @throws Exception Exception
	 */
	@Override
	public Service chain() throws Exception {
		return new ApiAI(this).resolve().get();
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
			dump.put("args", mem);
			dump.put("fulfillment", this.getFulfillment());
			dump.put("class", this.getClass().getSimpleName());
			System.out.println(dump.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
