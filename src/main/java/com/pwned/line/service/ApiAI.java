package com.pwned.line.service;

import com.pwned.line.http.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/***
 * Service for sending requests to DialogFlow.
 * Required params: [ACCESS_TOKEN, uid]
 * Resolved params: [parameters]
 * @author Christopher Wong, Calvin Ku
 */
public class ApiAI implements Service {

	private static final String BASE_URL = "https://api.dialogflow.com/v1/query";
	private static final String VERSION = "20170712";

	private String fulfillment = null;
	private Map<String, Object> args = null;

	public ApiAI(String query) {
		this.fulfillment = query;
		this.args = new HashMap<>();
	}

	public ApiAI(String query, Map<String, Object> args) {
		this.fulfillment = query;
		this.args = args;
	}

	public ApiAI(Service service) {
		this.fulfillment = service.getFulfillment();
		this.args = service.getArgs();
	}

	/***
	 * DialogFlow payload
	 * @return instance
	 */
	@Override
	public CompletableFuture<Service> resolve() {
		try {
			HTTP http = new HTTP(BASE_URL);
			http.setHeaders("Authorization", "Bearer " + this.getParam("ACCESS_TOKEN"));
			http.setHeaders("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
			http.setParams("v", VERSION);
			http.setParams("query", this.fulfillment);
			http.setParams("sessionId", this.getParam("uid"));
			JSONObject json = new JSONObject(http.get());
			this.handler(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CompletableFuture.supplyAsync(() -> this);
	}

	/***
	 * Handles DialogFlow response.
	 * @param json http response
	 */
	private void handler(JSONObject json) {
		try {
			this.fulfillment = json.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
			this.setParam("parameters", json.getJSONObject("result").getJSONObject("parameters"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
