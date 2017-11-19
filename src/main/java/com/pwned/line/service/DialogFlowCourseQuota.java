package com.pwned.line.service;

import com.pwned.line.web.ApiAI;
import org.json.JSONObject;

public class DialogFlowCourseQuota extends DefaultService{
	public DialogFlowCourseQuota(Service service) { super(service);}

	private static final String API_AI_ACCESS_TOKEN = "bd33003adb6c4a5d8da4e278eaee8ee6";

	@Override
	public void payload() throws Exception{
		JSONObject json = new ApiAI(API_AI_ACCESS_TOKEN, this.getParam("uid").toString(), this.fulfillment).execute();
		this.fulfillment = json.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
		this.setParam("parameters", json.getJSONObject("result").getJSONObject("parameters"));
	}

	@Override
	public Service chain() throws Exception{
		return new CourseQuota(this).resolve().get();
	}

}
