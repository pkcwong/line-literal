package com.pwned.line.service;

import com.pwned.line.web.YandexTranslate;
import org.json.JSONObject;

/**
 * Service module for translation.
 * Required params: [parameters]
 * Reserved tokens: []
 * Resolved params: []
 * @author Christopher Wong, Calvin Ku
 */
public class Translate extends DefaultService {

	private static String YANDEX_ACCESS_TOKEN = "trnsl.1.1.20171018T105910Z.b2c2d71c4cea2b88.deb0f12c301616942345158b7773860e9b9b80d3";

	public Translate(Service service) {
		super(service);
	}

	@Override
	public void payload() throws Exception {
		JSONObject parameters = new JSONObject(this.getParam("parameters").toString());
		JSONObject yandex = new YandexTranslate(YANDEX_ACCESS_TOKEN, parameters.getString("phrase"), parameters.getString("lang-to")).execute();
		this.fulfillment = yandex.getJSONArray("text").get(0).toString();
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

}
