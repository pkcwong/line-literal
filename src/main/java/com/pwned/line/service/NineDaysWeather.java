package com.pwned.line.service;

import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.http.HTTP;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
        String[] text = {"http://www.weather.gov.hk/", "<img border=\"0\" src=\"", "<div style=\"text-align:left;padding: 0px;font-size:100%;\">\n", "</div>"};
        HTTP http = new HTTP(link);
        String ninedaysweather = http.get();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd E");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Hong_Kong"));
        Date today = new Date();
        String[] date = new String[9];
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        for(int dd = 0; dd < 9; dd++){
            c.add(Calendar.DATE, 1);
            Date nextday = c.getTime();
            date[dd] = dateFormat.format(nextday);
        }
        for(int i = 0; i < 20; i++)
        System.out.println("|||ImageURL|||");
        String imageurlstring = ninedaysweather;
        String[] imageurl = new String[9];
        for (int url = 0; url < 9; url++){
            imageurlstring = imageurlstring.substring(imageurlstring.indexOf(text[1]));
            imageurl[url] = text[0] + imageurlstring.substring(imageurlstring.indexOf("/"), imageurlstring.indexOf("/") + 25);
            System.out.println(imageurl[url]);
            System.out.println(imageurl[url]);
        }
        for(int i = 0; i < 20; i++)
            System.out.println("|||Desription|||");
        String desriptionsstring = ninedaysweather;
        String[] desription = new String[9];
        for(int weather = 0; weather < 9; weather++){
            desriptionsstring = desriptionsstring.substring(desriptionsstring.indexOf(text[2]));
            desription[weather] = desriptionsstring.substring(text[2].length(), desriptionsstring.indexOf(text[3]));
            System.out.println(desription[weather]);
            System.out.println(desription[weather]);
        }
        for(int i = 0; i < 20; i++)
            System.out.println("|||Carousel|||");
        CarouselTemplate ninedays;
        List<CarouselColumn> nineColumns = new ArrayList<CarouselColumn>();
        for(int days = 0; days < 9; days++){
            nineColumns.set(days, new CarouselColumn(imageurl[days], date[days], desription[days], null));
        }
        ninedays = new CarouselTemplate(nineColumns);
        this.fulfillment = this.fulfillment.replace("@weather::ninedaysweather", "The following is the weather forecast for next 9 days:");
        KitchenSinkController.push(this.getParam("uid").toString(), new TemplateMessage("Nine Days Weather Forecast", ninedays));
    }


    @Override
    public Service chain() throws Exception {
        return this;
    }
}
