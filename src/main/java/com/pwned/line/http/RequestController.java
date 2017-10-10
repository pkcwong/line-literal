package com.pwned.line.http;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RequestController {

	@RequestMapping(value = {"/"}, method = {RequestMethod.GET})
	@ResponseBody
	public String getRequest(HttpServletRequest request) {
		JSONObject json = new JSONObject(request.getParameterMap());
		return json.toString();
	}

}
