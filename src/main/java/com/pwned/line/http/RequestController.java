package com.pwned.line.http;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pwned.line.interpreter.ApiAI;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RequestController {

	@RequestMapping(value = {"/"}, method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String getRequest(HttpServletRequest request) {
		JSONObject json = new JSONObject(request.getParameterMap());
		ApiAI.handler(json);
		return json.toString();
	}

}
