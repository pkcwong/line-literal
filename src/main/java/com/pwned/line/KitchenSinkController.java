package com.pwned.line;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.pwned.line.handler.TextHandler;

import java.io.IOException;

@LineMessageHandler
public class KitchenSinkController {

	@EventMapping
	public static void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
		TextHandler.handle(event);
	}

	public static void reply(String replyToken, Message message) {
		try {
			ReplyMessage replyMessage = new ReplyMessage(replyToken, message);
			LineMessagingServiceBuilder.create(System.getenv("LINE_BOT_CHANNEL_TOKEN")).build().replyMessage(replyMessage).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
