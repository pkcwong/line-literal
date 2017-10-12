package com.pwned.line.interpreter;

import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.http.HTTP;
import org.apache.http.HttpHeaders;
import org.json.JSONObject;

public class ApiAI {

	private static final String BASE_URL = "https://api.api.ai/v1/query";
	private static final String CLIENT_ACCESS_TOKEN = "5d14b8306ce0427fb32ced2c3bdd0b25";
	private static final String VERSION = "20170712";

	/***
	 * Sends a request to DialogFlow
	 * @param replyToken Line replyToken
	 * @param query query string
	 */
	public static void request(String replyToken, String query) {

		try {
			HTTP http = new HTTP(BASE_URL);
			http.setHeaders(HttpHeaders.AUTHORIZATION, "Bearer " + CLIENT_ACCESS_TOKEN);
			http.setParams("v", VERSION);
			http.setParams("query", query);
			http.setParams("sessionId", replyToken);
			JSONObject json = new JSONObject(http.get());
			ApiAI.handler(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * DialogFlow response handler
	 * @param json received callback
	 */
	public static void handler(JSONObject json) {
		try {
			String replyToken = json.getString("sessionId");
			String message = json.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
			TextMessage msg = new TextMessage(message);
			KitchenSinkController.reply(replyToken, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
