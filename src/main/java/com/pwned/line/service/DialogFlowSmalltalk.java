package com.pwned.line.service;

import com.pwned.line.web.ApiAI;
import org.json.JSONObject;

/***
 * Service for small talk.
 * Required params: [parameters]
 * Reserved tokens: []
 * Resolved params: []
 * @author Bear
 */
public class DialogFlowSmalltalk extends DefaultService{

	public DialogFlowSmalltalk(Service service) { super(service); }

	private static final String API_AI_ACCESS_TOKEN = "8ced9810636e4273b57ac5af9335a2ab";

	@Override
	public void payload() throws Exception {
		JSONObject response = new ApiAI(API_AI_ACCESS_TOKEN, this.getParam("uid").toString(), this.fulfillment).execute();
		this.setParam("parameters", response.getJSONObject("result").getJSONObject("parameters"));
		this.fulfillment = response.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

}
