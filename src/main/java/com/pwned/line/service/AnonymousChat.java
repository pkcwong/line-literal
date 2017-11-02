package com.pwned.line.service;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.mongodb.BasicDBObject;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnonymousChat extends DefaultService {

	public AnonymousChat(Service service) {
		super(service);
	}

	@Override
	public void payload() throws Exception {
		if (this.fulfillment.equals("anonymous")) {
			ConfirmTemplate confirmTemplate;
			if (this.getParam("uid").toString().equals(this.getParam("bind").toString())) {
				this.fulfillment = "***\nTap \'Connect\' to chat with a random user!\n***";
				confirmTemplate = new ConfirmTemplate("Anonymous Chat System", new PostbackAction("connect", "anonymous::connect"), new PostbackAction("cancel", "anonymous::cancel"));
			} else {
				this.fulfillment = "***\nTap \'Terminate\' to close the session.\n***";
				confirmTemplate = new ConfirmTemplate("Anonymous Chat System", new PostbackAction("terminate", "anonymous::terminate"), new PostbackAction("cancel", "anonymous::cancel"));
			}
			KitchenSinkController.push(this.getParam("uid").toString(), new TemplateMessage("Anonymous Chat System", confirmTemplate));
		} else if (this.fulfillment.equals("anonymous::connect")) {

			MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));

			BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
			ArrayList<Document> user = MongoDB.get(mongo.getCollection("anonymous").find(SELF));

			if (user.size() == 0) {
				Document data = new Document();
				data.append("uid", this.getParam("uid").toString());
				mongo.getCollection("anonymous").insertOne(data);
				this.fulfillment = "***\nWaiting for a random user!\n***";
			} else {
				this.fulfillment = "***\nAlready in queue!\n***";
			}

		} else if (this.fulfillment.equals("anonymous::terminate")) {

			MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));

			BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
			ArrayList<Document> user = MongoDB.get(mongo.getCollection("user").find(SELF));

			String partner = new JSONObject(user.get(0).toJson()).getString("bind");
			BasicDBObject PARTNER = new BasicDBObject().append("uid", partner);

			mongo.getCollection("user").updateOne(SELF, new BasicDBObject("bind", this.getParam("uid").toString()));
			mongo.getCollection("user").updateOne(PARTNER, new BasicDBObject("bind", partner));

			this.fulfillment = "***\nYour session is terminated.\n***";
		}
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

	public static void run() throws Exception {

		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		ArrayList<Document> list = MongoDB.get(mongo.getCollection("anonymous").find());

		System.out.println("Anonymous bin size: " + list.size());

		if (list.size() >= 2) {
			String NORTH = new JSONObject(list.get(0).toJson()).getString("uid");
			String SOUTH = new JSONObject(list.get(1).toJson()).getString("uid");
			mongo.getCollection("user").updateOne(new BasicDBObject("uid", NORTH), new BasicDBObject("$set", new BasicDBObject("bind", SOUTH)));
			mongo.getCollection("user").updateOne(new BasicDBObject("uid", SOUTH), new BasicDBObject("$set", new BasicDBObject("bind", NORTH)));
			KitchenSinkController.push(NORTH, new TextMessage("***\nYou are connected to a random user!\n***"));
			KitchenSinkController.push(SOUTH, new TextMessage("***\nYou are connected to a random user!\n***"));
			mongo.getCollection("anonymous").deleteOne(new BasicDBObject("uid", NORTH));
			mongo.getCollection("anonymous").deleteOne(new BasicDBObject("uid", SOUTH));
		}

	}

}
