package com.pwned.line.service;

import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;

public class Review extends DefaultService {

	public Review(Service service) {
		super(service);
	}

	@Override
	public void payload() throws Exception {
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		JSONObject apiParam = new JSONObject(this.getParam("parameters").toString());
		String department = apiParam.getString("department");
		String courseCode = apiParam.getString("number");
		if(this.fulfillment.contains("add")){

		}
		else {
			this.fulfillment = "";
			ArrayList<Document> courseReview = MongoDB.get(mongo.getCollection("courseReview").find());
			for(int i = 0; i < courseReview.size(); i++){
				JSONObject course = new JSONObject(courseReview.get(i).toJson());
				String dep = course.getString("department");
				String code = course.getString("code");
				if (department.equals(dep) && courseCode.equals(code)) {
					if (course.getJSONArray("reviews").length() > 0) {
						JSONObject review = course.getJSONArray("reviews").getJSONObject((int) (Math.random() * course.getJSONArray("reviews").length()));
						String text = review.getString("text");
						String impression = review.getString("impression");
						this.fulfillment = "Here is a random review of " + department + courseCode + ":\n" +
								"Opinion: " + text + "\nImpression(A-F without sub grade): " + impression;
					}
				}
			}
			if (this.fulfillment.equals("")) {
				this.fulfillment = "Sorry, no reviews yet. Help us make one! :D :D";
			}
		}
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

}
