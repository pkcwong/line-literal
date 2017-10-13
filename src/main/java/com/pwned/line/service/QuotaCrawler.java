package com.pwned.line.service;

import com.pwned.line.http.HTTP;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Service for sending requests to HKUST QUOTA WEBSITE.
 *  * Required params: [DEPARTMENT, COURSE_CODE]
 * @author Calvin Ku, Christopher Wong
 */
public class QuotaCrawler extends Service{
	private static final String QUOTA_URL = "https://w5.ab.ust.hk/wcq/cgi-bin/1710/subject/";

	public QuotaCrawler(String query){
		super (query);
	}

	/***
	 * Fetch the course information from HKUST's Quota Page.
	 * @return
	 */
	@Override
	public String resolve() {
		String department = super.getArgs("DEPARTMENT").toString();
		String courseCode = super.getArgs("COURSE_CODE").toString();
		HTTP httpClient = new HTTP(QUOTA_URL + department);
		super.fulfillment = getCourseName(httpClient.get());
		return super.fulfillment;
	}

	/***
	 * Returns the name of the course.
	 * @param httpResponse http response
	 * @return Course name
	 */
	private String getCourseName(String httpResponse) {
		String department = super.getArgs("DEPARTMENT").toString();
		String courseCode = super.getArgs("COURSE_CODE").toString();
		String regex = "<h2>" + department + "\\s" + courseCode + "\\s-\\s(.+?)\\s\\(\\d\\sunits\\)</h2>";
		String courseName = "";
		Pattern departmentPattern = Pattern.compile(regex);
		Matcher courseMatcher = departmentPattern.matcher(httpResponse);
		while(courseMatcher.find()){
			System.out.println(regex);
			courseName = courseMatcher.group(1);
		}
		return super.fulfillment.replace("@data", courseName);
	}

}

