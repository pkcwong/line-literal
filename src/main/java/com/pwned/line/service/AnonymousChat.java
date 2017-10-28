package com.pwned.line.service;

import com.linecorp.bot.model.message.TextMessage;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.web.MongoDB;
import org.bson.Document;

import java.util.ArrayList;

public class AnonymousChat extends DefaultService {

	public AnonymousChat(Service service) {
		super(service);
	}

	@Override
	public void payload() throws Exception {
		MongoDB mongo = new MongoDB("mongodb://user:password@ds115045.mlab.com:15045/heroku_0s8hc3hf", "heroku_0s8hc3hf");

		BasicDBObject projection = new BasicDBObject();
		projection.append("uid", 1);
		BasicDBObject constraint = new BasicDBObject();
		constraint.append("uid", new BasicDBObject("$ne", this.getParam("uid")));
		MongoCursor<Document> cursor = mongo.getCollection("user").find(constraint).projection(projection).iterator();

		ArrayList<String> uid = new ArrayList<>();
		while (cursor.hasNext()) {
			uid.add(cursor.next().get("uid").toString());
		}

		if (uid.size() != 0) {
			int index = (int) (Math.random() * uid.size());

			BasicDBObject own = new BasicDBObject();
			constraint.append("uid", new BasicDBObject("$eq", this.getParam("uid")));

			BasicDBObject op = new BasicDBObject();
			op.append("$set", new BasicDBObject().append("bind", uid.get(index)));

			mongo.getCollection("user").updateOne(own, op);

			BasicDBObject con = new BasicDBObject();
			con.append("uid", new BasicDBObject("$eq", uid.get(index)));

			BasicDBObject tar = new BasicDBObject();
			tar.append("$set", new BasicDBObject().append("bind", this.getParam("uid")));

			mongo.getCollection("user").updateOne(con, tar);

			KitchenSinkController.push(uid.get(index), new TextMessage("You are connected to a random user!"));

			this.fulfillment = "You are connected to a random user!";
		}

		cursor.close();
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

}
