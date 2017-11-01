package com.pwned.line.handler;

import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.service.AnonymousChat;
import com.pwned.line.service.DefaultService;
import com.pwned.line.service.Service;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostbackHandler {

	public static void handle(PostbackEvent event) {
		String incoming = event.getPostbackContent().getData();
		CompletableFuture.supplyAsync(() -> {
			try {
				String namespace = "";
				String data = "";
				Pattern regex = Pattern.compile("(.+?)::(.+)");
				Matcher matcher = regex.matcher(incoming);
				while (matcher.find()) {
					namespace = matcher.group(1);
					data = matcher.group(2);
				}
				if (namespace.equals("anonymous")) {
					Service module = new DefaultService(incoming);
					module.setParam("uid", event.getSource().getUserId());
					module.setParam("replyToken", event.getReplyToken());
					module.setParam("timestamp", event.getTimestamp().toString());
					return new AnonymousChat(module).resolve().get();
				}
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
