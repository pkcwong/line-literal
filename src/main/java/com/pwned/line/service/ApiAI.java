package com.pwned.line.service;

import com.pwned.line.http.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/***
 * Service for sending requests to DialogFlow.
 * Required params: [ACCESS_TOKEN, uid]
 * Resolved params: [parameters]
 * @author Christopher Wong, Calvin Ku
 */
public class ApiAI extends Service {

	private static final String BASE_URL = "https://api.dialogflow.com/v1/query";
	private static final String VERSION = "20170712";

	public ApiAI(String query) {
		super(query);
	}

	public ApiAI(String query, Map<String, Object> args) {
		super(query, args);
	}

	/***
	 * Sends a query to DialogFlow.
	 * @return fulfillment
	 */
	@Override
	public String resolve() {
		try {
			HTTP http = new HTTP(BASE_URL);
			http.setHeaders("Authorization", "Bearer " + super.getArgs("ACCESS_TOKEN"));
			http.setHeaders("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
			http.setParams("v", VERSION);
			http.setParams("query", super.fulfillment);
			http.setParams("sessionId", super.getArgs("uid"));
			JSONObject json = new JSONObject(http.get());
			System.out.println(json);
			handler(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.fulfillment;
	}

	/***
	 * Handles DialogFlow response.
	 * @param json http response
	 */
	private void handler(JSONObject json) {
		try {
			super.fulfillment = json.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
			super.setArgs("parameters", json.getJSONObject("result").getJSONObject("parameters"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
