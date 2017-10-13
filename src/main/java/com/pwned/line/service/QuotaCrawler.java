package com.pwned.line.service;

import com.pwned.line.http.HTTP;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Service for sending requests to HKUST QUOTA WEBSITE.
 *  * Required params: [DEPARTMENT, COURSE_CODE]
 * @author Christopher Wong, Calvin Ku
 */
public class QuotaCrawler extends Service{
	private static final String QUOTA_URL = "https://w5.ab.ust.hk/wcq/cgi-bin/1710/subject/";

	public QuotaCrawler(String query){ super (query); }
// The course pre-requisite for @courseid is
	@Override
	public String resolve() {
		String department = super.getArgs("DEPARTMENT").toString();
		String courseCode = super.getArgs("COURSE_CODE").toString();
		HTTP httpClient = new HTTP(QUOTA_URL + department);
		Pattern departmentPattern = Pattern.compile("<h2>" + department + " " + courseCode + ".+</h2>");
		Matcher courseMatcher = departmentPattern.matcher(httpClient.get());
		courseMatcher.find();
		super.fulfillment = courseMatcher.group();
		return super.fulfillment;
	}
}

