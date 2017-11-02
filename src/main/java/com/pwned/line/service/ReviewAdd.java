package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdd extends DefaultService {

	public ReviewAdd(Service service) {
		super(service);
	}

	@Override
	public void payload() throws Exception {
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));

		//fetch buff -> data from MongoDB
		BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
		ArrayList<Document> user = MongoDB.get(mongo.getCollection("user").find(SELF));
		JSONObject USER = new JSONObject(user.get(0).toJson());
		String department = USER.getJSONObject("buff").getJSONObject("data").getString("department");
		String code = USER.getJSONObject("buff").getJSONObject("data").getString("code");

		// build query
		BasicDBObject dept = new BasicDBObject().append("department", department);
		BasicDBObject courseCode = new BasicDBObject().append("code", code);
		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<>();
		obj.add(new BasicDBObject("code", courseCode));
		obj.add(new BasicDBObject("department", dept));
		andQuery.put("$and", obj);
		mongo.getCollection("courseReview").findOneAndUpdate(andQuery, new BasicDBObject("$addToSet", new BasicDBObject("reviews", this.fulfillment)), new FindOneAndUpdateOptions().upsert(true));
		Document data = new Document();
		data.append("uid", this.getParam("uid").toString());
		data.append("bind", this.getParam("uid").toString());
		data.append("buff", new BasicDBObject("cmd", "master"));
		mongo.getCollection("user").findOneAndUpdate(SELF, new BasicDBObject("$addToSet", data));
		this.fulfillment = "Your course review had been added";
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

}
