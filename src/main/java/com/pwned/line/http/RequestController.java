package com.pwned.line.http;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pwned.line.interpreter.ApiAI;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RequestController {

	@RequestMapping(value = {"/get"}, method = {RequestMethod.GET})
	@ResponseBody
	public String getHandler(HttpServletRequest request) {
		JSONObject json = new JSONObject(request.getParameterMap());
		System.out.println(json.toString());
		return json.toString();
	}

	@RequestMapping(value = {"/post"}, method = {RequestMethod.POST})
	@ResponseBody
	public String postHandler(@RequestBody String request) {
		try {
			JSONObject json = new JSONObject(request);
			System.out.println(json.toString());
			return json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
