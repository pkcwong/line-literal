package com.pwned.line.handler;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.interpreter.ApiAI;

import java.net.URISyntaxException;

public class TextHandler {

	public static void handle(MessageEvent<TextMessageContent> event) throws URISyntaxException {
		TextMessageContent message = event.getMessage();
		String incoming = message.getText();
		ApiAI.request(event.getReplyToken(), incoming);
		//KitchenSinkController.reply(event.getReplyToken(), new TextMessage(incoming));
	}

}
