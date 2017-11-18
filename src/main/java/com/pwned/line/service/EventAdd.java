package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		//{EventName}@yyyy/mm/dd@hh:mm-hh:mm
		Pattern regex = Pattern.compile("(.+?)@(.+)@(.+):(.+)-(.+):(.+)");
		Matcher matcher = regex.matcher(keyword);
		String eventName,eventDate,startHour,startMinute,endHour,endMinute;
		if (matcher.find()) {
			eventName = matcher.group(1);
			eventDate = matcher.group(2);
			startHour = matcher.group(3);
			startMinute = matcher.group(4);
			endHour = matcher.group(5);
			endMinute = matcher.group(6);

		}else{
			this.fulfillment = "Please follow that format {EventName}@yyyy/mm/dd@hh:mm-hh:mm\n + " +
					"e.g. Milestone 3 submit@2017/11/20@18:00-23:59";
			return;
		}
		if(!checkInput(eventDate,startHour,startMinute,endHour,endMinute)){
			return;
		}



		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));


		//fetch buff -> data from MongoDB
		BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
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
		event.append("StartTime", startHour + ":" + startMinute);
		event.append("EndTime", endHour + ":" + endMinute);

		data.append("events", event);
		mongo.getCollection("Event").findOneAndUpdate(groupSELF, new BasicDBObject("$addToSet", data));

		Document doc = new Document();
		doc.append("uid", this.getParam("uid").toString());
		doc.append("bind", this.getParam("uid").toString());
		doc.append("buff", new BasicDBObject("cmd", "master"));
		mongo.getCollection("user").findOneAndUpdate(SELF, new BasicDBObject("$set", doc));
		this.fulfillment = "Your event had been added";
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

	private boolean checkInput(String date, String shour, String sminute, String ehour, String eminute) throws Exception{
		Pattern regex = Pattern.compile("(.+)/(.+)/(.+)");
		Matcher matcher = regex.matcher(date);
		int year,month,day,smin,shr,emin,ehr;
		if (matcher.find()) {
			try{
				year = Integer.parseInt(matcher.group(1));
				month = Integer.parseInt(matcher.group(2));
				day = Integer.parseInt(matcher.group(3));
				smin = Integer.parseInt(sminute);
				shr = Integer.parseInt(shour);
				emin = Integer.parseInt(eminute);
				ehr = Integer.parseInt(ehour);
			}catch (Exception e){
				this.fulfillment = "Please enter correct date and time";
				e.printStackTrace();
				throw new Exception("Please enter correct date and time");
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
		}else if (smin < 0 || emin < 0 || smin > 59 || emin > 59){
			this.fulfillment = "Invalid minute";
			return false;
		}else if (shr < 0 || ehr < 0 || shr > 23 || ehr > 23){
			this.fulfillment = "Invalid hour";
			return false;
		}
		return true;

	}
}
