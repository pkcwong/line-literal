package com.pwned.line.handler;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.database.MongoDB;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class TextHandler {

	public static void handle(MessageEvent<TextMessageContent> event) throws URISyntaxException {
		TextMessageContent message = event.getMessage();
		String incoming = message.getText();

		MongoDB mongo = new MongoDB("mongodb://user:password@ds115045.mlab.com:15045/heroku_0s8hc3hf", "heroku_0s8hc3hf");
		try {
			JSONObject json = new JSONObject();
			json.put("uid", event.getSource().getUserId());
			json.put("replyToken", event.getReplyToken());
			json.put("timestamp", event.getTimestamp());
			json.put("message", event.getMessage().getText());
			mongo.insert("log", json);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		KitchenSinkController.reply(event.getReplyToken(), new TextMessage(incoming));
	}

}
