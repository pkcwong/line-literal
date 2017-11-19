package com.pwned.line.service;

import com.pwned.line.web.ApiAI;
import org.json.JSONObject;

/***
 * Service module for DialogFlow translation
 * Required params: [uid]
 * Reserved tokens: [@translate]
 * Resolved params: [parameters]
 * @author Christopher Wong, Calvin Ku
 */
public class DialogFlowTranslate extends DefaultService {

	private static String API_AI_ACCESS_TOKEN = "6ad9c104380b49d6803939577310156a";

	/***
	 * Constructor
	 * @param service instance
	 */
	public DialogFlowTranslate(Service service) {
		super(service);
	}

	/***
	 * Sends user speech to DialogFlow
	 * @throws Exception Exception
	 */
	@Override
	public void payload() throws Exception {
		JSONObject response = new ApiAI(API_AI_ACCESS_TOKEN, this.getParam("uid").toString(), this.fulfillment).execute();
		this.setParam("parameters", response.getJSONObject("result").getJSONObject("parameters"));
		this.fulfillment = response.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
	}

	/***
	 * Pipes output to Translate engine
	 * @return Instance
	 * @throws Exception Exception
	 */
	@Override
	public Service chain() throws Exception {
		return new Translate(this).resolve().get();
	}

}
