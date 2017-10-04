package com.pwned.line;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.pwned.line.handler.TextHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@LineMessageHandler
public class KitchenSinkController {

	@Autowired
	private LineMessagingClient lineMessagingClient;

	@EventMapping
	public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
		TextHandler.handle(lineMessagingClient, event);
	}

	private void reply(String replyToken, Message message) {
		this.reply(replyToken, Collections.singletonList(message));
	}

	private void reply(String replyToken, List<Message> messages) {
		try {
			lineMessagingClient.replyMessage(new ReplyMessage(replyToken, messages)).get();
		} catch (InterruptedException | ExecutionException e) {
			throw  new RuntimeException(e);
		}
	}

	public static void reply(LineMessagingClient lineMessagingClient, String replyToken, Message message) {
		KitchenSinkController.reply(lineMessagingClient, replyToken, Collections.singletonList(message));
	}

	public static void reply(LineMessagingClient lineMessagingClient, String replyToken, List<Message> messages) {
		try {
			lineMessagingClient.replyMessage(new ReplyMessage(replyToken, messages)).get();
		} catch (InterruptedException | ExecutionException e) {
			throw  new RuntimeException(e);
		}
	}

}
