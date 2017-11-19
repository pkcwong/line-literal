package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.pwned.line.http.HTTP;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Timeslot editor for event maker
 * Required params: [keyword]
 * Reserved tokens: []
 * Resolved params: []
 * @author Bear
 */
public class TimeSlot extends DefaultService {
	private String keyword;
	public TimeSlot(Service service, String keyword) {
		super(service);
		this.keyword = keyword;
	}


	@Override
	public void payload() throws Exception {
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		String uid = this.getParam("uid").toString();
		String groupId = this.getParam("groupId").toString();
		BasicDBObject SELF = new BasicDBObject().append("uid", uid);
		ArrayList<Document> user = MongoDB.get(mongo.getCollection("TimeSlot").find(SELF));
		BasicDBObject groupSELF = new BasicDBObject().append("groupId", groupId);
		ArrayList<Document> group = MongoDB.get(mongo.getCollection("Event").find(groupSELF));

		if(user.size() == 0){
			Document data = new Document();
			data.append("uid", uid);
			mongo.getCollection("Event").findOneAndUpdate(groupSELF, new BasicDBObject("$addToSet", data),
					new FindOneAndUpdateOptions().upsert(true));
		}

		if(keyword.contains("edit")){
			callEditTimeSlot(uid,SELF,mongo);
			this.fulfillment = "Please give your command by: \n" +
								"1. Add new available time for the day by: e.g. add 18:00-19:30@2017/11/27\n" +
								"2. Drop existing  available time for the day by: e.g. drop 18:00-19:30@2017/11/27\n";
			return;
		}else if (keyword.contains("check")){
			if(user.size() == 0){
				this.fulfillment = "You don't have any available timeslot, please create one first by calling edit timeslot.";
				return;
			}
			JSONObject timeArr = new JSONObject(user.get(0).toJson());
			StringBuilder result = new StringBuilder();
			for(int i = 0; i < timeArr.getJSONArray("timeslot").length(); i++){
				String time = timeArr.getJSONArray("timeslot").getString(i);
				//{"EndTime":"15:30","StartTime":"13:00","Date":"2017/11/27"}
				Pattern regex = Pattern.compile("\\{\"EndTime\":\"(.+)\",\"StartTime\":\"(.+)\",\"Date\":\"(.+)\"\\}");
				Matcher matcher = regex.matcher(time);

				while (matcher.find()) {
					result.append(matcher.group(2));
					result.append("-");
					result.append(matcher.group(1));
					result.append("@");
					result.append(matcher.group(3));
					result.append("\n");

				}
			}

			this.fulfillment = "Your timeslot:\n" + result.toString();
		}else{
			this.fulfillment = "Sorry, this function for time slot is not supported";
			return;
		}
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}


	private void callEditTimeSlot(String uid, BasicDBObject SELF, MongoDB mongo){
		mongo.getCollection("user").updateOne(SELF,
				new BasicDBObject("$set",
						new BasicDBObject("buff",
								new BasicDBObject().append("cmd", "timeslot::edit").append("data", new BasicDBObject().append("uid", uid)))));
	}


}
