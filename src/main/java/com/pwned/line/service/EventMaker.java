package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.pwned.line.http.HTTP;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		Pattern regex = Pattern.compile("event\\s(.+)");
		Matcher matcher = regex.matcher(keyword);
		String eventName = "";
		while (matcher.find()) {
			eventName = matcher.group(1);
		}

		if(eventName.equals("")){
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
		group = MongoDB.get(mongo.getCollection("Event").find(GID));
		JSONObject events = new JSONObject(group.get(0).toJson());


		if(!checkEventExist(events, eventName)){
			callEventAdd(uid, groupId, new BasicDBObject().append("uid", uid), mongo);
			this.fulfillment = "Please follow that format {EventName}@yyyy/mm/dd\n + " +
						"e.g. Milestone 3 submit@2017/11/20";
			return;
		//Check available timeslot
		}else{
			StringBuilder string = new StringBuilder("Event ");
			string.append(eventName);
			string.append("has be found, the common timeslot for all of you are TESTING");
			this.fulfillment = string.toString();
			return;
		}

		/*URI = URI.replace("{groupId}", groupId);
		URI = URI.replace("{userId}", uid);
		HTTP http = new HTTP(URI);
		http.setHeaders("Authorization", "Bearer " + ACCESS_TOKEN);*/

	}

	private void callEventAdd(String uid, String gid, BasicDBObject SELF, MongoDB mongo){
		mongo.getCollection("user").updateOne(SELF,
				new BasicDBObject("$set",
						new BasicDBObject("buff",
								new BasicDBObject().append("cmd", "event::add").append("data", new BasicDBObject().append("groupId", gid)))));
	}

	public static boolean checkEventExist(JSONObject events, String eventName) throws JSONException {
		if(!events.toString().contains("events")){
			return false;
		}
		for(int i = 0; i < events.getJSONArray("events").length(); i++){
			if(events.getJSONArray("events").get(i).toString().contains(eventName)){
				return true;
			}
		}
		return false;
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}
}
