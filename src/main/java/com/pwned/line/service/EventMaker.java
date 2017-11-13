package com.pwned.line.service;

import com.pwned.line.handler.JoinHandler;
import com.pwned.line.http.HTTP;

/***
 * Service for event maker.
 * Required params: [params]
 * Reserved tokens: []
 * Resolved params: []
 * @author Bear
 */

public class EventMaker extends DefaultService{
	private String URI = "https://api.line.me/v2/bot/group/{groupId}/member/{userId}";
	private static final String ACCESS_TOKEN = System.getenv("LINE_BOT_CHANNEL_TOKEN");
	public EventMaker(Service service){
		super(service);
	}

	@Override
	public void payload() throws Exception{
		URI.replace("{groupId}", JoinHandler.getGroupId());
		URI.replace("{userId}",this.getParam("uid").toString());
		HTTP http = new HTTP(URI);
		http.setHeaders("Authorization", "Bearer " + ACCESS_TOKEN);
		System.out.println("Result of http get" + http.get());


	}


	@Override
	public Service chain() throws Exception {
		return this;
	}
}
