package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.pwned.line.web.MongoDB;
import org.bson.Document;

import java.util.ArrayList;

public class AnonymousChat extends DefaultService {

	public AnonymousChat(Service service) {
		super(service);
	}

	@Override
	public void payload() throws Exception {
		MongoDB mongo = new MongoDB("mongodb://user:password@ds115045.mlab.com:15045/heroku_0s8hc3hf", "heroku_0s8hc3hf");

		BasicDBObject projection = new BasicDBObject();
		projection.append("uid", 1);
		BasicDBObject constraint = new BasicDBObject();
		constraint.append("uid", new BasicDBObject("$ne", this.getParam("uid")));
		MongoCursor<Document> cursor = mongo.getCollection("user").find(constraint).projection(projection).iterator();

		ArrayList<String> uid = new ArrayList<>();
		while (cursor.hasNext()) {
			uid.add(cursor.next().get("uid").toString());
		}

		if (uid.size() != 0) {
			int index = (int) (Math.random() * uid.size());
			this.fulfillment = uid.get(index);
		}
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

}
