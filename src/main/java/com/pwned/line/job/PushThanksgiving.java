package com.pwned.line.job;

import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


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
	public static Trigger buildTrigger(int hours) {
		return TriggerBuilder
				.newTrigger()
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInHours(hours).repeatForever())
				.build();
	}

	public static JobDetail buildJob(Class <? extends Job> job) {
		return JobBuilder.newJob(PushThanksgiving.class).build();
	}

	public static void pushThanksgiving() throws JSONException {
		String imageURI = "https://cdn.frip.in/wp-content/uploads/2013/11/Thanksgiving-Day-Party-Poster.jpg";
		usersArrayList = MongoDB.get(new MongoDB(System.getenv("MONGODB_URI")).getCollection("user").find());
		acceptedUsersArrayList = MongoDB.get(new MongoDB(System.getenv("MONGODB_URI")).getCollection("party").find());

		ArrayList<String> uid = new ArrayList<>();
		ArrayList<String> acceptedUid = new ArrayList<>();
		StringBuilder acceptedName = new StringBuilder();

		for (int i = 0; i < usersArrayList.size(); i++) {
			uid.add(new JSONObject(usersArrayList.get(i).toJson()).getString("uid"));
		}
		for (int i = 0; i < acceptedUsersArrayList.size(); i++) {
			acceptedUid.add(new JSONObject(acceptedUsersArrayList.get(i).toJson()).getString("uid"));
			acceptedName.append((new JSONObject(acceptedUsersArrayList.get(i).toJson()).getString("name")).toUpperCase());
			acceptedName.append(" ");
		}

		Calendar today = Calendar.getInstance();
		Calendar partyDate = Calendar.getInstance();
		partyDate.set(2017,Calendar.NOVEMBER,26);
		today.add(Calendar.HOUR,8);

		for (int i = 0; i < usersArrayList.size(); i++) {
			if(acceptedUid.contains(uid.get(i).toString()))
			{
				if(checkSameDate(today,partyDate)){
					KitchenSinkController.push(uid.get(i), new TextMessage("Remember to join the party tomorrow! " + acceptedName.toString() + "will join also!"));
				}
			}
			else
				KitchenSinkController.push(uid.get(i).toString(), new ImageMessage(imageURI, imageURI));
		}

	}

	private static boolean checkSameDate(Calendar cal1, Calendar cal2){
		if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
				cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)){
			return true;
		}else{
			System.out.printf("The time of Today is %s and the partyDate before is %s\n", cal1.toString(),cal2.toString());
			return false;
		}


	}
}
