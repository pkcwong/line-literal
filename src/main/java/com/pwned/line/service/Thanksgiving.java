package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.pwned.line.http.HTTP;
import com.pwned.line.web.MongoDB;
import org.bson.Document;

import javax.print.Doc;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/***
 * Accept/Bring food for thanksgiving
 * Required params: []
 * Reserved tokens: []
 * Resolved params: []
 * @author Bear
 */

public class Thanksgiving extends DefaultService{
	private String keyword;
	private static String userURI = "https://api.line.me/v2/bot/profile/";
	private static String ACCESS_TOKEN = System.getenv("LINE_BOT_CHANNEL_TOKEN");

	public Thanksgiving(Service service, String key){
		super(service);
		keyword = key;
	}

	@Override
	public void payload(){
		if(keyword.contains("accept")){
			// Date of party
			Calendar partyDate = Calendar.getInstance();
			partyDate.set(2017,Calendar.NOVEMBER,23);
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String formatted = format1.format(partyDate.getTime());



			MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
			String uid = this.getParam("uid").toString();
			BasicDBObject SELF = new BasicDBObject().append("uid", uid);
			ArrayList<Document> user = MongoDB.get(mongo.getCollection("party").find(SELF));



			if (user.get(0).getString("Accept").equals("N")) {
				mongo.getCollection("party").findOneAndUpdate(new BasicDBObject().append("uid", uid),new BasicDBObject("Accept", "Y"));
				this.fulfillment = "Thank you for your join, " + getName(uid) + "! Have a fun night! See you on " + formatted;
			} else {
				this.fulfillment = "Already accept the party. Please be reminded that the party will be held on " + formatted;
			}
		}

		else if(keyword.contains("bring")){
			String[] keywordArray = keyword.split(" ");
			MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));

			BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
			ArrayList<Document> user = MongoDB.get(mongo.getCollection("party").find(SELF));

			if (user.size() == 0) {
				this.fulfillment = "You haven't accept the party invitation! Please enter Accpet to join the party!";
				return;
			}

			else
			{

				for(int i = 1 ; i < keywordArray.length; i++){
					BasicDBObject FOOD = new BasicDBObject().append("type", keywordArray[i].toLowerCase());
					ArrayList<Document> food = MongoDB.get(mongo.getCollection("food").find(FOOD));
					if(food.size() == 0){
						Document data = new Document();
						data.append("type", keywordArray[i].toLowerCase());
						mongo.getCollection("food").insertOne(data);
					}
					else{
						this.fulfillment = "Someone is bringing " + keywordArray[i].toLowerCase() + " already, can you pick another one?";
						return;
					}
				}
				this.fulfillment = "Great, please prepare 5 people portion of that.";
			}
		}
	}

	public static String getName(String uid){
		HTTP http = new HTTP(userURI + uid);
		http.setHeaders("Authorization", "Bearer " + ACCESS_TOKEN);
		String response = http.get();
		Pattern regex = Pattern.compile("\"displayName\":\"(.+?)\"");
		Matcher matcher = regex.matcher(response);
		String name = "";
		while (matcher.find()) {
			name = matcher.group(1);
		}
		return name;
	}



	@Override
	public Service chain(){
		return this;
	}

}
