package com.pwned.line.service;

import com.pwned.line.http.HTTP;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * LiftAdvisor API
 * Required params: [parameters]
 * Reserved tokens: [@LiftAdvisor]
 * Resolved params: []
 * @author Christopher Wong
 */
public class LiftAdvisor extends DefaultService {

	public LiftAdvisor(Service service) {
		super(service);
	}

	/**
	 * Sends a request to LiftAdvisor php server.
	 * @throws Exception Exception
	 */
	@Override
	public void payload() throws Exception {
		JSONObject apiParams = new JSONObject(this.getParam("parameters").toString());
		String location = apiParams.getString("location");
		HTTP http = new HTTP("http://pathadvisor.ust.hk/phplib/search.php");
		http.setParams("keyword", location);
		http.setParams("type", "lift");
		String response = http.get();
		Pattern regex = Pattern.compile("liftLIFT\\s(.+?);(.+)");
		Matcher matcher = regex.matcher(response);
		String lift = "";
		while (matcher.find()) {
			lift = matcher.group(1);
		}
		this.fulfillment = this.fulfillment.replace("@LiftAdvisor", lift);
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

}
