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
				System.out.println("\n\n\nGet Event result = " + result + "\n\n\n");
			}
		}
		Pattern regex = Pattern.compile("\\{\"EventName\":\"" + eventName + "\",\"Date\":\"(.+)\"\\}");
		Matcher matcher = regex.matcher(result);
		String date = new String();
		while (matcher.find()) {
			date = matcher.group(1);
		}
		System.out.println("\n\n\nGet Event Date = " + date + "\n\n\n");
		return date;
	}


	private String getCommonTimeSlot(MongoDB mongo, ArrayList<Document> group, String date) throws JSONException {
		JSONObject userArr = new JSONObject(group.get(0).toJson());
		StringBuilder common = new StringBuilder("");
		for(int i = 0; i < userArr.getJSONArray("uid").length(); i++){
			if(userArr.getJSONArray("uid").length() == 1){
				return getTimeSlot(mongo,new BasicDBObject().append("uid", userArr.getJSONArray("uid").getString(i)),date);
			}else{
				if(getTimeSlot(mongo,new BasicDBObject().append("uid", userArr.getJSONArray("uid").getString(i)),date).equals("WHOLE DAY")){
					continue;
				}
				String timeslot = getTimeSlot(mongo,new BasicDBObject().append("uid", userArr.getJSONArray("uid").getString(i)),date);
				String[] timeslotArr = timeslot.split("\n");
				for(int j = 0 ; j < timeslotArr.length; j++){
					if(checkAllAvailable(userArr,mongo,timeslotArr[j],date))
						common.append(timeslotArr[j]+"\n");
				}
			}

		}
		if(common.toString().equals("")){
			return "WHOLE DAY";
		}
		return common.toString();
	}

	private boolean checkAllAvailable(JSONObject userArr, MongoDB mongo, String timeslot, String date ) throws JSONException {
		int shr,smin,ehr,emin;
		int arrshr,arrsmin,arrehr,arremin;



		for(int i = 0; i < userArr.getJSONArray("uid").length(); i++){
			BasicDBObject SELF = new BasicDBObject().append("uid", userArr.getJSONArray("uid").getString(i));
			String resultTimeslot = getTimeSlot(mongo, SELF, date);
			String[] resultTimeslotArr = resultTimeslot.split("\n");
			for(int j = 0; j < resultTimeslotArr.length; j++){
				System.out.println("\n\nComparing " + resultTimeslotArr + " and\n " + timeslot + "\n\n\n");
				Pattern regex = Pattern.compile("(.+):(.+)-(.+):(.+)");
				Matcher matcher = regex.matcher(timeslot);
				while (matcher.find()) {
					shr = Integer.parseInt(matcher.group(1));
					smin = Integer.parseInt(matcher.group(2));
					ehr = Integer.parseInt(matcher.group(3));
					emin = Integer.parseInt(matcher.group(4));
					System.out.printf("\n\nTimeslot %d, %d, %d, %d\n\n\n",shr,smin,ehr,emin);
				}

				matcher = regex.matcher(resultTimeslotArr[j]);
				while (matcher.find()) {
					arrshr = Integer.parseInt(matcher.group(1));
					arrsmin = Integer.parseInt(matcher.group(2));
					arrehr = Integer.parseInt(matcher.group(3));
					arremin = Integer.parseInt(matcher.group(4));
					System.out.printf("\n\nARRTimeslot %d, %d, %d, %d\n\n\n",arrshr,arrsmin,arrehr,arremin);
				}
			}
		}

		return true;
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
