package com.pwned.line.handler;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.service.ApiAI;
import com.pwned.line.service.DefaultService;
import com.pwned.line.service.Service;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

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
		new DefaultService(incoming).resolve().thenApply((Service service) -> {
			try {
				Service apiAiEngine = new ApiAI(service).resolve().get();
				apiAiEngine.setParam("ACCESS_TOKEN", System.getenv("API_AI_ACCESS_TOKEN"));
				apiAiEngine.setParam("uid", event.getSource().getUserId());
				return apiAiEngine;
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			return null;
		}).thenApply((Service service) -> {
			KitchenSinkController.reply(event.getReplyToken(), new TextMessage(service.getFulfillment()));
			return null;
		});
	}

}
