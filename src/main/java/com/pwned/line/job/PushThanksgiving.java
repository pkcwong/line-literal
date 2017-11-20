package com.pwned.line.job;

import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.mongodb.BasicDBObject;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.http.HTTP;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/***
 * Pushing poster
 * Required params: []
 * Reserved tokens: []
 * Resolved params: []
 * @author Bear
 */
public class PushThanksgiving extends DefaultJob{
	public static String userURI = "https://api.line.me/v2/bot/profile/";
	public static String ACCESS_TOKEN = System.getenv("LINE_BOT_CHANNEL_TOKEN");
	public static ArrayList<Document> usersArrayList = new ArrayList<>();
	public static ArrayList<Document> acceptedUsersArrayList = new ArrayList<>();
	private static String imageURI = "https://i.pinimg.com/736x/bc/bb/40/bcbb405562b44357e48c84eeadcd6d9b--thanksgiving--thanksgiving-decorations.jpg";

	/**
	 *
	 * @param context
	 * @throws JobExecutionException
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException{
		try {
			this.pushThanksgiving();
		} catch (JSONException e) {
			throw new JobExecutionException();
		}
	}

	/**
	 *
	 * @param hours Number of hours before each push
	 * @return
	 */
	public static Trigger buildTrigger(int hours){
		return TriggerBuilder
				.newTrigger()
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInHours(hours).repeatForever())
				.build();
	}

	/**
	 *
	 * @param job
	 * @return
	 */
	public static JobDetail buildJob(Class <? extends Job> job){
		return JobBuilder.newJob(PushThanksgiving.class).build();
	}

	/**
	 * Push thanksgiving invitation to users
	 * @throws JSONException
	 */
	public static void pushThanksgiving() throws JSONException{
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		addUserToParty(mongo);
		usersArrayList = MongoDB.get(mongo.getCollection("party").find());
		ArrayList<String> uid = new ArrayList<>();
		ArrayList<String> acceptedUid = new ArrayList<>();
		StringBuilder acceptedName = new StringBuilder();
		for(int i = 0 ; i < usersArrayList.size(); i++){
			uid.add(new JSONObject(usersArrayList.get(i).toJson()).getString("uid"));
			if(usersArrayList.get(i).getString("Accept").equals("Y")) {
				acceptedUsersArrayList.add(usersArrayList.get(i));
				acceptedUid.add(new JSONObject(usersArrayList.get(i).toJson()).getString("uid"));
				acceptedName.append((new JSONObject(usersArrayList.get(i).toJson()).getString("name")).toUpperCase());
				acceptedName.append(", ");
			}
		}
		if(acceptedName.length() > 2){
			acceptedName.delete(acceptedName.length() - 2, acceptedName.length() - 1);
		}
		Calendar today = Calendar.getInstance();
		Calendar partyDate = Calendar.getInstance();
		partyDate.set(2017,Calendar.NOVEMBER,22);
		//Today.set(2017,Calendar.NOVEMBER,22);
		today.add(Calendar.HOUR,8);
		partyDate.add(Calendar.HOUR,8);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = format1.format(today.getTime());
		for (int i = 0; i < usersArrayList.size(); i++) {
			if (!checkPushed(mongo, uid.get(i), today)) {
				if (acceptedUid.contains(uid.get(i).toString())) {
					if (checkSameDate(today, partyDate)) {
						KitchenSinkController.push(uid.get(i), new TextMessage("Remember to join the party tomorrow! " + acceptedName.toString() + " will join also!"));
					}
				} else{
					KitchenSinkController.push(uid.get(i).toString(), new ImageMessage(imageURI, imageURI));
				}
				Document data = new Document();
				data.append("Date", formatted);
				BasicDBObject SELF = new BasicDBObject().append("uid", uid.get(i));
				mongo.getCollection("party").findOneAndUpdate(SELF, new BasicDBObject("$addToSet", data));
			}else
				continue;
		}

	}

	/**
	 * Check if the two dates entered are the same date
	 * @param cal1 date one
	 * @param cal2 date two
	 * @return true or false
	 */
	public static boolean checkSameDate(Calendar cal1, Calendar cal2){
		if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
				cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Check if the user has received a push notification or not today
	 * @param mongo Database
	 * @param uid uid of user
	 * @param today date of today
	 * @return has the user been pushed today
	 * @throws JSONException
	 */
	public static boolean checkPushed(MongoDB mongo, String uid, Calendar today) throws JSONException {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = format1.format(today.getTime());
		BasicDBObject SELF = new BasicDBObject().append("uid", uid);
		ArrayList<Document> user = MongoDB.get(mongo.getCollection("party").find(SELF));
		if(user.size() != 0) {
			JSONObject date = new JSONObject(user.get(0).toJson());
			if (!date.toString().contains("Date")) {
				return false;
			}
			for (int i = 0; i < date.getJSONArray("Date").length(); i++) {
				if (date.getJSONArray("Date").get(i).toString().equals(formatted)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Add users that are not in the group of party to to group, so they would receive invitation
	 * @param mongo Database
	 */
	public static void addUserToParty(MongoDB mongo) throws JSONException{
		usersArrayList = MongoDB.get(mongo.getCollection("user").find());
		for(int i = 0 ; i < usersArrayList.size(); i++){
			String uid = usersArrayList.get(i).getString("uid");
			BasicDBObject SELF = new BasicDBObject().append("uid", uid);
			ArrayList<Document> user = MongoDB.get(mongo.getCollection("party").find(SELF));
			if(user.size() == 0){
				Document data = new Document();
				data.append("uid", uid);
				data.append("name", getName(uid));
				data.append("Accept", "N");
				mongo.getCollection("party").insertOne(data);
			}
		}
	}

	/**
	 * Get user account name through uid
	 * @param uid User's uid
	 * @return User's account name
	 * @throws JSONException
	 */
	public static String getName(String uid) throws JSONException{
		HTTP http = new HTTP(userURI + uid);
		http.setHeaders("Authorization", "Bearer " + ACCESS_TOKEN);
		String response = http.get();
		if(response != null) {
			return new JSONObject(response).getString("displayName");
		}
		return "";
	}
}
