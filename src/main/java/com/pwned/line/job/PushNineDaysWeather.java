package com.pwned.line.job;

import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.*;

import java.util.ArrayList;
import java.util.Arrays;

public class PushNineDaysWeather extends DefaultJob{
    public static ArrayList<Document> usersArrayList;
    public PushNineDaysWeather() {

    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("PushNineDaysWeather");
        this.NineDaysWeather();
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

    public static void NineDaysWeather() {
        usersArrayList = MongoDB.get(new MongoDB(System.getenv("MONGODB_URI")).getCollection("user").find());
        String imageUrl = "https://line.me";
        CarouselTemplate carouselTemplate = new CarouselTemplate(
                Arrays.asList(
                        new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                new URIAction("Go to line.me",
                                        "https://line.me")
                        )),
                        new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                new PostbackAction("言 hello2",
                                        "hello こんにちは",
                                        "hello こんにちは")
                        )),
                        new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                new PostbackAction("言 hello3",
                                        "hello こんにちは",
                                        "hello こんにちは")
                        )),
                        new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                new PostbackAction("言 hello4",
                                        "hello こんにちは",
                                        "hello こんにちは")
                        )),
                        new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                new PostbackAction("言 hello5",
                                        "hello こんにちは",
                                        "hello こんにちは")
                        )),
                        new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                new PostbackAction("言 hello6",
                                        "hello こんにちは",
                                        "hello こんにちは")
                        )),
                        new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                new PostbackAction("言 hello7",
                                        "hello こんにちは",
                                        "hello こんにちは")
                        )),
                        new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                new PostbackAction("言 hello8",
                                        "hello こんにちは",
                                        "hello こんにちは")
                        )),
                        new CarouselColumn(imageUrl, "Datetime Picker", "Please select a date, time or datetime", Arrays.asList(
                                new DatetimePickerAction("Datetime",
                                        "action=sel",
                                        "datetime",
                                        "2017-06-18T06:15",
                                        "2100-12-31T23:59",
                                        "1900-01-01T00:00")
                        ))
                ));
        TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate);
        for (int i = 0; i < usersArrayList.size(); i++) {
            try{
                KitchenSinkController.push(new JSONObject(usersArrayList.get(i).toJson()).getString("uid"), templateMessage);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}