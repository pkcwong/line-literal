package com.pwned.line.service;

import com.pwned.line.http.HTTP;
import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;

/***
 * Service for Yandex translation api.
 * Required params: [YANDEX_ACCESS_TOKEN, LANG]
 * @author Christopher Wong, Calvin Ku
 */
public class Yandex extends DefaultService {

	private static final String BASE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate";

	public Yandex(Service service) {
		super(service);
	}

	/***
	 * Yandex payload
	 * @return instance
	 * @throws Exception exception
	 */
	@Override
	public CompletableFuture<Service> resolve() throws Exception {
		this.dump();
		HTTP http = new HTTP(BASE_URL);
		http.setParams("key", this.getParam("YANDEX_ACCESS_TOKEN"));
		http.setParams("text", this.fulfillment);
		http.setParams("lang", this.getParam("LANG"));
		this.handler(new JSONObject(http.get()));
		return CompletableFuture.supplyAsync(() -> this);
	}

	/***
	 * handler for Yandex response
	 * @param httpResponse http response
	 * @throws Exception exception
	 */
	private void handler(JSONObject httpResponse) throws Exception {
		this.fulfillment = httpResponse.toString();
	}

}
