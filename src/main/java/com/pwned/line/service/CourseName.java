package com.pwned.line.service;

import com.pwned.line.http.HTTP;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Service for sending requests to HKUST QUOTA WEBSITE.
 * Required params: [DEPARTMENT, COURSE_CODE]
 * @author Calvin Ku, Christopher Wong
 */
public class CourseName extends DefaultService {

	private static final String QUOTA_URL = "https://w5.ab.ust.hk/wcq/cgi-bin/1710/subject/";

	public CourseName(Service service) {
		super(service);
	}

	/***
	 * Fetch the course information from HKUST Course Quota Page.
	 */
	@Override
	public void payload() {
		String department = this.getParam("DEPARTMENT").toString();
		HTTP httpClient = new HTTP(QUOTA_URL + department);
		this.fulfillment = getCourseName(httpClient.get());
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
	public Service chain() throws Exception {
		return this;
	}

}
