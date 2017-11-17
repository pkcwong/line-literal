package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.pwned.line.http.HTTP;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;


import java.util.ArrayList;

/***
 * Service for event maker.
 * Required params: [params]
 * Reserved tokens: []
 * Resolved params: []
 * @author Bear
 */

public class EventMaker extends DefaultService{
	private String URI = "https://api.line.me/v2/bot/group/{groupId}/member/{userId}";
	private static final String ACCESS_TOKEN = System.getenv("LINE_BOT_CHANNEL_TOKEN");
	public EventMaker(Service service){
		super(service);
	}

	@Override
	public void payload() throws Exception{
		String groupId = this.getParam("groupId").toString();
		String uid = this.getParam("uid").toString();

		if(uid.equals(groupId)){
			this.fulfillment = "Sorry, you can only use this function in a group chat";
			return;
		}

		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		BasicDBObject SELF = new BasicDBObject().append("groupId", groupId);
		ArrayList<Document> group = MongoDB.get(mongo.getCollection("Event").find(SELF));
		if(group.size() == 0){
			Document data = new Document();
			data.append("groupId", groupId);
			data.append("uid", uid);
			mongo.getCollection("Event").insertOne(data);
		}
		BasicDBObject eventName = new BasicDBObject().append("eventName", groupId);
		ArrayList<Document> events = MongoDB.get(mongo.getCollection("Event").find(eventName));
		if(events.size() == 0){
			SELF = new BasicDBObject().append("uid", uid);
			mongo.getCollection("user").updateOne(SELF,
					new BasicDBObject("$set",
							new BasicDBObject("buff",
									new BasicDBObject().append("cmd", "event::add").append("data", new BasicDBObject().append("groupid", groupId)))));
			this.fulfillment = "You have not create event yet, please create your event with {Event Name}@yyyy/mm/dd:";
			return;

		}else{
			JSONObject event = new JSONObject(events.get(0).toJson());
			this.fulfillment = event.toString();
		}

		URI = URI.replace("{groupId}", groupId);
		URI = URI.replace("{userId}", uid);
		HTTP http = new HTTP(URI);
		http.setHeaders("Authorization", "Bearer " + ACCESS_TOKEN);
		System.out.printf("Result of groupId = %s\n", this.getParam("groupId").toString());
		this.fulfillment = "Please create your event with {Event Name}@yyyy/mm/dd";
	}

	private boolean checkGroupChat(String uid, String gid){
		if(uid.equals(gid)){
			return false;
		}else
			return true;
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}
}
