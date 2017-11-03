package com.pwned.line.service;

import com.pwned.line.web.ApiAI;
import org.json.JSONObject;

public class DialogFlowReview extends DefaultService{

	public DialogFlowReview(Service service) { super(service); }

	private static final String API_AI_ACCESS_TOKEN = "bd33003adb6c4a5d8da4e278eaee8ee6";

	@Override
	public void payload() throws Exception {
		JSONObject response = new ApiAI(API_AI_ACCESS_TOKEN, this.getParam("uid").toString(), this.fulfillment).execute();
		this.setParam("parameters", response.getJSONObject("result").getJSONObject("parameters"));
		this.fulfillment = response.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
	}

	@Override
	public Service chain() throws Exception {
		return new Review(this).resolve().get();
	}

}
