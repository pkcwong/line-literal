package com.pwned.line.web;

import com.pwned.line.http.HTTP;
import org.json.JSONObject;

public class ApiAI {

	private static final String BASE_URL = "https://api.dialogflow.com/v1/query";
	private static final String VERSION = "20170712";

	private String ACCESS_TOKEN = "";
	private String query = "";
	private String session = "";

	public ApiAI(String token, String session, String query) {
		this.ACCESS_TOKEN = token;
		this.session = session;
		this.query = query;
	}

	public JSONObject execute() throws Exception {
		HTTP http = new HTTP(BASE_URL);
		http.setHeaders("Authorization", "Bearer " + ACCESS_TOKEN);
		http.setHeaders("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
		http.setParams("v", VERSION);
		http.setParams("query", this.query);
		http.setParams("sessionId", this.session);
		System.out.println("Result of http get: " + http.get());
		return new JSONObject(http.get());
	}

}
