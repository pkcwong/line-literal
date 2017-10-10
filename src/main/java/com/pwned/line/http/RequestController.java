package com.pwned.line.http;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

public class RequestController {

	@RequestMapping(value = {"/"}, method = {RequestMethod.GET})
	@ResponseBody
	public String postRequest(HttpServletRequest request) {
		JSONObject json = new JSONObject(request.getParameterMap());
		return json.toString();
	}

}
