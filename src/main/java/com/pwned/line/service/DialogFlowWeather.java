package com.pwned.line.service;

import com.pwned.line.web.ApiAI;
import org.json.JSONObject;

/***
 * Service module for DialogFlow Weather
 * Required params: [uid]
 * Reserved tokens: []
 * Resolved params: [parameters]
 * @author Timothy Pak
 */
public class DialogFlowWeather extends DefaultService {

	private static String API_AI_ACCESS_TOKEN = "1918af645bed436db988189c2f77f832";

	public DialogFlowWeather(Service service) {
		super(service);
	}

	@Override
	public void payload() throws Exception {
		JSONObject response = new ApiAI(API_AI_ACCESS_TOKEN, this.getParam("uid").toString(), this.fulfillment).execute();
		this.setParam("parameters", response.getJSONObject("result").getJSONObject("parameters"));
		this.fulfillment = response.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
	}

	@Override
	public Service chain() throws Exception {
		return new Weather(this).resolve().get();
	}

}
