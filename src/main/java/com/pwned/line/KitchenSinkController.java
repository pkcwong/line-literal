package com.pwned.line;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.pwned.line.handler.PostbackHandler;
import com.pwned.line.handler.TextHandler;

import java.io.IOException;

@LineMessageHandler
public class KitchenSinkController {

	/***
	 * Default handler for incoming Line text messages.
	 * @param event Line event
	 * @throws Exception Exception
	 */
	@EventMapping
	public static void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
		TextHandler.handle(event);
	}

	/***
	 * Default handler for incoming postback events.
	 * @param event Line event
	 */
	@EventMapping
	public void handlePostbackEvent(PostbackEvent event) {
		PostbackHandler.handle(event);
	}

	/***
	 * Respond to events from users, groups, and rooms.
	 * @param replyToken replyToken received via webhook
	 * @param message messages
	 */
	public static void reply(String replyToken, Message message) {
		try {
			ReplyMessage replyMessage = new ReplyMessage(replyToken, message);
			LineMessagingServiceBuilder.create(System.getenv("LINE_BOT_CHANNEL_TOKEN")).build().replyMessage(replyMessage).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * Send messages to a user, group, or room at any time.
	 * @param id id of the receiver
	 * @param message messages
	 */
	public static void push(String id, Message message) {
		try {
			PushMessage pushMessage = new PushMessage(id, message);
			LineMessagingServiceBuilder.create(System.getenv("LINE_BOT_CHANNEL_TOKEN")).build().pushMessage(pushMessage).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
