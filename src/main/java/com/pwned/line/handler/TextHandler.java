package com.pwned.line.handler;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.pwned.line.interpreter.ApiAI;

import java.net.URISyntaxException;
import java.sql.SQLException;

public class TextHandler {

	public static void handle(LineMessagingClient lineMessagingClient, MessageEvent<TextMessageContent> event) throws URISyntaxException, SQLException {
		String resolve = null;
		TextMessageContent message = event.getMessage();
		resolve = message.getText();
		ApiAI.request(resolve);
	}

}
