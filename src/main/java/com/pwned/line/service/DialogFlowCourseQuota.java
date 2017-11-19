package com.pwned.line.service;

import com.pwned.line.web.ApiAI;
import org.json.JSONObject;

/***
 * Service module for DialogFlow for Course Quota
 * Required params: [uid]
 * Reserved tokens: [@department, @number]
 * Resolved params: [parameters]
 * @author Calvin Ku
 */

public class DialogFlowCourseQuota extends DefaultService{

	/**
	 * Constructor for DialogFlowCourseQuota
	 * @param service
	 */
	public DialogFlowCourseQuota(Service service) { super(service);}

	private static final String API_AI_ACCESS_TOKEN = "bd33003adb6c4a5d8da4e278eaee8ee6";

	/***
	 * Payload for DialogueFlow Course Quota
	 * @throws Exception
	 */
	@Override
	public void payload() throws Exception{
		JSONObject json = new ApiAI(API_AI_ACCESS_TOKEN, this.getParam("uid").toString(), this.fulfillment).execute();
		this.fulfillment = json.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
		this.setParam("parameters", json.getJSONObject("result").getJSONObject("parameters"));
	}

	/***
	 * Chain for DialogueFlow Course Quota
	 * @return Service state
	 * @throws Exception
	 */
	@Override
	public Service chain() throws Exception{
		return new CourseQuota(this).resolve().get();
	}

}
