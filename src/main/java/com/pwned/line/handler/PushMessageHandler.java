package com.pwned.line.handler;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import retrofit2.Response;

import java.io.IOException;

public class PushMessageHandler {

	private String to = null;
	private String message = null;

	public PushMessageHandler(String uid, String message) {
		this.to = uid;
		this.message = message;
	}

	public int execute() {
		try {
			TextMessage textMessage = new TextMessage(this.message);
			PushMessage pushMessage = new PushMessage(this.to, textMessage);
			Response<BotApiResponse> response = LineMessagingServiceBuilder.create(System.getenv("LINE_BOT_CHANNEL_TOKEN")).build().pushMessage(pushMessage).execute();
			return response.code();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
