package com.pwned.line.service;

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
 * Reserved tokens: [@weather::ninedaysweather]
 * Resolved params: []
 * @author Timothy Pak
 */

public class NineDaysWeather extends DefaultService{


    public NineDaysWeather(Service service) {
        super(service);
    }
    @Override
    public void payload() throws Exception {
        String link = "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm";
        String[] text = {"<td style=\"width: 65px; padding: 2px;\" class=\"evenCol\">"};
        HTTP http = new HTTP(link);
        String ninedaysweather = http.get();
        String[] date = new String[9];
        date[0] = ninedaysweather.substring(ninedaysweather.indexOf(text[0]));
        CarouselTemplate ninedays;
        List<CarouselColumn> nineColumns = new ArrayList<CarouselColumn>();
        for(int days = 0; days < 9; days++){
            nineColumns.set(days, new CarouselColumn("", "", "", null));
        }
        ninedays = new CarouselTemplate(nineColumns);
//        String[] messages = {"Weather forecast", "<br/><br/>Outlook"};
//        weather = weather.substring(weather.indexOf(messages[0]), weather.indexOf(messages[1]));
//        weather = weather.replace("<br/>", "\n");
//        while (weather.contains("<")){
//            weather = weather.substring(0, weather.indexOf("<")) + weather.substring(weather.indexOf(">") + 1);
//        }
        this.fulfillment = this.fulfillment.replace("@weather::ninedaysweather", "The following is the weather forecast for next 9 days:");
        KitchenSinkController.push(this.getParam("uid").toString(), new TemplateMessage("Nine Days Weather Forecast", ninedays));
    }


    @Override
    public Service chain() throws Exception {
        return this;
    }
}
