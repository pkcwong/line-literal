package com.pwned.line.service;

import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.entity.Course;
import com.pwned.line.web.ApiAI;
import org.json.JSONObject;

public class CourseQuota extends DefaultService{

	public CourseQuota(Service service){ super(service); }

	private static final String API_AI_ACCESS_TOKEN = "bd33003adb6c4a5d8da4e278eaee8ee6";

	@Override
	public void payload() throws Exception {
		JSONObject json = new ApiAI(API_AI_ACCESS_TOKEN, this.getParam("uid").toString(), this.fulfillment).execute();
		this.fulfillment = json.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
		this.setParam("parameters", json.getJSONObject("result").getJSONObject("parameters"));
		JSONObject apiParam = new JSONObject(this.getParam("parameters").toString());
		String department = apiParam.getString("department");
		String code = apiParam.getString("number");
		Course course = new Course(department, code);
		course.query();
		KitchenSinkController.push(this.getParam("uid").toString(), new TextMessage("Department: " + course.department + "\nCode: " + course.code  + "\nTitle: " + course.title +
				"\nCredit: " + course.credit));
		for(int i = 0; i < course.sections.size(); i++){
			KitchenSinkController.push(this.getParam("uid").toString(), new TextMessage("Section name: " + course.sections.get(i).name + "\nSection Code: " + course.sections.get(i).code + "\nQuota: " + course.sections.get(i).quota +
					"\nEnrol: " + course.sections.get(i).enrol +"\nAvail: " + course.sections.get(i).avail + "\nWait: " + course.sections.get(i).wait));
		}
		this.fulfillment = "";
		if(course.department == null){
			this.fulfillment = "Sorry " + department + code + " could not be found in Course Quota Page";
		}
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}
}
