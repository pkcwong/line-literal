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
	public static final String REGEX_COURSE = "<div class=\\\"course\\\">[\\s\\S]+?<tr><th>DESCRIPTION<\\/th><td>(.+?)<\\/td><\\/tr>[\\s\\S]+?<h2>(....)\\s(....)\\s-\\s(.+?)\\s\\((\\d)\\sunits\\)<\\/h2>\\s<table class=\\\"sections\\\" width=\\\"1012\\\">\\s([\\s\\S]+?)<\\/td><\\/tr><\\/table>\\s<\\/div>";
	public static final String REGEX_GET_SECTION= "<td align=\"center\".+?\\s?<td>(?:.*) class=(?:.*)>";
	public static final String REGEX_SECTION_NAME_CODE = ".+?>(\\w{1,3}) \\((\\w{1,4})";
	public static final String REGEX_GET_ALL_DATE_TIME_ROOM_PROF = "(Mo|Tu|We|Th|Fr|MoTu|MoWe|MoTh|MoFr|TuWe|TuTh|TuFr|WeTh|WeFr|ThFr|MoTuWe|MoTuTh|MoTuFr|MoWeTh|MoWeFr|MoThFr|TuWeTh|TuWeFr|WeThFr) ([^ ]*) - ([^<]*)<\\/td><td>([^<]*).+?instructor\\/([^\"]*)";
	public static final String REGEX_GET_QUOTA_INFO = "<\\/a>.+?\".+?>(\\w{1,3}).+?\">(\\w{1,3}).+?(\\d{1,3}).+?\">(\\w{1,3})";

	public String introduction;
	public String department;
	public String code;
	public String title;
	public String credit;
	public ArrayList<Section> sections;

	public Course(String department, String code) {
		this.department = department.toUpperCase();
		this.code = code;
	}

	public void query() {
		String section_block = null;
		HTTP http = new HTTP(BASE_URL + this.department);
		String response = http.get();
		//System.out.println(response);
		Pattern regex_course = Pattern.compile(REGEX_COURSE);
		//Pattern regex_course = Pattern.compile(this.builder());
		Matcher matcher_course = regex_course.matcher("<h2>COMP 2012 - Object-Oriented Programming and Data Structures (4 units)</h2>\n" +
				"<table class=\"sections\" width=\"1012\">\n" +
				"<tr><th width=\"85\">Section</th><th width=\"190\" style=\"text-align: left\">Date & Time</th><th width=\"160\" style=\"text-align: left\">Room</th><th width=\"190\" style=\"text-align: left\">Instructor</th><th width=\"45\">Quota</th><th width=\"45\">Enrol</th><th width=\"45\">Avail</th><th width=\"45\">Wait</th><th width=\"81\">Remarks</th></tr>\n" +
				"<tr class=\"newsect secteven\">\n" +
				"<td align=\"center\">L1 (1807)</td>\n" +
				"<td>WeFr 04:30PM - 05:50PM</td><td>Rm 4619, Lift 31-32 (126)</td><td><a href=\"/wcq/cgi-bin/1710/instructor/TSOI, Yau Chat\">TSOI, Yau Chat</a></td><td align=\"center\">120</td><td align=\"center\">116</td><td align=\"center\">4</td><td align=\"center\">0</td><td align=\"center\">&nbsp;</td></tr><tr class=\"newsect sectodd\">\n" +
				"<td align=\"center\">L2 (1809)</td>\n" +
				"<td>TuTh 03:00PM - 04:20PM</td><td>Rm 2465, Lift 25-26 (122)</td><td><a href=\"/wcq/cgi-bin/1710/instructor/QUAN, Long\">QUAN, Long</a></td><td align=\"center\">87</td><td align=\"center\">74</td><td align=\"center\">13</td><td align=\"center\">0</td><td align=\"center\">&nbsp;</td></tr><tr class=\"newsect secteven\">\n" +
				"<td align=\"center\">LA1 (1811)</td>\n" +
				"<td>Th 01:00PM - 02:50PM</td><td>Rm 4210, Lift 19 (67)</td><td><a href=\"/wcq/cgi-bin/1710/instructor/QUAN, Long\">QUAN, Long</a><br><a href=\"/wcq/cgi-bin/1710/instructor/TSOI, Yau Chat\">TSOI, Yau Chat</a></td><td align=\"center\">67</td><td align=\"center\">66</td><td align=\"center\">1</td><td align=\"center\">0</td><td align=\"center\">&nbsp;</td></tr><tr class=\"newsect sectodd\">\n" +
				"<td align=\"center\">LA2 (1813)</td>\n" +
				"<td>Th 10:30AM - 12:20PM</td><td>Rm 4210, Lift 19 (67)</td><td><a href=\"/wcq/cgi-bin/1710/instructor/QUAN, Long\">QUAN, Long</a><br><a href=\"/wcq/cgi-bin/1710/instructor/TSOI, Yau Chat\">TSOI, Yau Chat</a></td><td align=\"center\">65</td><td align=\"center\">61</td><td align=\"center\">4</td><td align=\"center\">0</td><td align=\"center\">&nbsp;</td></tr><tr class=\"newsect secteven\">\n" +
				"<td align=\"center\">LA3 (3878)</td>\n" +
				"<td>Tu 11:30AM - 01:20PM</td><td>Rm 4210, Lift 19 (67)</td><td><a href=\"/wcq/cgi-bin/1710/instructor/QUAN, Long\">QUAN, Long</a><br><a href=\"/wcq/cgi-bin/1710/instructor/TSOI, Yau Chat\">TSOI, Yau Chat</a></td><td align=\"center\">67</td><td align=\"center\">63</td><td align=\"center\">4</td><td align=\"center\">0</td><td align=\"center\">&nbsp;</td></tr></table>\n" +
				"</div>");
		System.out.println(this.builder());
		while (matcher_course.find()){
			this.introduction = matcher_course.group(1);
			this.title = matcher_course.group(4);
			this.credit = matcher_course.group(5);
			section_block = matcher_course.group(6);
		}
		System.out.println(this.introduction);
		System.out.println(this.title);
		System.out.println(this.credit);
		System.out.print(section_block);
		System.out.println("1");
		Pattern regex_section_info = Pattern.compile(REGEX_GET_SECTION);
		Matcher matcher_section = regex_section_info.matcher(section_block);
		ArrayList<String> section_info = new ArrayList<>();
		while(matcher_section.find()){
			section_info.add(matcher_section.group());
		}
		System.out.println("2");
		for(int i = 0; i <section_info.size(); i++){
			Pattern regex_section_name_code = Pattern.compile(REGEX_SECTION_NAME_CODE);
			Matcher matcher_section_name_code = regex_section_name_code.matcher(section_info.get(i));
			String name = null;
			String code = null;
			while(matcher_section_name_code.find()) {
				name = matcher_section_name_code.group(1);
				code = matcher_section_name_code.group(2);
			}
			System.out.println("3");
			Pattern regex_section_data = Pattern.compile(REGEX_GET_ALL_DATE_TIME_ROOM_PROF);
			Matcher matcher_section_data = regex_section_data.matcher(section_info.get(i));
			ArrayList<DateAndTime> dateAndTimes = new ArrayList<>();
			ArrayList<String> rooms = new ArrayList<>();
			ArrayList<String> instructors = new ArrayList<>();
			while(matcher_section_data.find()){
				String day = matcher_section_data.group(1);
				String startTime = matcher_section_data.group(2);
				String endTime = matcher_section_data.group(3);
				DateAndTime dateAndTime = new DateAndTime(day, startTime, endTime);
				dateAndTimes.add(dateAndTime);
				rooms.add(matcher_section_data.group(4));
				instructors.add(matcher_section_data.group(5));
			}
			System.out.println("4");
			Pattern regex_section_quota_info = Pattern.compile(REGEX_GET_QUOTA_INFO);
			Matcher matcher_section_quota_info = regex_section_quota_info.matcher(section_info.get(i));
			String quota = null;
			String enrol = null;
			String avail = null;
			String wait = null;
			while(matcher_section_quota_info.find()){
				quota = matcher_section_quota_info.group(1);
				enrol = matcher_section_quota_info.group(2);
				avail = matcher_section_quota_info.group(3);
				wait = matcher_section_quota_info.group(4);
			}
			System.out.println("5");
			Section section = new Section(name, code, dateAndTimes, rooms, instructors, quota, enrol, avail, wait);
			sections.add(section);
		}
	}
//	public void query() {
//		String class_block;
//		HTTP http = new HTTP(BASE_URL + this.department);
//		String response = http.get();
//		System.out.println(response);
//		Pattern regex_course = Pattern.compile(this.builder());
//		Matcher matcher_course = regex_course.matcher(response);
//		while (matcher_course.find()) {
//			System.out.println("1");
//			this.introduction = matcher_course.group(1);
//			this.title = matcher_course.group(4);
//			this.credit = matcher_course.group(5);
//			class_block = matcher_course.group(6);
//			Pattern regex_section_info = Pattern.compile(REGEX_GET_CLASS);
//			Matcher matcher_section = regex_section_info.matcher(class_block);
//			while(matcher_section.find()){
//				System.out.println("2");
//				String sectionString = matcher_section.group();
//				Pattern regex_section_name_code = Pattern.compile(REGEX_SECTION_NAME_CODE);
//				Matcher matcher_section_name_code = regex_section_name_code.matcher(sectionString);
//				String name = "";
//				String code = "";
//				while(matcher_section_name_code.find()) {
//					System.out.println("3");
//					name= matcher_section_name_code.group(1);
//					code = matcher_section_name_code.group(2);
//				}
//				Pattern regex_section_data = Pattern.compile(REGEX_GET_ALL_DATE_TIME_ROOM_PROF);
//				Matcher matcher_section_data = regex_section_data.matcher(sectionString);
//				ArrayList<DateAndTime> dateAndTimes = new ArrayList<>();
//				ArrayList<String> rooms = new ArrayList<>();
//				ArrayList<String> instructors = new ArrayList<>();
//				while(matcher_section_data.find()){
//					System.out.println("4");
//					String day = matcher_section_data.group(1);
//					String startTime = matcher_section_data.group(2);
//					String endTime = matcher_section_data.group(3);
//					DateAndTime dateAndTime = new DateAndTime(day, startTime, endTime);
//					dateAndTimes.add(dateAndTime);
//					rooms.add(matcher_section_data.group(4));
//					instructors.add(matcher_section_data.group(5));
//				}
//				Pattern regex_section_quota_info = Pattern.compile(REGEX_GET_QUOTA_INFO);
//				Matcher matcher_section_quota_info = regex_section_quota_info.matcher(sectionString);
//				String quota = "";
//				String enrol = "";
//				String avail = "";
//				String wait = "";
//				while(matcher_section_quota_info.find()){
//					System.out.println("5");
//					quota = matcher_section_quota_info.group(1);
//					enrol = matcher_section_quota_info.group(2);
//					avail = matcher_section_quota_info.group(3);
//					wait = matcher_section_quota_info.group(4);
//				}
//				Section section = new Section(name, code, dateAndTimes, rooms, instructors, quota, enrol, avail, wait);
//				sections.add(section);
//			}
//		}
//	}

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
