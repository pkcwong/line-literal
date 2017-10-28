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
	//private static String UNDERGROUNDWEATHER_ACCESS_TOKEN = "de8a21a31f2cd2b9";
	@Override
	public void payload() throws Exception {

		String city = "Hong Kong";
		//scity = new JSONObject(this.getParam("parameters").toString()).getString("city");
		//String link = "http://api.wunderground.com/api/de8a21a31f2cd2b9/conditions/q/" + city + ".json";
		String link = "http://rss.weather.gov.hk/rss/CurrentWeather_uc.xml";
		HTTP weather = new HTTP(link);
		String weather_string = weather.get();

		//Document weather_xml_document = loadXMLFromString(weather_string);
		//weather_xml_document.getDocumentElement().normalize();
		//NodeList nList = weather_xml_document.getElementsByTagName("tr");
		weather_string.indexOf("Bulletin");
		String time = weather_string.substring(weather_string.indexOf("<pubDate>") + 9, weather_string.indexOf("</pubDate>") - 1);
		String temperature = "";
		if(weather_string.indexOf("degrees Celsius<br/>") - 1 > weather_string.indexOf("Air temperature : ") + 18){
			temperature = weather_string.substring(weather_string.indexOf("Air temperature : ") + 18, weather_string.indexOf("degrees Celsius<br/>") - 1);
		}
		if(temperature == ""){
			temperature = "27";
		}
		String weather_reply = "The temperate of " + city + "is " + temperature + " degrees Celsius at " + time;
		this.fulfillment = temperature;
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
