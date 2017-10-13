package com.pwned.line.service;

import com.pwned.line.http.HTTP;

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
		HTTP httpClient = new HTTP(QUOTA_URL + department);
		System.out.println(httpClient.get());
		
		return super.fulfillment;
	}
}

