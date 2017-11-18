package com.pwned.line.job;

import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.service.KMB;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.*;

import java.util.ArrayList;

public class PushKMB extends DefaultJob{

    public static ArrayList<Document> KMBArrayList;

    public PushKMB(){
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException{
        System.out.println("PushKMB");
        try {
            this.updateKMB();
        }
        catch (JSONException e){
            throw new JobExecutionException();
        }
    }


    public static JobDetail buildJob(Class <? extends Job> job){
        return JobBuilder.newJob(PushKMB.class).build();
    }

    public static Trigger buildTrigger(int seconds){
        return TriggerBuilder
                .newTrigger()
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(seconds).repeatForever())
                .build();
    }

    public static void updateKMB() throws JSONException{
        String kmb = "The following bus will arrive soon:";
        MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
        KMBArrayList = MongoDB.get(mongo.getCollection("kmb").find());
        String etasecondNorth91 = KMB.getBusETA("KMBBus91", "HKUST North Gate");
        if(etasecondNorth91 == null){
            etasecondNorth91 = "601";
        }
        String etasecondSouth91 = KMB.getBusETA("KMBBus91", "HKUST South Gate");
        if(etasecondSouth91 == null){
            etasecondSouth91 = "601";
        }
        String etasecondSouth91M = KMB.getBusETA("KMBBus91M", "HKUST South Gate");
        if(etasecondSouth91 == null){
            etasecondSouth91 = "601";
        }
        if(Integer.parseInt(etasecondNorth91) > 600 && Integer.parseInt(etasecondSouth91) > 600 && Integer.parseInt(etasecondSouth91M) > 600){

        }else{
            if(Integer.parseInt(etasecondNorth91) <= 600){
                kmb = kmb + "\nKMB 91 will arrive at HKUST North Gate in " + Integer.parseInt(etasecondNorth91) / 60 + " minutes";
            }
            if(Integer.parseInt(etasecondSouth91) <= 600){
                kmb = kmb + "\nKMB 91 will arrive at HKUST South Gate in " + Integer.parseInt(etasecondSouth91) / 60 + " minutes";
            }
            if(Integer.parseInt(etasecondSouth91M) <= 600){
                kmb = kmb + "\nKMB 91M will arrive at HKUST South Gate in " + Integer.parseInt(etasecondSouth91M) / 60 + " minutes";
            }
            pushKMB(kmb);
        }
    }

    public static void pushKMB(String kmb) throws JSONException{
        for (int i = 0; i < KMBArrayList.size(); i++) {
            KitchenSinkController.push(new JSONObject(KMBArrayList.get(i).toJson()).getString("uid"), new TextMessage(kmb));
        }
    }
}
