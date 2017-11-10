package com.pwned.line.entity;

import com.pwned.line.http.HTTP;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Course Entity
 * @author Christopher Wong, Calvin Ku
 */
public class Course {

	private static final String BASE_URL = "https://w5.ab.ust.hk/wcq/cgi-bin/1710/subject/";
	public static final String REGEX_COURSE = "<div class=\\\"course\\\">[\\s\\S]+?<tr><th>DESCRIPTION<\\/th><td>(.+?)<\\/td><\\/tr>[\\s\\S]+?<h2>(....)\\s(....)\\s-\\s(.+?)\\s\\((\\d)\\sunits\\)<\\/h2>\\s<table class=\\\"sections\\\" width=\\\"1012\\\">\\s([\\s\\S]+?)<\\/td><\\/tr><\\/table>\\s<\\/div>";
	public static final String REGEX_INFO = "";

	public String introduction;
	public String department;
	public String code;
	public String title;
	public String credit;

	public Course(String department, String code) {
		this.department = department.toUpperCase();
		this.code = code;
	}

	public void query() {
		String dom_timetable;
		HTTP http = new HTTP(BASE_URL + this.department);
		String response = http.get();
		Pattern regex_course = Pattern.compile(this.builder());
		Matcher matcher_course = regex_course.matcher(response);
		while (matcher_course.find()) {
			this.introduction = matcher_course.group(1);
			this.title = matcher_course.group(4);
			this.credit = matcher_course.group(5);
			dom_timetable = matcher_course.group(6);
		}

	}

	public String builder() {
		return REGEX_COURSE.replace("@department", this.department).replace("@code", this.code);
	}

}
