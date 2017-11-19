package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Adding review to MongoDB.
 * Required params: [uid, parameters,keyword]
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

	/***
	 * Payload for EventAdd
	 * Adding event into Database by format {EventName}@yyyy/mm/dd. Pass the date to checkInput() method to check the correctness of the input, if it's not correct, user will be asked to create again. If it's correct, the event with given date and name will be created.
	 * @throws Exception
	 */
	@Override
	public void payload() throws Exception {
		//{EventName}@yyyy/mm/dd@hh:mm-hh:mm
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
		if(keyword.equals("cancel")){
			Document doc = new Document();
			doc.append("uid", this.getParam("uid").toString());
			doc.append("bind", this.getParam("uid").toString());
			doc.append("buff", new BasicDBObject("cmd", "master"));
			mongo.getCollection("user").findOneAndUpdate(SELF, new BasicDBObject("$set", doc));
			this.fulfillment = "Event creation has cancelled";
			return;
		}
		Pattern regex = Pattern.compile("(.+?)@(.+)");
		Matcher matcher = regex.matcher(keyword);
		String eventName,eventDate;
		if (matcher.find()) {
			eventName = matcher.group(1);
			eventDate = matcher.group(2);

		}else{
			this.fulfillment = "Please follow that format {EventName}@yyyy/mm/dd\n + " +
					"e.g. Milestone 3 submit@2017/11/20";
			return;
		}
		if(!checkInput(eventDate)){
			return;
		}


		ArrayList<Document> user = MongoDB.get(mongo.getCollection("user").find(SELF));
		JSONObject USER = new JSONObject(user.get(0).toJson());
		String groupId = USER.getJSONObject("buff").getJSONObject("data").getString("groupId");
		BasicDBObject groupSELF= new BasicDBObject().append("groupId", groupId);
		ArrayList<Document> group = MongoDB.get(mongo.getCollection("Event").find(groupSELF));
		JSONObject events = new JSONObject(group.get(0).toJson());
		if(EventMaker.checkEventExist(events, eventName)){
			this.fulfillment = "The name of event already exist, please retry.";
			return;
		}

		BasicDBObject data = new BasicDBObject();
		BasicDBObject event = new BasicDBObject();
		event.append("EventName", eventName);
		event.append("Date", eventDate);

		data.append("events", event);
		mongo.getCollection("Event").findOneAndUpdate(groupSELF, new BasicDBObject("$addToSet", data));

		Document doc = new Document();
		doc.append("uid", this.getParam("uid").toString());
		doc.append("bind", this.getParam("uid").toString());
		doc.append("buff", new BasicDBObject("cmd", "master"));
		mongo.getCollection("user").findOneAndUpdate(SELF, new BasicDBObject("$set", doc));
		this.fulfillment = "Your event had been added";
	}

	/***
	 * Checking input date for EventAdd
	 * Checking whether the date input by user to create the event is make sense and with correct format.
	 * @param date
	 * @throws Exception
	 */
	private boolean checkInput(String date) throws Exception{
		Pattern regex = Pattern.compile("(.+)/(.+)/(.+)");
		Matcher matcher = regex.matcher(date);
		int year,month,day;
		if (matcher.find()) {
			try{
				year = Integer.parseInt(matcher.group(1));
				month = Integer.parseInt(matcher.group(2));
				day = Integer.parseInt(matcher.group(3));
			}catch (Exception e){
				this.fulfillment = "Please enter correct date.";
				e.printStackTrace();
				throw new Exception("Please enter correct date.");
			}

		}else{
			this.fulfillment = "Please input correct date by yyyy/mm/dd";
			return false;
		}

		if (year > 2018 || year < 2017){
			this.fulfillment = "Invalid year";
			return false;
		}else if(month < 1 || month > 12){
			this.fulfillment = "Invalid month";
			return false;
		}else if (day < 1 || day > 30){
			this.fulfillment = "Invalid day";
			return false;
		}
		return true;

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
