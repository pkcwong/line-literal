package com.pwned.line.service;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.http.HTTP;

import java.util.ArrayList;
import java.util.List;

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
		String url = "http://www.weather.gov.hk/images/wxicon/pic62.png";
		ArrayList<CarouselColumn> nineColumns = new ArrayList<>();
		List<Action> actions = new ArrayList<>();
		actions.add(new MessageAction("hi", "hi"));
		nineColumns.add(new CarouselColumn(url, "title", "Testing", actions));
		for(int i = 0; i < 20; i++)
		System.out.println("Before push");
		KitchenSinkController.push(this.getParam("uid").toString(), new TemplateMessage("Carousel Template", new CarouselTemplate(nineColumns)));
		for(int i = 0; i < 20; i++)
		System.out.println("After push");
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
