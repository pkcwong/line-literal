package com.pwned.line.handler;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.service.ApiAI;
import com.pwned.line.service.CourseQuota;
import com.pwned.line.service.DefaultService;
import com.pwned.line.service.Service;
import org.json.JSONException;
import org.json.JSONObject;

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
				Service apiAiEngine = new ApiAI(service);
				apiAiEngine.setParam("ACCESS_TOKEN", System.getenv("API_AI_ACCESS_TOKEN"));
				apiAiEngine.setParam("uid", event.getSource().getUserId());
				return apiAiEngine.resolve().get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			return null;
		})/*.thenApply((Service service) -> {
			try {
				Service courseQuotaEngine = new CourseQuota(service);
				JSONObject apiParam = new JSONObject(service.getParam("parameters").toString());
				courseQuotaEngine.setParam("DEPARTMENT", apiParam.getString("sis-department"));
				courseQuotaEngine.setParam("COURSE_CODE", apiParam.getString("number"));
				return courseQuotaEngine.resolve().get();
			} catch (JSONException | InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			return service;
		})*/.thenApply((Service service) -> {
			KitchenSinkController.reply(event.getReplyToken(), new TextMessage(service.getFulfillment()));
			return null;
		});
	}

}
