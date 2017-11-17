package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;

/***
 * Adding review to MongoDB.
 * Required params: []
 * Reserved tokens: []
 * Resolved params: []
 * @author Bear
 */

public class EventAdd extends DefaultService {
	private String keyword;

	public EventAdd(Service service, String s) {
		super(service);
		keyword = s;
	}

	@Override
	public void payload() throws Exception {
		String[] keywordArray = keyword.split("@");
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));

		//fetch buff -> data from MongoDB
		BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
		ArrayList<Document> user = MongoDB.get(mongo.getCollection("user").find(SELF));
		JSONObject USER = new JSONObject(user.get(0).toJson());
		String groupId = USER.getJSONObject("buff").getJSONObject("data").getString("groupId");


		mongo.getCollection("Event").findOneAndUpdate
				(new BasicDBObject().append("groupId", groupId), new BasicDBObject("$addToSet", new BasicDBObject("Name", keywordArray[0]).append("Time",keywordArray[1])), new FindOneAndUpdateOptions().upsert(true));
		Document data = new Document();
		data.append("uid", this.getParam("uid").toString());
		data.append("bind", this.getParam("uid").toString());
		data.append("buff", new BasicDBObject("cmd", "master"));
		mongo.getCollection("user").findOneAndUpdate(SELF, new BasicDBObject("$set", data));
		this.fulfillment = "Your event had been added";
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

}
