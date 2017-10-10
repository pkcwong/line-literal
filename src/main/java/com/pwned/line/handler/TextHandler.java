package com.pwned.line.handler;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;

import java.net.URISyntaxException;
import java.sql.SQLException;

public class TextHandler {

	public static void handle(LineMessagingClient lineMessagingClient, MessageEvent<TextMessageContent> event) throws URISyntaxException, SQLException {
		TextMessageContent message = event.getMessage();
		String incoming = message.getText();
		KitchenSinkController.reply(lineMessagingClient, event.getReplyToken(), new TextMessage(incoming));
	}

}
