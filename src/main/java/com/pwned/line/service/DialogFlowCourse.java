package com.pwned.line.service;

import com.pwned.line.web.ApiAI;
import org.json.JSONObject;

/***
 * Service for course information.
 * Required params: [uid]
 * Reserved tokens: []
 * Resolved params: [parameters]
 * @author Christopher Wong, Calvin Ku
 */
public class DialogFlowCourse extends DefaultService {

	private static final String ACCESS_TOKEN = "bd33003adb6c4a5d8da4e278eaee8ee6";

	public DialogFlowCourse(Service service) {
		super(service);
	}

	/***
	 * DialogFlow payload
	 */
	@Override
	public void payload() throws Exception {
		JSONObject json = new ApiAI(ACCESS_TOKEN, this.getParam("uid").toString(), this.fulfillment).execute();
		this.fulfillment = json.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
		this.setParam("parameters", json.getJSONObject("result").getJSONObject("parameters"));
	}

	@Override
	public Service chain() throws Exception {
		return new CourseName(this).resolve().get();
	}

}
