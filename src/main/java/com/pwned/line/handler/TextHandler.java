package com.pwned.line.handler;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.database.MongoDB;
import org.bson.Document;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class TextHandler {

	public static void handle(MessageEvent<TextMessageContent> event) throws URISyntaxException {
		TextMessageContent message = event.getMessage();
		String incoming = message.getText();

		MongoDB mongo = new MongoDB("mongodb://user:password@ds115045.mlab.com:15045/");
		MongoDatabase db = mongo.selectDB("heroku_0s8hc3hf");
		MongoCollection<Document> collection = db.getCollection("log");
		Map<String, Object> data = new HashMap<>();
		data.put("id", event.getSource().getUserId());
		data.put("replyToken", event.getReplyToken());
		data.put("msg", message.getText());
		Document document = new Document(data);
		collection.insertOne(document);

		KitchenSinkController.reply(event.getReplyToken(), new TextMessage(incoming));
	}

}
