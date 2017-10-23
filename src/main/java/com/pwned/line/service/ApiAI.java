package com.pwned.line.service;

import com.pwned.line.sys.Environment;
import com.pwned.line.http.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

/***
 * Service for sending requests to DialogFlow.
 * Required params: [uid]
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
	 */
	@Override
	public void payload() {
		try {
			HTTP http = new HTTP(BASE_URL);
			http.setHeaders("Authorization", "Bearer " + Environment.API_AI_ACCESS_TOKEN);
			http.setHeaders("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
			http.setParams("v", VERSION);
			http.setParams("query", this.fulfillment);
			http.setParams("sessionId", this.getParam("uid"));
			JSONObject json = new JSONObject(http.get());
			this.fulfillment = json.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
			this.setParam("parameters", json.getJSONObject("result").getJSONObject("parameters"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Service chain() throws Exception {
		return new CourseName(this);
	}

}
