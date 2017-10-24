package com.pwned.line.web;

import com.pwned.line.http.HTTP;
import org.json.JSONObject;

public class YandexTranslate {

	private static String BASE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate";

	private String ACCESS_TOKEN;
	private String text = "";
	private String lang = "";

	public YandexTranslate(String token, String text, String lang) {
		this.ACCESS_TOKEN = token;
		this.text = text;
		this.lang = lang;
	}

	public JSONObject execute() throws Exception {
		HTTP http = new HTTP(BASE_URL);
		http.setParams("key", this.ACCESS_TOKEN);
		http.setParams("lang", this.lang);
		http.setParams("text", this.text);
		return new JSONObject(http.get());
	}

}
