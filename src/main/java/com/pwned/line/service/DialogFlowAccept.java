package com.pwned.line.service;

import com.pwned.line.web.ApiAI;
import org.json.JSONObject;

/***
 * Service module for DialogFlow Accept
 * Required params: [uid]
 * Reserved tokens: []
 * Resolved params: [parameters]
 * @author Timothy Pak
 */

public class DialogFlowAccept extends DefaultService{

	private static String API_AI_ACCESS_TOKEN = "42707c743551486f88aba92fa112b9a3";

	/**
	 *
	 * @param service
	 */
	public DialogFlowAccept(Service service){
		super(service);
	}

	/**
	 * Payload of Service module.
	 * @throws Exception Exception
	 */
	@Override
	public void payload() throws Exception{
		JSONObject response = new ApiAI(API_AI_ACCESS_TOKEN, this.getParam("uid").toString(), this.fulfillment).execute();
		this.setParam("parameters", response.getJSONObject("result").getJSONObject("parameters"));
		this.fulfillment = response.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
	}

	/**
	 * Request processing from next Service module.
	 * @return Service state
	 * @throws Exception Exception
	 */
	@Override
	public Service chain() throws Exception{
		return new Accept(this).resolve().get();
	}

}
