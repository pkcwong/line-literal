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

    /**
     * Constructor
     */
    public PushKMB(){
    }

	/**
	 *
	 * @param context
	 * @throws JobExecutionException
	 */
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

	/**
	 *
	 * @param job
	 * @return
	 */
    public static JobDetail buildJob(Class <? extends Job> job){
        return JobBuilder.newJob(PushKMB.class).build();
    }

	/**
	 *
	 * @param seconds Time of next trigger in seconds
	 * @return
	 */
	public static Trigger buildTrigger(int seconds){
        return TriggerBuilder
                .newTrigger()
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(seconds).repeatForever())
                .build();
    }

	/**
	 * Check if any bus is arriving in 10 minutes, if yes, call pushKMB()
	 * @throws JSONException
	 */
	public static void updateKMB() throws JSONException{
        String kmb = "The following bus will arrive soon:";
        MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
        KMBArrayList = MongoDB.get(mongo.getCollection("kmb").find());
        String etaSecondNorth91 = KMB.getBusETA("KMBBus91", "HKUST North Gate");
        if(etaSecondNorth91 == null){
            etaSecondNorth91 = "601";
        }
        String etaSecondSouth91 = KMB.getBusETA("KMBBus91", "HKUST South Gate");
        if(etaSecondSouth91 == null){
            etaSecondSouth91 = "601";
        }
        String etaSecondSouth91M = KMB.getBusETA("KMBBus91M", "HKUST South Gate");
        if(etaSecondSouth91M == null){
            etaSecondSouth91M = "601";
        }
        if(Integer.parseInt(etaSecondNorth91) > 600 && Integer.parseInt(etaSecondSouth91) > 600 && Integer.parseInt(etaSecondSouth91M) > 600){

        }else{
            if(Integer.parseInt(etaSecondNorth91) <= 600){
                kmb = kmb + "\nKMB 91 will arrive at HKUST North Gate in " + Integer.parseInt(etaSecondNorth91) / 60 + " minutes";
            }
            if(Integer.parseInt(etaSecondSouth91) <= 600){
                kmb = kmb + "\nKMB 91 will arrive at HKUST South Gate in " + Integer.parseInt(etaSecondSouth91) / 60 + " minutes";
            }
            if(Integer.parseInt(etaSecondSouth91M) <= 600){
                kmb = kmb + "\nKMB 91M will arrive at HKUST South Gate in " + Integer.parseInt(etaSecondSouth91M) / 60 + " minutes";
            }
            pushKMB(kmb);
        }
    }

	/**
	 * Push notification to user that has subscribed
	 * @param kmb Users' uid list
	 * @throws JSONException
	 */
	public static void pushKMB(String kmb) throws JSONException{
        for (int i = 0; i < KMBArrayList.size(); i++) {
            KitchenSinkController.push(new JSONObject(KMBArrayList.get(i).toJson()).getString("uid"), new TextMessage(kmb));
        }
    }
}
