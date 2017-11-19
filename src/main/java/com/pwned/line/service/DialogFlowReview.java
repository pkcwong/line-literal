package com.pwned.line.service;

import com.pwned.line.web.ApiAI;
import org.json.JSONObject;

/***
 * Service module for DialogFlow for Course Review
 * Required params: [uid]
 * Reserved tokens: [parameters]
 * Resolved params: [parameters]
 * @author Calvin Ku
 */

public class DialogFlowReview extends DefaultService{

	/**
	 * Constructor for DialogFlowReview
	 * @param service
	 */
	public DialogFlowReview(Service service) { super(service); }

	private static final String API_AI_ACCESS_TOKEN = "bd33003adb6c4a5d8da4e278eaee8ee6";

	/***
	 * Payload for DialogFlow Course Review
	 * @throws Exception
	 */
	@Override
	public void payload() throws Exception {
		JSONObject response = new ApiAI(API_AI_ACCESS_TOKEN, this.getParam("uid").toString(), this.fulfillment).execute();
		this.setParam("parameters", response.getJSONObject("result").getJSONObject("parameters"));
		this.fulfillment = response.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
	}

	/***
	 * Chain for DialogFlow Course Review
	 * @return Service state
	 * @throws Exception
	 */
	@Override
	public Service chain() throws Exception {
		return new Review(this).resolve().get();
	}

}
