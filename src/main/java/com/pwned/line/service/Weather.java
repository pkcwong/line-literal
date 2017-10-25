package com.pwned.line.service;

import com.pwned.line.http.HTTP;
import com.pwned.line.web.YandexTranslate;
import org.json.JSONObject;

public class Weather extends DefaultService{


	public Weather(Service service) {
		super(service);
	}
	private static String UNDERGROUNDWEATHER_ACCESS_TOKEN = "de8a21a31f2cd2b9";
	@Override
	public void payload() throws Exception {

		String city = new JSONObject(this.getParam("parameters").toString()).getString("city");
		String link = "http://api.wunderground.com/api/de8a21a31f2cd2b9/conditions/q/" + city + ".json";
		HTTP weather = new HTTP(link);
		String weather_string = weather.get();
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}
}
