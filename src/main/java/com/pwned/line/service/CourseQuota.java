package com.pwned.line.service;

import com.pwned.line.entity.Course;

public class CourseQuota extends DefaultService{

	public CourseQuota(Service service){ super(service); }

	private static final String API_AI_ACCESS_TOKEN = "bd33003adb6c4a5d8da4e278eaee8ee6";

	@Override
	public void payload() throws Exception {
		Course course = new Course("COMP", "2012");
		course.query();
		System.out.println("Department: " + course.department + "\nCode: " + course.code  + "\nTitle: " + course.title +
		"\nCredit: " + course.credit);
		for(int i = 0; i < course.sections.size(); i++){
			System.out.println("Section name: " + course.sections.get(i).name + "\nSection Code: " + course.sections.get(i).code + "\nQuota: " + course.sections.get(i).quota +
			"\nEnrol: " + course.sections.get(i).enrol +"\nAvail: " + course.sections.get(i).avail + "\nWait: " + course.sections.get(i).wait);
			for(int j = 0; j < course.sections.get(i).dateAndTimes.size(); j++){
				System.out.println("Day: " + course.sections.get(i).dateAndTimes.get(j).day + "\nStart Time: " + course.sections.get(i).dateAndTimes.get(j).startTime +
				"\nEnd Time: " + course.sections.get(i).dateAndTimes.get(j).endTime);
			}
			System.out.println();
		}
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}
}
