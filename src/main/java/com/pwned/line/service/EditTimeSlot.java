package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;

/***
 * Timeslot editor for event maker
 * Required params: [keyword]
 * Reserved tokens: []
 * Resolved params: []
 * @author Bear
 */
public class EditTimeSlot extends DefaultService {
	private String keyword;
	public EditTimeSlot(Service service, String keyword) {
		super(service);
		this.keyword = keyword;
	}


	@Override
	public void payload() throws Exception {
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		String uid = this.getParam("uid").toString();
		BasicDBObject SELF = new BasicDBObject().append("uid", uid);
		ArrayList<Document> user = MongoDB.get(mongo.getCollection("Timeslot").find(SELF));
		if(keyword.equals("cancel")) {
			finish(mongo, SELF);
			return;
		}

		String start = "";
		String end = "";
		String date = "";
		if(keyword.contains("add")){
			Pattern regex = Pattern.compile("add\\s(.+)-(.+)@(.+)");
			Matcher matcher = regex.matcher(keyword);

			if (matcher.find()) {
				start = matcher.group(1);
				end = matcher.group(2);
				date = matcher.group(3);
			}
			if(start.equals("")||end.equals("")||date.equals("")){
				this.fulfillment = "Please follow the format hh:mm-hh:mm@yyyy/mm/dd";
				return;
			}
			createNewTimeSlot(mongo,SELF,start,end,date);
		}

	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

	public void createNewTimeSlot(MongoDB mongo, BasicDBObject SELF,String start, String end, String date){
		BasicDBObject data = new BasicDBObject();
		BasicDBObject timeslot = new BasicDBObject();
		timeslot.append("Date", start);
		timeslot.append("StartTime", end);
		timeslot.append("EndTime", date);

		data.append("timeslot", timeslot);
		mongo.getCollection("TimeSlot").findOneAndUpdate(SELF, new BasicDBObject("$addToSet", timeslot));
		this.fulfillment = "Your available timeslot are successfully added";
	}

	private void finish(MongoDB mongo, BasicDBObject SELF){
		Document doc = new Document();
		doc.append("uid", this.getParam("uid").toString());
		doc.append("bind", this.getParam("uid").toString());
		doc.append("buff", new BasicDBObject("cmd", "master"));
		mongo.getCollection("user").findOneAndUpdate(SELF, new BasicDBObject("$set", doc));

		this.fulfillment = "Your event had been added";
	}




}
