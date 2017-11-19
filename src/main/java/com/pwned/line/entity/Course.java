package com.pwned.line.entity;

import com.pwned.line.http.HTTP;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Course Entity
 * @author Calvin Ku, Christopher Wong
 */
public class Course {

	private static final String BASE_URL = "https://w5.ab.ust.hk/wcq/cgi-bin/1710/subject/";
	public static final String REGEX_COURSE = "<h2>(@department)\\s(@code)\\s- (.+?)\\s\\((\\d)\\sunits\\)<\\/h2>.+?1012\">([\\s\\S]+?)<\\/td><\\/tr><\\/table>";
	public static final String REGEX_GET_SECTION = "\\w{1,2}\\d{1,2} \\(\\d{4}\\).+?(?=&nbsp)";
	public static final String REGEX_SECTION_NAME_CODE = "(\\w{1,2}\\d{1,2}) \\((\\d{4})\\)";
	public static final String REGEX_GET_ALL_DATE_TIME_ROOM_PROF = "(Mo|Tu|We|Th|Fr|MoTu|MoWe|MoTh|MoFr|TuWe|TuTh|TuFr|WeTh|WeFr|ThFr|MoTuWe|MoTuTh|MoTuFr|MoWeTh|MoWeFr|MoThFr|TuWeTh|TuWeFr|WeThFr) ([^ ]*) - ([^<]*)<\\/td><td>([^<]*).+?instructor\\/([^\"]*)";
	public static final String REGEX_GET_QUOTA_INFO = "(<td>TBA<\\/td><td align=\"center\">|<\\/a>.+?\".+?>)(\\d{1,3}).+?\">(\\d{1,3}).+?(\\d{1,3}).+?\">(\\d{1,3})";

	public String department;
	public String code;
	public String title;
	public String credit;
	public ArrayList<Section> sections = new ArrayList<>();

	public Course(String department, String code) {
		this.department = department.toUpperCase();
		this.code = code;
	}

	public void query() {
		String section_block = null;
		HTTP http = new HTTP(BASE_URL + this.department);
		String response = http.get();
		Pattern regex_course = Pattern.compile(this.builder());
		Matcher matcher_course = regex_course.matcher(response);
		while (matcher_course.find()){
			this.title = matcher_course.group(3);
			this.credit = matcher_course.group(4);
			section_block = matcher_course.group(5);
		}
		//System.out.println(section_block);
		Pattern regex_section_info = Pattern.compile(REGEX_GET_SECTION);
		Matcher matcher_section = regex_section_info.matcher(section_block);
		ArrayList<String> section_info = new ArrayList<>();
		while(matcher_section.find()){
			section_info.add(matcher_section.group());
		}
		for(int i = 0; i <section_info.size(); i++){
			Pattern regex_section_name_code = Pattern.compile(REGEX_SECTION_NAME_CODE);
			Matcher matcher_section_name_code = regex_section_name_code.matcher(section_info.get(i));
			String name = null;
			String code = null;
			while(matcher_section_name_code.find()) {
				name = matcher_section_name_code.group(1);
				code = matcher_section_name_code.group(2);
			}
			Pattern regex_section_data = Pattern.compile(REGEX_GET_ALL_DATE_TIME_ROOM_PROF);
			Matcher matcher_section_data = regex_section_data.matcher(section_info.get(i));
			ArrayList<DateAndTime> dateAndTimes = new ArrayList<>();
			ArrayList<String> rooms = new ArrayList<>();
			ArrayList<String> instructors = new ArrayList<>();
			String day = null;
			String startTime = null;
			String endTime = null;
			while(matcher_section_data.find()){
				day = matcher_section_data.group(1);
				startTime = matcher_section_data.group(2);
				endTime = matcher_section_data.group(3);
				DateAndTime dateAndTime = new DateAndTime(day, startTime, endTime);
				dateAndTimes.add(dateAndTime);
				rooms.add(matcher_section_data.group(4));
				instructors.add(matcher_section_data.group(5));
			}
			if(day == null && startTime == null && endTime == null){
				DateAndTime dateAndTime = new DateAndTime("TBA", "TBA", "TBA");
				dateAndTimes.add(dateAndTime);
			}
			Pattern regex_section_quota_info = Pattern.compile(REGEX_GET_QUOTA_INFO);
			Matcher matcher_section_quota_info = regex_section_quota_info.matcher(section_info.get(i));
			String quota = null;
			String enrol = null;
			String avail = null;
			String wait = null;
			while(matcher_section_quota_info.find()){
				quota = matcher_section_quota_info.group(2);
				enrol = matcher_section_quota_info.group(3);
				avail = matcher_section_quota_info.group(4);
				wait = matcher_section_quota_info.group(5);
			}
			Section section = new Section(name, code, dateAndTimes, rooms, instructors, quota, enrol, avail, wait);
			sections.add(section);
		}
	}

	public String builder() {
		return REGEX_COURSE.replace("@department", this.department).replace("@code", this.code);
	}

	public class Section {
		public String name;
		public String code;
		public ArrayList<DateAndTime> dateAndTimes = new ArrayList<>();
		public ArrayList<String> rooms = new ArrayList<>();
		public ArrayList<String> instructors = new ArrayList<>();
		public String quota;
		public String enrol;
		public String avail;
		public String wait;

		public Section(String name, String code, ArrayList<DateAndTime> dateAndTimes, ArrayList<String> rooms, ArrayList<String> instructor, String quota, String enrol, String avail, String wait){
			this.name = name;
			this.code = code;
			this.dateAndTimes = dateAndTimes;
			this.rooms = rooms;
			this.instructors = instructor;
			this.quota = quota;
			this.enrol = enrol;
			this.avail = avail;
			this.wait = wait;
		}
	}

	public class DateAndTime{
		public String day;
		public String startTime;
		public String endTime;
		public DateAndTime(String day, String startTime, String endTime){
			this.day = day;
			this.startTime = startTime;
			this.endTime = endTime;
		}
	}
}
