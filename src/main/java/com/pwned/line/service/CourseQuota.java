package com.pwned.line.service;

import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.entity.Course;
import com.pwned.line.web.ApiAI;
import org.json.JSONObject;

public class CourseQuota extends DefaultService{
	/***
	 * Review, looks up course review in MongoDB.
	 * Required params: [uid, parameters]
	 * Reserved tokens: []
	 * Resolved params: [department, code, reviews]
	 * @author Calvin Ku
	 */
	
	public CourseQuota(Service service){ super(service); }

	@Override
	public void payload() throws Exception {
		JSONObject apiParam = new JSONObject(this.getParam("parameters").toString());
		String department = apiParam.getString("department");
		String code = apiParam.getString("number");
		Course course = new Course(department, code);
		course.query();
		pushCourseMsg(course, department, code);
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

	public void pushCourseMsg(Course course, String department, String code){
		if(course.title == null){
			this.fulfillment = "Sorry " + department + code + " could not be found in Course Quota Page";
		}else{
			KitchenSinkController.push(this.getParam("uid").toString(), new TextMessage("Department: " + course.department + "\nCode: " + course.code  + "\nTitle: " + course.title +
					"\nCredit: " + course.credit));
			for(int i = 0; i < course.sections.size(); i++){
				KitchenSinkController.push(this.getParam("uid").toString(), new TextMessage("Section name: " + course.sections.get(i).name + "\nSection Code: " + course.sections.get(i).code + "\nQuota: " + course.sections.get(i).quota +
						"\nEnrol: " + course.sections.get(i).enrol +"\nAvail: " + course.sections.get(i).avail + "\nWait: " + course.sections.get(i).wait));
			}
			this.fulfillment = "";
		}


	}
}
