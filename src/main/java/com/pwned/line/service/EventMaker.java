package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Service for event maker.
 * Required params: [uid, groupId parameters]
 * Reserved tokens: []
 * Resolved params: []
 * @author Bear
 */

public class EventMaker extends DefaultService{
	private String keyword;
	/**
	 * Constructor for EventMaker
	 * @param service
	 * @param keyword
	 */
	public EventMaker(Service service, String keyword){
		super(service);
		this.keyword = keyword;

	}

	/**
	 * Payload for Event maker.
	 * The method search the event name pass by user. If it already exists, then search for the common available timeslot for every group members.
	 * If the event doesn't exist, then user will be required to create the event by format {EventName}@yyyy/mm/dd.
	 * @throws Exception
	 */
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
			this.fulfillment = "Event not found. Please create the event by format {EventName}@yyyy/mm/dd\n" +
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


	}

	/**
	 * callEventAdd for EventMaker
	 * This method is called by payload(). When user call event {event name} which the event is not exists, this method will be called and set buff's cmd to event::add and ask user to create and event.
	 * @param gid
	 * @param SELF
	 * @param mongo
	 */
	private void callEventAdd(String gid, BasicDBObject SELF, MongoDB mongo){
		mongo.getCollection("user").updateOne(SELF,
				new BasicDBObject("$set",
						new BasicDBObject("buff",
								new BasicDBObject().append("cmd", "event::add").append("data", new BasicDBObject().append("groupId", gid)))));
	}

	/**
	 * Check whether an event is exist for EventMaker and EventAdd
	 * This static method will be called by EventAdd class and EventMaker class. In EventMaker, this method is called to check whether the event is exists. If not, then ask user to create one. In EventAdd class, it is called to secure the user will not create event with duplicated name.
	 * @param events
	 * @param eventName
	 * @throws JSONException
	 * @return Whether an event with existing name exist.
	 */
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

	/**
	 * Get the date of the event for EventMaker
	 * @param events
	 * @param eventName
	 * @throws JSONException
	 * @return The date of event
	 */
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

	/**
	 * Adding common time slot for EventMaker
	 * Get the timeslot JSONArray and find the available timeslot of the user in @param date day.
	 * @param mongo
	 * @param SELF
	 * @param date
	 * @throws JSONException
	 * @return Available time slot for one user in certain date
	 */
	public String getTimeSlot(MongoDB mongo, BasicDBObject SELF, String date) throws JSONException {
		ArrayList<Document> user = MongoDB.get(mongo.getCollection("TimeSlot").find(SELF));
		if(user.size() == 0){
			return "WHOLE DAY";
		}
		JSONObject timeArr = new JSONObject(user.get(0).toJson());
		StringBuilder result = new StringBuilder("");
		for(int i = 0; i < timeArr.getJSONArray("timeslot").length(); i++){
			if(timeArr.getJSONArray("timeslot").getString(i).contains(date)){
				String time = timeArr.getJSONArray("timeslot").getString(i);
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

	/**
	 * Get common time slot according to the available time for EventMaker
	 * Calling addAllCommonTimeslot() method to add all common time slot for the given event. Get the available time slot for all group members and pass it to addAllCommonTimeslot() method to compare the time slot.
	 * @param mongo
	 * @param group
	 * @param date
	 * @throws JSONException
	 * @return The common available time slot
	 */
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


	/**
	 * Adding common time slot for EventMaker
	 * Compare the time slot in the @param allTimeslot, check if there exists a common time slot for the event and add it to @param common.
	 * @param allTimeslot
	 * @param common
	 * @throws JSONException
	 */
	private void addAllCommonTimeslot(String[] allTimeslot, StringBuilder common) throws ParseException {

		SimpleDateFormat parser = new SimpleDateFormat ("HH:mm");

		int count = allTimeslot.length-1;
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
			break;
		}

		for(int j = 0 ; j < allTimeslot.length; j++){
			if(i == j){
				continue;
			}
			if(allTimeslot[j].equals("WHOLE DAY")){
				count--;
				continue;
			}
			String[] temp = allTimeslot[j].split("-");
			Date startTime1 = parser.parse(temp[0]);
			Date endTime1 = parser.parse(temp[1]);
			if((startTime.before(endTime1) && (endTime.after(startTime1)))){
				count--;

				if(startTime.before(startTime1)){
					startTime = parser.parse(temp[0]);
				}
				if(endTime1.before(endTime)){
					endTime = parser.parse(temp[1]);
				}

			}
		}

		if(count == 0){
			StringBuilder time = new StringBuilder(parser.format(startTime));
			time.append("-");
			time.append(parser.format(endTime));
			common.append(time.toString());
			return;
		}
		common.append("NOT FOUND");
	}




	/***
	 * Chain for Event maker
	 * @return Service state
	 * @throws Exception
	 */
	@Override
	public Service chain() throws Exception {
		return this;
	}
}
