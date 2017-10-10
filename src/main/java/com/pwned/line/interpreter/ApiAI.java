package com.pwned.line.interpreter;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;

import java.net.URI;

public class ApiAI {

	private static final String BASE_URL = "https://api.api.ai/v1/";
	private static final String DEVELOPER_ACCESS_TOKEN = "9fcc04ad79974d15880465050230ecaf";
	private static final String VERSION = "20170712";

	/***
	 * send GET request to Heroku
	 * @param query
	 */
	public static void request(String query) {

		try {
			URIBuilder host = new URIBuilder();
			host.setHost(BASE_URL).setParameter(query, "query");
			URI uri = host.build();
			//HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(uri);

			request.addHeader("Authorization: Bearer", DEVELOPER_ACCESS_TOKEN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * function of where api.ai will give response
	 * @param json
	 */
	public static void handler(JSONObject json) {

	}

}
