package com.pwned.line.job;

import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.http.HTTP;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.quartz.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
public class PushNineDaysWeather extends DefaultJob{
    public static ArrayList<Document> usersArrayList;
    public PushNineDaysWeather() {

    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("PushNineDaysWeather");
        this.NineDaysWeather("hi");
    }


    public static JobDetail buildJob(Class <? extends Job> job) {
        return JobBuilder.newJob(PushWeather.class).build();
    }

    public static Trigger buildTrigger(int seconds) {
        return TriggerBuilder
                .newTrigger()
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(seconds).repeatForever())
                .build();
    }

    public static void NineDaysWeather(String uid) {
        usersArrayList = MongoDB.get(new MongoDB(System.getenv("MONGODB_URI")).getCollection("nine").find());

        String link = "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm";
        String[] text = {"http://www.weather.gov.hk", "<img border=\"0\" src=\"", "text-align:left;padding: 0px;font-size:100%;", "</div>"};
        HTTP http = new HTTP(link);
        String ninedaysweather = http.get();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd E");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Hong_Kong"));
        Date today = new Date();
        String[] date = new String[9];
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(today);
        for(int dd = 0; dd < 9; dd++){
            c.add(Calendar.DATE, 1);
            Date nextday = c.getTime();
            date[dd] = dateFormat.format(nextday);
        }
        String imageurlstring = ninedaysweather;
        String[] imageurl = new String[9];
        for (int url = 0; url < 9; url++){
            imageurlstring = imageurlstring.substring(imageurlstring.indexOf(text[1]));
            imageurl[url] = text[0] + imageurlstring.substring(imageurlstring.indexOf("/"), imageurlstring.indexOf("/") + 24);
            imageurlstring = imageurlstring.substring(2);
        }
        String desriptionsstring = ninedaysweather;
        String[] desription = new String[9];
        for(int weather = 0; weather < 9; weather++){
            desriptionsstring = desriptionsstring.substring(desriptionsstring.indexOf(text[2]));
            desription[weather] = desriptionsstring.substring(text[2].length() + 4, desriptionsstring.indexOf(text[3]));
            desriptionsstring = desriptionsstring.substring(2);
        }
        for(int i = 0; i < 20; i++)
        System.out.println("hi");
        String imageUrl = "/static/buttons/1040.jpg";
        CarouselTemplate carouselTemplate = new CarouselTemplate(
                Arrays.asList(
                        new CarouselColumn(imageUrl, "Hi", "Hi", Arrays.asList(
                                new MessageAction("Link",
                                        "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
                        ))
//                        new CarouselColumn(imageurl[0], date[0], desription[0], Arrays.asList(
//                                new MessageAction("Link",
//                                        "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
//                        )),
//                        new CarouselColumn(imageurl[1], date[1], desription[1], Arrays.asList(
//                                new MessageAction("Link",
//                                        "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
//                        )),
//                        new CarouselColumn(imageurl[2], date[2], desription[2], Arrays.asList(
//                                new MessageAction("Link",
//                                        "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
//                        )),
//                        new CarouselColumn(imageurl[3], date[3], desription[3], Arrays.asList(
//                                new MessageAction("Link",
//                                        "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
//                        )),
//                        new CarouselColumn(imageurl[4], date[4], desription[4], Arrays.asList(
//                                new MessageAction("Link",
//                                        "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
//                        )),
//                        new CarouselColumn(imageurl[5], date[5], desription[5], Arrays.asList(
//                                new MessageAction("Link",
//                                        "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
//                        )),
//                        new CarouselColumn(imageurl[6], date[6], desription[6], Arrays.asList(
//                                new MessageAction("Link",
//                                        "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
//                        )),
//                        new CarouselColumn(imageurl[7], date[7], desription[7], Arrays.asList(
//                                new MessageAction("Link",
//                                        "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
//                        )),
//                        new CarouselColumn(imageurl[8], date[7], desription[7], Arrays.asList(
//                                new MessageAction("Link",
//                                        "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
//                        ))
                ));
        TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate);
        KitchenSinkController.push(uid, templateMessage);
    }
}