package com.pwned.line.service;

import com.pwned.line.web.ApiAI;
import org.json.JSONObject;

/***
 * Service for finding closest lift by location.
 * Required params: [uid]
 * Reserved tokens: [API_AI]
 * Resolved params: []
 * @author Bear
 */

public class DialogFlowLiftAdvisor extends DefaultService {

	private static final String API_AI_ACCESS_TOKEN = "e6e935812bcb48a0a2f8a882e08a4928";

	public DialogFlowLiftAdvisor(Service service) {
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
		return new LiftAdvisor(this).resolve().get();
	}

}
