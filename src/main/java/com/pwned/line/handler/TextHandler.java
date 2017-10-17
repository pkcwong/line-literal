package com.pwned.line.handler;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.service.DefaultService;
import com.pwned.line.service.Service;

import java.net.URISyntaxException;

/***
 * Handler for incoming text message
 * @author Christopher Wong
 */
public class TextHandler {

	/***
	 * Text message handler
	 * @param event Line event
	 * @throws URISyntaxException URISyntaxException
	 */
	public static void handle(MessageEvent<TextMessageContent> event) throws URISyntaxException {
		TextMessageContent message = event.getMessage();
		String resolve = message.getText();
		Service engine = new DefaultService(resolve);
		engine.resolve().thenApply(service -> {
			return new DefaultService(service);
		}).thenApply(service -> {
			return new DefaultService(service);
		}).thenApply(service -> {
			KitchenSinkController.reply(event.getReplyToken(), new TextMessage(service.getFulfillment()));
			return null;
		});
	}

}
