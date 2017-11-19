package com.pwned.line.handler;

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
				Service module = new DefaultService(incoming);
				module.setParam("uid", event.getSource().getUserId());
				module.setParam("replyToken", event.getReplyToken());
				module.setParam("timestamp", event.getTimestamp().toString());
				return new MasterController(module).resolve().get();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}).thenApply((Service service) -> {
			try {
				service.dump();
			} catch (Exception e) {
				e.printStackTrace();
				KitchenSinkController.push(event.getSource().getUserId(), new TextMessage(e.getMessage()));
			}
			KitchenSinkController.reply(event.getReplyToken(), new TextMessage(service.getFulfillment()));
			return null;
		});
	}

}
