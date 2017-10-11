package com.pwned.line.interpreter;

import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;

import java.net.URI;

public class ApiAI {

	private static final String BASE_URL = "https://api.api.ai/v1/query?";
	private static final String DEVELOPER_ACCESS_TOKEN = "9fcc04ad79974d15880465050230ecaf";
	private static final String VERSION = "20170712";

	/***
	 * Send GET request to DialogFlow
	 * @param query query string
	 */
	public static void request(String replyToken, String query) {

		try {
			URIBuilder host = new URIBuilder();
			host.setHost(BASE_URL).setParameter("v", VERSION).setParameter("query", query).setParameter("sessionId", replyToken);
			URI uri = host.build();
			HttpGet request = new HttpGet(uri);
			request.addHeader("Authorization: Bearer", DEVELOPER_ACCESS_TOKEN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * Function of where api.ai will give response
	 * @param json received callback
	 */
	public static void handler(JSONObject json) {
		try {
			String replyToken = json.getString("sessionId");
			String message = json.getJSONObject("fulfillment").getString("speech");
			TextMessage msg = new TextMessage(message);
			KitchenSinkController.reply(replyToken, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
