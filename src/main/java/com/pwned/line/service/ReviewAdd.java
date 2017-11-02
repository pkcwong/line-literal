package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.pwned.line.web.MongoDB;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdd extends DefaultService {

	public ReviewAdd(Service service) {
		super(service);
	}

	@Override
	public void payload() throws Exception {
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		BasicDBObject SELF = new BasicDBObject().append("buff", this.getParam("data").toString());
		BasicDBObject department = new BasicDBObject().append("department", SELF);
		BasicDBObject courseCode = new BasicDBObject().append("code", SELF);
		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<>();
		obj.add(new BasicDBObject("department", department);
		obj.add(new BasicDBObject("code", courseCode);
		andQuery.put("$and", obj);
		mongo.getCollection("courseReview").findOneAndUpdate(andQuery, new BasicDBObject("$addToSet", this.fulfillment));
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

}
