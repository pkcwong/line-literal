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
		String[] date = keywordArray[1].split("/");
		int year = Integer.parseInt(date[0]);
		int month = Integer.parseInt(date[1]);
		int day = Integer.parseInt(date[2]);

		if(keywordArray.length !=2){
			this.fulfillment = "Please follow that format {EventName}@yyyy/mm/dd";
			return;
		}else if (year > 2018 || year < 2017){
			this.fulfillment = "Invalid year";
			return;
		}else if(month < 1 || month > 12){
			this.fulfillment = "Invalid month";
			return;
		}else if (day < 1 || day > 30){
			this.fulfillment = "Invalid day";
			return;
		}
		
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));

		//fetch buff -> data from MongoDB
		BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
		ArrayList<Document> user = MongoDB.get(mongo.getCollection("user").find(SELF));
		JSONObject USER = new JSONObject(user.get(0).toJson());
		String groupId = USER.getJSONObject("buff").getJSONObject("data").getString("groupId");


		mongo.getCollection("Event").findOneAndUpdate(new BasicDBObject().append("groupId", groupId),
				new BasicDBObject("$set",
						new BasicDBObject("event",
								new BasicDBObject().append("Name", keywordArray[0]).append("Date", keywordArray[1]))));

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
