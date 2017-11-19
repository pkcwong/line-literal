package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.pwned.line.http.HTTP;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
			mongo.getCollection("Event").insertOne(data);
			data = new Document();
			data.append("uid",uid);
			mongo.getCollection("Event").findOneAndUpdate(GID, new BasicDBObject("$addToSet", data),
					new FindOneAndUpdateOptions().upsert(true));

		}
		group = MongoDB.get(mongo.getCollection("Event").find(GID));
		JSONObject events = new JSONObject(group.get(0).toJson());



		if(!checkEventExist(events, eventName)){
			callEventAdd(groupId, new BasicDBObject().append("uid", uid), mongo);
			this.fulfillment = "Event not found. Please create the event by format {EventName}@yyyy/mm/dd\n + " +
						"e.g. Milestone 3 submit@2017/11/20";
			return;
		//Check available timeslot
		}else{
			StringBuilder string = new StringBuilder("Event ");
			string.append(eventName);
			string.append(" has be found, the common timeslot for all of you are\n");
			string.append(getCommonTimeSlot(mongo,group,getEventDate(events,eventName)) + "\n");
			string.append("Please be reminded that not editing your available timeslot will be considered as available in whole day");
			this.fulfillment = string.toString();
			return;
		}

		/*URI = URI.replace("{groupId}", groupId);
		URI = URI.replace("{userId}", uid);
		HTTP http = new HTTP(URI);
		http.setHeaders("Authorization", "Bearer " + ACCESS_TOKEN);*/

	}

	private void callEventAdd(String gid, BasicDBObject SELF, MongoDB mongo){
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

	private String getEventDate(JSONObject events, String eventName) throws JSONException {
		// {"EventName":"milestone 3 submit","Date":"2017/11/20"}
		String result = new String();
		for(int i = 0; i < events.getJSONArray("events").length(); i++){
			if(events.getJSONArray("events").get(i).toString().contains(eventName)){
				result = events.getJSONArray("events").get(i).toString();
			}
		}
		Pattern regex = Pattern.compile("\\{\"EventName\":\"" + eventName + "\",\"Date\":\"(.+)\"\\}");
		Matcher matcher = regex.matcher(result);
		String date = new String();
		while (matcher.find()) {
			date = matcher.group(1);
		}
		return date;
	}


	private String getCommonTimeSlot(MongoDB mongo, ArrayList<Document> group, String date) throws JSONException {
		JSONObject userArr = new JSONObject(group.get(0).toJson());
		StringBuilder common = new StringBuilder("");
		String[] allTimeslot = new String[userArr.getJSONArray("uid").length()];
		for(int i = 0; i < userArr.getJSONArray("uid").length(); i++){
			allTimeslot[i] = getTimeSlot(mongo,new BasicDBObject().append("uid", userArr.getJSONArray("uid").getString(i)),date);
			if(userArr.getJSONArray("uid").length() == 1){
				return allTimeslot[0];
			}
		}
		addAllCommonTimeslot(allTimeslot,common);
		if(common.toString().contains("NOT FOUND")){
			return "Sorry, no common timeslot are found";
		}else if (common.toString().equals("")){
			return "WHOLE DAY";
		}
		return common.toString();
	}

	//private boolean checkAvailable(){


	private void addAllCommonTimeslot(String[] allTimeslot, StringBuilder common){
		int startHour,starMinute,endHour,endMinute,arrStartHour,arrStarMinute,arrEndHour,arrEndMinute;
		Pattern regex = Pattern.compile("(.+):(.+)-(.+):(.+)");
		//Matcher matcher = regex.matcher(timeslotFor1user[i][j]);

		int i, j;

		String[][] timeslotFor1user = new String[allTimeslot.length][];
		for(i = 0 ; i < allTimeslot.length; i++){
			for(j = 0 ; j < allTimeslot[i].split("\n").length; j++){
				timeslotFor1user[i] = allTimeslot[i].split("\n");
				System.out.printf("\n\n\n%s\n\n\n", timeslotFor1user[i][j]);
				Matcher matcher = regex.matcher(timeslotFor1user[i][j]);
				startHour = Integer.parseInt(matcher.group(1));
				starMinute = Integer.parseInt(matcher.group(2));
				endHour = Integer.parseInt(matcher.group(3));
				endMinute = Integer.parseInt(matcher.group(4));
				for(int x = 1 ; i < allTimeslot.length; x++){
					for(int y = 0 ; j < allTimeslot[i].split("\n").length; y++){
						if(timeslotFor1user[x][y].equals("WHOLE DAY")){
							break;
						}
						matcher = regex.matcher(timeslotFor1user[x][y]);
						arrStartHour = Integer.parseInt(matcher.group(1));
						arrStarMinute = Integer.parseInt(matcher.group(2));
						arrEndHour = Integer.parseInt(matcher.group(3));
						arrEndMinute = Integer.parseInt(matcher.group(4));
						System.out.printf("\n\nComparing %d:%d-%d%d and %d:%d-%d:%d\n\n",startHour,starMinute,endHour,endMinute,arrStartHour,arrStarMinute,arrEndHour,arrEndMinute);

					}
				}
			}
		}




		common.append("NOT FOUND");
	}

	private String getTimeSlot(MongoDB mongo, BasicDBObject SELF, String date) throws JSONException {
		ArrayList<Document> user = MongoDB.get(mongo.getCollection("TimeSlot").find(SELF));
		if(user.size() == 0){
			return "WHOLE DAY";
		}
		JSONObject timeArr = new JSONObject(user.get(0).toJson());
		StringBuilder result = new StringBuilder("");
		for(int i = 0; i < timeArr.getJSONArray("timeslot").length(); i++){
			if(timeArr.getJSONArray("timeslot").getString(i).contains(date)){
				String time = timeArr.getJSONArray("timeslot").getString(i);
				//{"EndTime":"15:30","StartTime":"13:00","Date":"2017/11/27"}
				Pattern regex = Pattern.compile("\\{\"EndTime\":\"(.+)\",\"StartTime\":\"(.+)\",\"Date\":\"" + date + "\"\\}");
				Matcher matcher = regex.matcher(time);

				while (matcher.find()) {
					result.append(matcher.group(2));
					result.append("-");
					result.append(matcher.group(1));
					result.append("\n");
				}

			}

		}
		if(result.toString().equals("")){
			return "WHOLE DAY";
		}
		return result.toString();

	}

	@Override
	public Service chain() throws Exception {
		return this;
	}
}
