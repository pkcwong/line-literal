package com.pwned.line.handler;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.service.ApiAI;
import com.pwned.line.service.QuotaCrawler;

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
		String incoming = message.getText();
		ApiAI apiAIEngine = new ApiAI(incoming);
		apiAIEngine.setArgs("ACCESS_TOKEN", System.getenv("API_AI_ACCESS_TOKEN"));
		apiAIEngine.setArgs("uid", event.getSource().getUserId());
		KitchenSinkController.reply(event.getReplyToken(), new TextMessage(apiAIEngine.resolve()));
		QuotaCrawler quotaCrawlerEngine = new QuotaCrawler("The course title is @data");
		quotaCrawlerEngine.setArgs("DEPARTMENT", "ACCT");
		quotaCrawlerEngine.setArgs("COURSE_CODE", "1010");
		System.out.println(quotaCrawlerEngine.resolve());
	}

}
