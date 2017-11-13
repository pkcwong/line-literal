package com.pwned.line.handler;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.service.DefaultService;
import com.pwned.line.service.MasterController;
import com.pwned.line.service.Service;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

/***
 * Handler for joining event
 * @author Bear
 */
public class JoinHandler {

	public static void handle(JoinEvent event) throws URISyntaxException {
		//String replyToken = event.getReplyToken();
		System.out.println("Inside event handler" + event.getSource().getSenderId());
	}

}
