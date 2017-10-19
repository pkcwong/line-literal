package com.pwned.line.service;

import com.pwned.line.http.HTTP;
import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;

/***
 * Service for sending requests to DialogFlow.
 * Required params: [ACCESS_TOKEN, uid]
 * Resolved params: [parameters]
 * @author Christopher Wong, Calvin Ku
 */
public class ApiAI extends DefaultService {

	private static final String BASE_URL = "https://api.dialogflow.com/v1/query";
	private static final String VERSION = "20170712";

	public ApiAI(Service service) {
		super(service);
	}

	/***
	 * DialogFlow payload
	 * @return instance
	 */
	@Override
	public CompletableFuture<Service> resolve() throws Exception {
		HTTP http = new HTTP(BASE_URL);
		http.setHeaders("Authorization", "Bearer " + this.getParam("ACCESS_TOKEN"));
		http.setHeaders("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
		http.setParams("v", VERSION);
		http.setParams("query", this.fulfillment);
		http.setParams("sessionId", this.getParam("uid"));
		JSONObject json = new JSONObject(http.get());
		this.handler(json);
		return CompletableFuture.supplyAsync(() -> this);
	}

	/***
	 * Handles DialogFlow response.
	 * @param json http response
	 */
	private void handler(JSONObject json) throws Exception {
		this.fulfillment = json.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
		this.setParam("parameters", json.getJSONObject("result").getJSONObject("parameters"));
	}

}
