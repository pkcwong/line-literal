package com.pwned.line.service;

import com.pwned.line.http.HTTP;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Service for sending requests to HKUST QUOTA WEBSITE.
 * Required params: [DEPARTMENT, COURSE_CODE]
 * @author Calvin Ku, Christopher Wong
 */
public class CourseQuota implements Service {

	private static final String QUOTA_URL = "https://w5.ab.ust.hk/wcq/cgi-bin/1710/subject/";

	private String fulfillment = null;
	private Map<String, Object> args = null;

	public CourseQuota(String query) {
		this.fulfillment = query;
		this.args = new HashMap<>();
	}

	public CourseQuota(String query, Map<String, Object> args) {
		this.fulfillment = query;
		this.args = args;
	}

	public CourseQuota(Service service) {
		this.fulfillment = service.getFulfillment();
		this.args = service.getArgs();
	}

	/***
	 * Fetch the course information from HKUST Course Quota Page.
	 * @return instance
	 */
	@Override
	public CompletableFuture<Service> resolve() {
		String department = this.getParam("DEPARTMENT").toString();
		HTTP httpClient = new HTTP(QUOTA_URL + department);
		this.fulfillment = getCourseName(httpClient.get());
		return CompletableFuture.supplyAsync(() -> this);
	}

	/***
	 * Returns the name of the course.
	 * @param httpResponse http response
	 * @return Course name
	 */
	private String getCourseName(String httpResponse) {
		String department = this.getParam("DEPARTMENT").toString();
		String courseCode = this.getParam("COURSE_CODE").toString();
		String regex = "<h2>" + department + "\\s" + courseCode + "\\s-\\s(.+?)\\s\\(\\d\\sunits\\)</h2>";
		String courseName = "";
		Pattern departmentPattern = Pattern.compile(regex);
		Matcher courseMatcher = departmentPattern.matcher(httpResponse);
		while (courseMatcher.find()) {
			System.out.println(regex);
			courseName = courseMatcher.group(1);
		}
		return this.fulfillment.replace("@data", courseName);
	}

	@Override
	public void setParam(String key, Object value) {
		this.args.put(key, value);
	}

	@Override
	public Object getParam(String key) {
		return this.args.get(key);
	}

	@Override
	public String getFulfillment() {
		return this.fulfillment;
	}

	@Override
	public Map<String, Object> getArgs() {
		return this.args;
	}

}
