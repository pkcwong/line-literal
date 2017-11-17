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
	private String keyword;
	private String URI = "https://api.line.me/v2/bot/group/{groupId}/member/{userId}";
	private static final String ACCESS_TOKEN = System.getenv("LINE_BOT_CHANNEL_TOKEN");
	public EventMaker(Service service, String s){
		super(service);
		keyword = s;

	}

	@Override
	public void payload() throws Exception{
		String groupId = this.getParam("groupId").toString();
		String uid = this.getParam("uid").toString();

		if(uid.equals(groupId)){
			this.fulfillment = "Sorry, you can only use this function in a group chat";
			return;
		}

		String[] keywordArray = keyword.split(" ");
		if(keywordArray.length == 1){
			this.fulfillment = "Please state the event name, type help if any help is needed";
			return;
		}



		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		BasicDBObject GID = new BasicDBObject().append("groupId", groupId);
		ArrayList<Document> group = MongoDB.get(mongo.getCollection("Event").find(GID));
		if(group.size() == 0){
			Document data = new Document();
			data.append("groupId", groupId);
			data.append("uid", uid);
			mongo.getCollection("Event").insertOne(data);
		}

		BasicDBObject eventName = new BasicDBObject().append("Name", keywordArray[1]);
		ArrayList<Document> events = MongoDB.get(mongo.getCollection("Event").find(eventName));
		if(events.size() == 0){
			callEventAdd(uid, groupId, new BasicDBObject().append("uid", uid), mongo);
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

	private void callEventAdd(String uid, String gid, BasicDBObject SELF, MongoDB mongo){
		mongo.getCollection("user").updateOne(SELF,
				new BasicDBObject("$set",
						new BasicDBObject("buff",
								new BasicDBObject().append("cmd", "event::add").append("data", new BasicDBObject().append("groupId", gid)))));
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}
}
