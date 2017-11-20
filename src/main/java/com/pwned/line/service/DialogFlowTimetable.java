package com.pwned.line.service;

import com.pwned.line.web.ApiAI;
import org.json.JSONObject;
/***
 * Service for Timetable information.
 * Required params: [parameters]
 * Reserved tokens: []
 * Resolved params: []
 * @author Eric Kwan
 */
public class DialogFlowTimetable extends DefaultService{
	/**
	 * Constructor
	 * @param service
	 */
	public DialogFlowTimetable(Service service) { super(service); }

	private static final String API_AI_ACCESS_TOKEN = "bd33003adb6c4a5d8da4e278eaee8ee6";

	/**
	 * Payload of Service module.
	 * @throws Exception Exception
	 */
	@Override
	public void payload() throws Exception {
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
	public Service chain() throws Exception {
		return new TimeTable(this).resolve().get();
	}

}
