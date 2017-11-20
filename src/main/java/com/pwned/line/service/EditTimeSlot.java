package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.pwned.line.web.MongoDB;
import org.bson.Document;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/***
 * Timeslot editor for TimeSlot
 * Required params: [keyword, uid, parameters]
 * Reserved tokens: []
 * Resolved params: []
 * @author Bear
 */
public class EditTimeSlot extends DefaultService {
	private String keyword;

	/***
	 * Constructor for EditTimeSlot
	 * @param service
	 * @param keyword
	 */
	public EditTimeSlot(Service service, String keyword) {
		super(service);
		this.keyword = keyword;
	}


	/***
	 * Payload for EditTimeSlot
	 * If @param keyword contains add, user is required to add the timeslot by format hh:mm-hh:mm@yyyy/mm/dd. If @param keyword contains drop, user is required to drop the timeslot by format hh:mm-hh:mm@yyyy/mm/dd.
	 * @throws Exception
	 */
	@Override
	public void payload() throws Exception {
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		String uid = this.getParam("uid").toString();
		BasicDBObject SELF = new BasicDBObject().append("uid", uid);
		ArrayList<Document> user = MongoDB.get(mongo.getCollection("TimeSlot").find(SELF));
		if(keyword.equals("cancel")) {
			finish(mongo, SELF);
			this.fulfillment = "Timeslot edit is cancelled";
			return;
		}
		if(user.size() == 0){
			Document doc = new Document();
			doc.append("uid",uid);
			mongo.getCollection("TimeSlot").insertOne(doc);
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
			if(user.toString().contains(date)){
				this.fulfillment = "Sorry, you can only create one timeslot for single day, please edit your timeslot by edit timeslot";
				return;
			}
			createNewTimeSlot(mongo,SELF,start,end,date);
			finish(mongo,SELF);
		}
		else if (keyword.contains("drop")){
			Pattern regex = Pattern.compile("drop\\s(.+)-(.+)@(.+)");
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
			BasicDBObject data = new BasicDBObject();
			BasicDBObject timeslot = new BasicDBObject();
			timeslot.append("Date", date);
			timeslot.append("StartTime", start);
			timeslot.append("EndTime", end);

			data.append("timeslot", timeslot);
			mongo.getCollection("TimeSlot").findOneAndUpdate(SELF, new BasicDBObject("$pull", data));
			this.fulfillment = "The timeslot is removed.";
			finish(mongo,SELF);
			return;
		}else{
			this.fulfillment = "Sorry, this function for editing timeslot is not supported";
			return;
		}

	}


	/***
	 * Add timeslot method for Event maker
	 * Call this method to add new timeslot on the date at start time to end time.
	 * @param mongo
	 * @param SELF
	 * @param start
	 * @param end
	 * @param date
	 */
	public void createNewTimeSlot(MongoDB mongo, BasicDBObject SELF,String start, String end, String date){
		BasicDBObject data = new BasicDBObject();
		BasicDBObject timeslot = new BasicDBObject();
		timeslot.append("Date", date);
		timeslot.append("StartTime", start);
		timeslot.append("EndTime", end);

		data.append("timeslot", timeslot);
		mongo.getCollection("TimeSlot").findOneAndUpdate(SELF, new BasicDBObject("$addToSet", data),
				new FindOneAndUpdateOptions().upsert(true));
		this.fulfillment = "Your available timeslot are successfully added";
	}

	/***
	 * finish editing timeslot method for Event maker
	 * After user edited the timeslot, this method will be called to change the buff state of user to master.
	 * @param mongo
	 * @param SELF
	 * @throws Exception
	 */

	private void finish(MongoDB mongo, BasicDBObject SELF){
		Document doc = new Document();
		doc.append("uid", this.getParam("uid").toString());
		doc.append("bind", this.getParam("uid").toString());
		doc.append("buff", new BasicDBObject("cmd", "master"));
		mongo.getCollection("user").findOneAndUpdate(SELF, new BasicDBObject("$set", doc));

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
