package com.pwned.line.service;

import com.linecorp.bot.model.message.TextMessage;
import com.mongodb.BasicDBObject;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;

/***
 * Master Controller for Service modules.
 * Required params: []
 * Reserved tokens: []
 * Resolved params: []
 * @author Calvin Ku
 */
public class MasterController extends DefaultService {

	public MasterController(Service service) {
		super(service);
	}

	/***
	 * Updates user messaging activity on database.
	 * @throws Exception Exception
	 */
	@Override
	public void payload() throws Exception {
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));

		BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
		ArrayList<Document> user = MongoDB.get(mongo.getCollection("user").find(SELF));

		if (user.size() == 0) {
			Document data = new Document();
			data.append("uid", this.getParam("uid").toString());
			data.append("bind", this.getParam("uid").toString());
			data.append("buff", new BasicDBObject("cmd", "master"));
			mongo.getCollection("user").insertOne(data);
			this.setParam("bind", this.getParam("uid").toString());
		} else {
			this.setParam("buff", new JSONObject(user.get(0).toJson()).getString("buff"));
			this.setParam("bind", new JSONObject(user.get(0).toJson()).getString("bind"));
		}

		BasicDBObject data = new BasicDBObject();
		BasicDBObject event = new BasicDBObject();
		event.append("timestamp", this.getParam("timestamp").toString());
		event.append("replyToken", this.getParam("replyToken").toString());
		event.append("text", this.fulfillment);
		data.append("msg", event);

		mongo.getCollection("user").findOneAndUpdate(SELF, new BasicDBObject("$addToSet", data));
	}

	/***
	 * Chaining to specific service modules.
	 * @return Service
	 * @throws Exception Exception
	 */
	@Override
	public Service chain() throws Exception {

		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
		ArrayList<Document> user = MongoDB.get(mongo.getCollection("user").find(SELF));
		JSONObject USER = new JSONObject(user.get(0).toJson());
		if (USER.getJSONObject("buff").getString("cmd").equals("review::add")) {
			return new ReviewAdd(this).resolve().get();
		}

		if (USER.getJSONObject("buff").getString("cmd").equals("event::add")) {
			String temp = this.fulfillment.toLowerCase();
			return new EventAdd(this, temp).resolve().get();
		}

		if (USER.getJSONObject("buff").getString("cmd").equals("timetable::add")) {
			return new TimeTableAdd(this).resolve().get();
		}
		if (USER.getJSONObject("buff").getString("cmd").equals("timetable::addOneByOne")) {
			return new TimeTableAddOneByOne(this).resolve().get();
		}

		if (USER.getJSONObject("buff").getString("cmd").equals("timeslot::edit")) {
			String temp = this.fulfillment.toLowerCase();
			return new EditTimeSlot(this, temp).resolve().get();
		}

		if (this.fulfillment.equals("anonymous") || this.fulfillment.equals("Anonymous")) {
			return new AnonymousChat(this).resolve().get();
		}
		if(this.fulfillment.toLowerCase().equals("timetable::delete")){
			return  new TimeTableDelete(this).resolve().get();
		}

		if (!this.getParam("uid").toString().equals(this.getParam("bind").toString())) {
			KitchenSinkController.push(this.getParam("bind").toString(), new TextMessage(this.fulfillment));
			this.fulfillment = "";
			return this;
		}

		String[] timetable = {"current", "timetable"};
		String[] lift = {"classroom", "room", "lift", "where", "how to go", "get to", "walk to"};
		String[] societies = {"societies", "society", "student Club", "club", "interest group"};
		String[] quota = {"class", "quota"};
		String[] translate = {"translate", "english", "chinese", "korean", "malaysian", "indonesian", "indo"};
		String[] review = {"review"};
		String[] help = {"help"};
		String[] event = {"event"};
		String[] timeslot = {"timeslot","check","edit"};
		String[] accept = {"accept"};
		String[] bring = {"bring"};
		String[] kmb = {"bus", "arrival", "departure", "arrive", "eta"};
		String[] notify = {"notify", "remind", "inform"};
		String[] stop = {"stop"};
		String[] weather = {"weather", "climate", "report"};
		String[] nine = {"9 days weather", "week weather", "next nine days", "next week", "next few days"};
		String[] temperature = {"temperature", "degrees"};


		for (String keywords : timetable) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				return new DialogFlowTimetable(this).resolve().get();
			}
		}
		for (String keywords : accept) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				return new DialogFlowAccept(this).resolve().get();
			}
		}for (String keywords : bring) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				return new DialogFlowBring(this).resolve().get();
			}
		}
		for (String keywords : lift) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				return new DialogFlowLiftAdvisor(this).resolve().get();
			}
		}
		for (String keywords : societies) {
			String[] words = fulfillment.split("\\s+");
			for (String word : words) {
				if (word.toLowerCase().equals(keywords)) {
					return new DialogFlowSociety(this).resolve().get();
				}
			}
		}
		for (String keywords : kmb) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				return new DialogFlowKMB(this).resolve().get();
			}
		}
		for (String keywords : notify) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				return new KMBNotify(this).resolve().get();
			}
		}
		for (String keywords : stop) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				return new KMBStopNotify(this).resolve().get();
			}
		}
		for (String keywords : nine) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				return new DialogFlowNineDaysWeather(this).resolve().get();
			}
		}
		for (String keywords : weather) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				return new DialogFlowWeather(this).resolve().get();
			}
		}
		for (String keywords : temperature) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				return new DialogFlowTemperature(this).resolve().get();
			}
		}
		for (String keywords : quota) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				return new DialogFlowCourseQuota(this).resolve().get();
			}
		}
		for (String keywords : translate) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				return new DialogFlowTranslate(this).resolve().get();
			}
		}
		for (String keywords : review) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				return new DialogFlowReview(this).resolve().get();
			}
		}
		for (String keywords : event) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				return new EventMaker(this, temp).resolve().get();
			}
		}

		for (String keywords : timeslot) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				return new TimeSlot(this, temp).resolve().get();
			}
		}

		for (String keywords : help) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				this.fulfillment= "Our current service provides the following:\n" +
						"1. Anonymous chat: type \"anonymous\"\n" +
						"2. Translate: e.g. translate ... to (specific language)\n" +
						"3. Lift advisor: where is ... \n" +
						"4. Course Review: review of the course ... (department + course code)\n" +
						"5. Weather Forecast: weather forecast\n" +
						"6. Temperature: temperature at a place (e.g. HKUST, Sai Kung, CUHK, Kowloon City)\n" +
						"7. Bus Arrival Time: Estimated time of arrival of next bus at [bus stop](e.g. South Gate)\n" +
						"8. Society information: [Society ...] (e.g. Hall 1)\n" +
						"9. TimeTable: timetable\n" +
						"10. Event Maker: event {Event Name} (e.g. event GroupMeeting)\n" +
						"11. Timeslot for event making: check timeslot/edit timeslot\n" +
						"12. Join Thanksgiving party: accept\n" +
						"13. Bring food for party: Bring {food name}\n";

				return this;
			}
		}
		return new DialogFlowSmalltalk(this).resolve().get();
	}
}
