package com.pwned.line.service;

import com.pwned.line.http.HTTP;
import org.json.JSONObject;
import java.io.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/***
 * Service for course information.
 * Required params: [uid]
 * Reserved tokens: [@weather::temperature]
 * Resolved params: []
 * @author Timothy Pak
 */

public class Weather extends DefaultService{


	public Weather(Service service) {
		super(service);
	}
	@Override
	public void payload() throws Exception {

		String city = new JSONObject(this.getParam("parameters").toString()).getString("Region1");
		String link = "http://rss.weather.gov.hk/rss/CurrentWeather.xml";
		HTTP weather = new HTTP(link);
		String weather_string = weather.get();
		String temperature = "";
		if(weather_string.contains(city)){
			temperature = weather_string.substring(weather_string.indexOf(city) + city.length() + 59, weather_string.indexOf(city) + city.length() + 61);
		}else{
			if(weather_string.indexOf("degrees Celsius<br/>") - 1 > weather_string.indexOf("Air temperature : ") + 18){
				temperature = weather_string.substring(weather_string.indexOf("Air temperature : ") + 18, weather_string.indexOf("degrees Celsius<br/>") - 1);
			}else{
				temperature = (weather_string.indexOf("degrees Celsius<br/>") - 1) + " " + (weather_string.indexOf("Air temperature : ") + 18);
			}
		}

		if(temperature == ""){
			temperature = "10";
		}
		//String weather_reply = "The temperate of " + city + "is " + temperature + " degrees Celsius at " + time;
		this.fulfillment = this.fulfillment.replace("@weather::temperature", temperature);
	}

	/*public static Document loadXMLFromString(String xml) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource s = new InputSource(new StringReader(xml));

		return builder.parse(s);
	}*/

	@Override
	public Service chain() throws Exception {
		return this;
	}
}
