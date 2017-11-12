package com.pwned.line.job;

import com.linecorp.bot.model.action.PostbackAction;
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
        String[] fulldesription = new String[9];
        for(int weather = 0; weather < 9; weather++){
            desriptionsstring = desriptionsstring.substring(desriptionsstring.indexOf(text[2]));
            fulldesription[weather] = desriptionsstring.substring(text[2].length() + 4, desriptionsstring.indexOf(text[3]));
            desription[weather] = fulldesription[weather].substring(0, fulldesription[weather].indexOf("."));
            System.out.println(fulldesription[weather].length());
            desriptionsstring = desriptionsstring.substring(2);
        }
        for(int i = 0; i < 20; i++)
        System.out.println("hi");
        String imageUrl = "https://i.pinimg.com/originals/f1/60/60/f1606043ba4fd7f87a9951250541e6f4.jpg";
        for (int url = 0; url < 9; url++){
            System.out.println(imageurl[url]);
        }
        for (int url = 0; url < 9; url++){
            System.out.println(desription[url]);
        }
        CarouselTemplate carouselTemplate = new CarouselTemplate(
                Arrays.asList(
//                        new CarouselColumn(imageUrl, date[0], desription[0], Arrays.asList(
//                                new URIAction("Detail Weather",
//                                "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
//                        )),
                        new CarouselColumn(imageUrl, date[1], desription[1], Arrays.asList(
                                new PostbackAction("Detail Weather",
                                        fulldesription[1])
                        ))
//                        new CarouselColumn(imageUrl, date[2], desription[2], Arrays.asList(
//                                new URIAction("Detail Weather",
//                                        "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
//                        )),
//                        new CarouselColumn(imageUrl, date[3], desription[3], Arrays.asList(
//                                new URIAction("Detail Weather",
//                                        "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
//                        )),
//                        new CarouselColumn(imageUrl, date[4], desription[4], Arrays.asList(
//                                new URIAction("Detail Weather",
//                                        "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
//                        )),
//                        new CarouselColumn(imageUrl, date[5], desription[5], Arrays.asList(
//                                new URIAction("Detail Weather",
//                                        "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
//                        )),
//                        new CarouselColumn(imageUrl, date[6], desription[6], Arrays.asList(
//                                new URIAction("Detail Weather",
//                                        "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
//                        )),
//                        new CarouselColumn(imageUrl, date[7], desription[7], Arrays.asList(
//                                new URIAction("Detail Weather",
//                                        "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
//                        )),
//                        new CarouselColumn(imageUrl, date[8], desription[8], Arrays.asList(
//                                new URIAction("Detail Weather",
//                                        "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm")
//                        ))
                ));
        TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate);
        KitchenSinkController.push(uid, templateMessage);
    }
}