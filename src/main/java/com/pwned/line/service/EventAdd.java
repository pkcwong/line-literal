package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/***
 * Adding review to MongoDB.
 * Required params: []
 * Reserved tokens: []
 * Resolved params: []
 * @author Bear
 */

public class EventAdd extends DefaultService {
	private Map<String, String> args;

	public EventAdd(Service service, String s) {
		super(service);
		String[] keywordArray = s.split("@");
		if(keywordArray.length == 2)
			args.put(keywordArray[0],keywordArray[1]);
		this.fulfillment = "Please follow the format {EventName}@yyyy/mm/dd";
		return;
	}

	@Override
	public void payload() throws Exception {

		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));

		//fetch buff -> data from MongoDB
		BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
		ArrayList<Document> user = MongoDB.get(mongo.getCollection("user").find(SELF));
		JSONObject USER = new JSONObject(user.get(0).toJson());
		String groupId = USER.getJSONObject("buff").getJSONObject("data").getString("groupId");


		mongo.getCollection("Event").findOneAndUpdate(new BasicDBObject().append("groupId", groupId), new BasicDBObject("$addToSet", new BasicDBObject("data", args)), new FindOneAndUpdateOptions().upsert(true));
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
