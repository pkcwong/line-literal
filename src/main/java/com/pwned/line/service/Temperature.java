package com.pwned.line.service;

import com.pwned.line.http.HTTP;
import org.json.JSONObject;

/***
 * Service for course information.
 * Required params: [uid]
 * Reserved tokens: [@weather::temperature]
 * Resolved params: []
 * @author Timothy Pak
 */

public class Temperature extends DefaultService{

	public Temperature(Service service){
		super(service);
	}

	@Override
	public void payload() throws Exception{
		this.fulfillment = this.fulfillment.replace("@weather::temperature", getTemperature(new JSONObject(this.getParam("parameters").toString()).getString("Region1")));
	}

	public static String getTemperature(String city){
		String link = "http://rss.weather.gov.hk/rss/CurrentWeather.xml";
		HTTP http = new HTTP(link);
		String temperatureString = http.get();
		String temperature = "";
		if(city.equals("") || city.equals("Hong Kong")){
			city = "Hong Kong";
			temperature = temperatureString.substring(temperatureString.indexOf("Air temperature :") + 18, temperatureString.indexOf("Air temperature :") + 20);
			temperature = temperature + "°C";
		}else if(temperatureString.contains(city)){
			temperature = temperatureString.substring(temperatureString.indexOf(city) + city.length() + 58, temperatureString.indexOf(city) + city.length() + 60);
			temperature = temperature + "°C";
		}else{
			temperature = " is not available";
		}
		temperature = "The temperature at " + city + " is " + temperature + ".";
		return temperature;
	}

	@Override
	public Service chain() throws Exception{
		return this;
	}
}
