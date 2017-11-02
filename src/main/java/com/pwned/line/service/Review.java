package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;

import javax.websocket.RemoteEndpoint;
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
		try {
			// add
			System.out.println(apiParam.toString());
			apiParam.getString("ReviewAdd");
			BasicDBObject SELF = new BasicDBObject("uid", this.getParam("uid").toString());
			System.out.println(SELF);
			//BasicDBObject cmd = new BasicDBObject("cmd", "review::add");
			//BasicDBObject data = new BasicDBObject("data", new BasicDBObject("data",  new BasicDBObject().append("department", department).append("code", courseCode)));
			//BasicDBObject set1 = new BasicDBObject("$set", new BasicDBObject("buff", new BasicDBObject("cmd", cmd)));
			//BasicDBObject set2 = new BasicDBObject("$set", new BasicDBObject("buff", new BasicDBObject("data", data)));
			mongo.getCollection("user").updateOne(SELF, new BasicDBObject("$set", new BasicDBObject("buff", "review::add")));
			//mongo.getCollection("user").updateOne(SELF, set2);
			//mongo.getCollection("user").updateOne(SELF, new BasicDBObject("$set", new BasicDBObject("buff", new BasicDBObject().append("cmd", "review::add").append("data", new BasicDBObject().append("department", department).append("code", courseCode)))));
			this.fulfillment = "You can type your detail review here: ";

		} catch (Exception e) {
			//no add
			this.fulfillment = "";
			ArrayList<Document> courseReview = MongoDB.get(mongo.getCollection("courseReview").find());
			for (int i = 0; i < courseReview.size(); i++) {
				JSONObject course = new JSONObject(courseReview.get(i).toJson());
				String dep = course.getString("department");
				String code = course.getString("code");
				if (department.equals(dep) && courseCode.equals(code)) {
					if (course.getJSONArray("reviews").length() > 0) {
						JSONObject review = course.getJSONArray("reviews").getJSONObject((int) (Math.random() * course.getJSONArray("reviews").length()));
						String text = review.getString("text");
						this.fulfillment = "Here is a random review of " + department + courseCode + ":\n" +
								"Opinion: " + text;
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
