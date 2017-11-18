package com.pwned.line.service;

import com.pwned.line.http.HTTP;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * LiftAdvisor API
 * Required params: [parameters]
 * Reserved tokens: [@LiftAdvisor]
 * Resolved params: []
 * @author Christopher Wong
 */
public class EditTimeslot extends DefaultService {

	public EditTimeslot(Service service) {
		super(service);
	}

	/**
	 * Sends a request to LiftAdvisor php server.
	 * @throws Exception Exception
	 */
	@Override
	public void payload() throws Exception {

	}

	@Override
	public Service chain() throws Exception {
		return this;
	}



}
