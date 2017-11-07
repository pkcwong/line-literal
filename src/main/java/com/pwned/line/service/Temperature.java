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

public class Temperature extends DefaultService{


	public Temperature(Service service) {
		super(service);
	}
	@Override
	public void payload() throws Exception {

		String city = new JSONObject(this.getParam("parameters").toString()).getString("Region1");
		String link = "http://rss.weather.gov.hk/rss/CurrentWeather.xml";
		HTTP http = new HTTP(link);
		String temperatureString = http.get();
		String temperature = "";
		if(temperatureString.contains(city)){
			temperature = temperatureString.substring(temperatureString.indexOf(city) + city.length() + 58, temperatureString.indexOf(city) + city.length() + 60);
			temperature = temperature + "°C";
		}else{
			if(temperatureString.indexOf("degrees Celsius<br/>") - 1 > temperatureString.indexOf("Air temperature : ") + 18){
				temperature = temperatureString.substring(temperatureString.indexOf("Air temperature : ") + 18, temperatureString.indexOf("degrees Celsius<br/>") - 1);
				temperature = temperature + "°C";
			}else{
				temperature = (temperatureString.indexOf("degrees Celsius<br/>") - 1) + " " + (temperatureString.indexOf("Air temperature : ") + 18);
			}
		}

		if(temperature == ""){
			temperature = "not available";
		}
		temperature = "The temperature at " + city + " is " + temperature + ".";
		this.fulfillment = this.fulfillment.replace("@weather::temperature", temperature);
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}
}
