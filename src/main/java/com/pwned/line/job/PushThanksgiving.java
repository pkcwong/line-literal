package com.pwned.line.job;

import com.linecorp.bot.model.message.ImageMessage;
import com.mongodb.BasicDBObject;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.*;

import java.util.ArrayList;
/***
 * Pushing poster
 * Required params: []
 * Reserved tokens: []
 * Resolved params: []
 * @author Bear
 */
public class PushThanksgiving extends DefaultJob{
	public static ArrayList<Document> usersArrayList;
	public static ArrayList<Document> acceptedUsersArrayList;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			this.pushThanksgiving();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public static Trigger buildTrigger(int seconds) {
		return TriggerBuilder
				.newTrigger()
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInSeconds(seconds).repeatForever())
				.build();
	}

	public static JobDetail buildJob(Class <? extends Job> job) {
		return JobBuilder.newJob(PushThanksgiving.class).build();
	}

	public static void pushThanksgiving() throws JSONException {
		String imageURI = "https://cdn.frip.in/wp-content/uploads/2013/11/Thanksgiving-Day-Party-Poster.jpg";
		usersArrayList = MongoDB.get(new MongoDB(System.getenv("MONGODB_URI")).getCollection("user").find());
		acceptedUsersArrayList = MongoDB.get(new MongoDB(System.getenv("MONGODB_URI")).getCollection("party").find());

		for (int i = 0; i < usersArrayList.size(); i++) {
			for(int j = 0 ; j < acceptedUsersArrayList.size(); j++)
				if((new JSONObject(usersArrayList.get(i).toJson()).getString("uid")) == (new JSONObject(acceptedUsersArrayList.get(i).toJson()).getString("uid"))) {
					KitchenSinkController.push(new JSONObject(usersArrayList.get(i).toJson()).getString("uid"), new ImageMessage(imageURI, imageURI));
				}
		}
	}
}
