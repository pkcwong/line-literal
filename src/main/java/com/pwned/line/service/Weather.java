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
 * Reserved tokens: [@weather::weather]
 * Resolved params: []
 * @author Timothy Pak
 */

public class Weather extends DefaultService{


	public Weather(Service service) {
		super(service);
	}
	@Override
	public void payload() throws Exception {
		String link = "http://www.hko.gov.hk/wxinfo/currwx/flw.htm";
		HTTP http = new HTTP(link);
		String weather = http.get();
		String[] messages = {"Weather forecast", "<br/><br/>Outlook"};
		weather = weather.substring(weather.indexOf(messages[0]), weather.indexOf(messages[1]));
		weather = weather.replace("<br/>", "\n");
		while (weather.contains("<")){
			weather = weather.substring(0, weather.indexOf("<")) + weather.substring(weather.indexOf(">") + 1);
		}
		this.fulfillment = this.fulfillment.replace("@weather::weather", weather);
	}


	@Override
	public Service chain() throws Exception {
		return this;
	}
}
