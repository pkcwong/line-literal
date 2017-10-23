package com.pwned.line.handler;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.service.DefaultService;
import com.pwned.line.service.Service;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

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
		String incoming = message.getText();
		CompletableFuture.supplyAsync(() -> {
			try {
				return new DefaultService(incoming).resolve().get();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}).thenApply((Service service) -> {
			service.dump();
			KitchenSinkController.reply(event.getReplyToken(), new TextMessage(service.getFulfillment()));
			return null;
		});
	}

}
