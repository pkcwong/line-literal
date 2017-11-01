package com.pwned.line.service;

import com.pwned.line.http.HTTP;
import com.pwned.line.web.ApiAI;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Service for course information.
 * Required params: [uid]
 * Reserved tokens: [@course::title]
 * Resolved params: []
 * @author Christopher Wong, Calvin Ku
 */
public class CourseName extends DefaultService {

	private static final String API_AI_ACCESS_TOKEN = "bd33003adb6c4a5d8da4e278eaee8ee6";
	private static final String QUOTA_URL = "https://w5.ab.ust.hk/wcq/cgi-bin/1710/subject/";

	public CourseName(Service service) {
		super(service);
	}

	/***
	 * DialogFlow payload
	 */
	@Override
	public void payload() throws Exception {
		JSONObject json = new ApiAI(API_AI_ACCESS_TOKEN, this.getParam("uid").toString(), this.fulfillment).execute();
		this.fulfillment = json.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
		this.setParam("parameters", json.getJSONObject("result").getJSONObject("parameters"));
		JSONObject apiParam = new JSONObject(this.getParam("parameters").toString());
		String department = apiParam.getString("department");
		HTTP httpClient = new HTTP(QUOTA_URL + department);
		this.fulfillment = getCourseName(httpClient.get());
	}

	/***
	 * Returns the name of the course.
	 * @param httpResponse http response
	 * @return Course name
	 */
	private String getCourseName(String httpResponse) throws Exception {
		JSONObject apiParam = new JSONObject(this.getParam("parameters").toString());
		String department = apiParam.getString("department");
		String courseCode = apiParam.getString("number");
		String regex = "<h2>" + department + "\\s" + courseCode + "\\s-\\s(.+?)\\s\\(\\d\\sunits\\)</h2>";
		String courseName = "";
		Pattern departmentPattern = Pattern.compile(regex);
		System.out.println(regex+departmentPattern);
		Matcher courseMatcher = departmentPattern.matcher(httpResponse);
		while (courseMatcher.find()) {
			courseName = courseMatcher.group(1);
		}
		return this.fulfillment.replace("@course::title", courseName);
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

}
