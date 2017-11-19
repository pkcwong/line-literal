package com.pwned.line.service;

import com.pwned.line.http.HTTP;

/***
 * Service for course information.
 * Required params: [uid]
 * Reserved tokens: [@weather::weather]
 * Resolved params: []
 * @author Timothy Pak
 */

public class Weather extends DefaultService{

	/**
	 * Constructor
	 */
	public Weather(Service service){
		super(service);
	}

	/**
	 * Payload of Service module.
	 * @throws Exception Exception
	 */
	@Override
	public void payload() throws Exception{
		this.fulfillment = this.fulfillment.replace("@weather::weather", getWeather());
	}

	/**
	 *
	 * @return Latest weather forecast from HKO
	 */
	public static String getWeather(){
		String link = "http://www.hko.gov.hk/wxinfo/currwx/flw.htm";
		HTTP http = new HTTP(link);
		String weather = http.get();
		String[] messages = {"Weather forecast", "<br/><br/>Outlook"};
		weather = weather.substring(weather.indexOf(messages[0]), weather.indexOf(messages[1]));
		weather = weather.replace("<br/>", "\n");
		while (weather.contains("<")){
			weather = weather.substring(0, weather.indexOf("<")) + weather.substring(weather.indexOf(">") + 1);
		}
		return weather;
	}

	/**
	 *
	 * @return Time of last modification of HKO
	 */
	public static String getDate(){
		String link = "http://www.hko.gov.hk/wxinfo/currwx/flw.htm";
		HTTP http = new HTTP(link);
		String date = http.get();
		String messages = "Bulletin updated at ";
		date = date.substring(date.indexOf(messages) + messages.length(), date.indexOf(messages) + messages.length() + 21);
		return date;
	}

	/**
	 * Request processing from next Service module.
	 * @return Service state
	 * @throws Exception Exception
	 */
	@Override
	public Service chain() throws Exception {
		return this;
	}
}
