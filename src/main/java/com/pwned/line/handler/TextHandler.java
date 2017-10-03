package com.pwned.line.handler;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;

public class TextHandler {

	public static void handle(LineMessagingClient lineMessagingClient, MessageEvent<TextMessageContent> event) {
		TextMessageContent message = event.getMessage();
		KitchenSinkController.reply(lineMessagingClient, event.getReplyToken(), new TextMessage(message.getText()));
	}

}
