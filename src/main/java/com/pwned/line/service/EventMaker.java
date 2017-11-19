package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.pwned.line.http.HTTP;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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


	private String getCommonTimeSlot(MongoDB mongo, ArrayList<Document> group, String date) throws JSONException, ParseException {
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


	private void addAllCommonTimeslot(String[] allTimeslot, StringBuilder common) throws ParseException {
		/*
		SimpleDateFormat parser = new SimpleDateFormat ("HH:mm");

		int i, j;
		int count =  allTimeslot.length;

		String[][] timeslotFor1user = new String[allTimeslot.length][];
		for(i = 0 ; i < allTimeslot.length; i++){
			for(j = 0 ; j < allTimeslot[i].split("\n").length; j++){
				timeslotFor1user[i] = allTimeslot[i].split("\n");
				System.out.println("\n" + timeslotFor1user[i][j] + "\n");
				if(timeslotFor1user[i][j].equals("WHOLE DAY")){
					break;
				}
				for(int x = 1; x < allTimeslot.length; x++)
					for(int y = 0 ; j < allTimeslot[i].split("\n").length; y++) {
						String[] temp = timeslotFor1user[i][j].split("-");
						Date startTime = parser.parse(temp[0]);
						Date endTime = parser.parse(temp[1]);
						String[] temp2 = timeslotFor1user[x][y].split("-");
						Date startTime2 = parser.parse(temp[0]);
						Date endTime2 = parser.parse(temp[1]);
						if(startTime.before(startTime2) && endTime.after(endTime2)){
							StringBuilder time = new StringBuilder(parser.format(startTime));
							time.append("-");
							time.append(parser.format(startTime));
							common.append(parser.format(startTime));
							startTime.
						}
					}
			}
		}*/
		SimpleDateFormat parser = new SimpleDateFormat ("HH:mm");

		int count = allTimeslot.length;
		int i;
		Date startTime = new Date();
		Date endTime = new Date();
		for(i = 0 ; i < allTimeslot.length; i++){
			if(allTimeslot[i].equals("WHOLE DAY")){
				//count--;
				continue;
			}
			String[] temp = allTimeslot[i].split("-");
			startTime = parser.parse(temp[0]);
			endTime = parser.parse(temp[1]);
			System.out.println("\n\nef timeslot " + startTime.toString() + ":" + endTime.toString() + "\n\n");
			break;
		}
		for(int j = 0 ; j != i && j < allTimeslot.length; j++){
			if(allTimeslot[i].equals("WHOLE DAY")){
				//count--;
				continue;
			}
			String[] temp = allTimeslot[i].split("-");
			Date startTime1 = parser.parse(temp[0]);
			Date endTime2 = parser.parse(temp[1]);
			if((startTime.before(startTime1) || startTime.equals(startTime1)) && (endTime.after(endTime2) || endTime.equals(endTime2))){
				count --;
				System.out.printf("\nBefore %s",startTime.toString());
				startTime = startTime1;
				endTime = endTime2;
				System.out.printf("\nAfter %s",startTime.toString());
			}
		}

		if(count == 0){
			StringBuilder time = new StringBuilder(parser.format(startTime));
			time.append("-");
			time.append(parser.format(endTime));
			System.out.printf("\ntime %s",time.toString());
			common.append(time.toString());
			return;
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
