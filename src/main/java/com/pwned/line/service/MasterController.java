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

	@Override
	public void payload() throws Exception {
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));

		BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
		ArrayList<Document> user = MongoDB.get(mongo.getCollection("user").find(SELF));

		if (user.size() == 0) {
			Document data = new Document();
			data.append("uid", this.getParam("uid").toString());
			data.append("bind", this.getParam("uid").toString());
			data.append("buff", new BasicDBObject("cmd", ""));
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
		ArrayList<Document> user = MongoDB.get(mongo.getCollection("user").find());
		JSONObject USER = new JSONObject(user.get(0).toJson());
		if (USER.getJSONObject("buff").getString("cmd").equals("review::add")) {
			return new ReviewAdd(this).resolve().get();
		}

		if (this.fulfillment.equals("anonymous") || this.fulfillment.equals("Anonymous")) {
			return new AnonymousChat(this).resolve().get();
		}

		if (!this.getParam("uid").toString().equals(this.getParam("bind").toString())) {
			KitchenSinkController.push(this.getParam("bind").toString(), new TextMessage(this.fulfillment));
			this.fulfillment = "";
			return this;
		}

		String[] timetable = {"current"};
		String[] lift = {"classroom", "room", "lift", "where"};
		String[] societies = {"societies"};
		String[] KMB = {"bus", "arrival", "departure"};
		String[] weather = {"weather", "degrees", "climate"};
		String[] temperature = {"temperature"};
		String[] quota = {"class"};
		String[] translate = {"translate", "english", "chinese", "korean", "malaysian", "indonesian", "indo"};
		String[] review = {"review"};
		String[] help = {"help"};

		for (String keywords : timetable) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				//return new DialogFlowTranslate(this).resolve().get();
			}
		}
		for (String keywords : lift) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				return new DialogFlowLiftAdvisor(this).resolve().get();
			}
		}
		for (String keywords : societies) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				//return new DialogFlowTranslate(this).resolve().get();
			}
		}
		for (String keywords : KMB) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				//return new DialogFlowTranslate(this).resolve().get();
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
				return new CourseName(this).resolve().get();
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
		for (String keywords : help) {
			String temp = this.fulfillment.toLowerCase();
			if(temp.contains(keywords)){
				this.fulfillment= "Our current service only provides the following:\n" +
						"1. Anonymous chat: type \"anonymous\"\n" +
						"2. Translate: e.g. translate ... to (specific language)\n" +
						"3. Lift advisor: where is ... \n" +
						"4. Course Review: review of the course ... (department + course code)\n" +
						"5. Weather: weather a place (e.g. HKUST, sai kung, CUHK, kowloon city)\n";
			}
		}
		this.fulfillment = "Sorry, we don't understand this. Please call out \"help\"!";
		return this;
	}
}
