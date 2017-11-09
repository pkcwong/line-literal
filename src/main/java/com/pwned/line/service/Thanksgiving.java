package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.pwned.line.http.HTTP;
import com.pwned.line.web.MongoDB;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/***
 * Accept/Bring food for thanksgiving
 * Required params: []
 * Reserved tokens: []
 * Resolved params: []
 * @author Bear
 */

public class Thanksgiving extends DefaultService{
	private String keyword;
	private String URI = "https://api.line.me/v2/oauth/accessToken";
	private String userURI = "https://api.line.me/v2/profile";
	private String refresh_token = "";
	private String client_id = "1535457737";
	private String client_secret = "56ce7e4d745a529be93647b1009e295c";
	private String access_token = "rtVsL+Y9jhge/qrRdsgKK2AbvX/t5z4ESVvV9j+cBYvh27KVB15tek0rfIHB3TrLg2rrjb/4VKa1Eck4RvUfqpIsaCwIV/MJ69s2wfuksvX+9acs/QRjx2a0PHci6ESM8HxdsGo1Zb7T31TsglQbuQdB04t89/1O/w1cDnyilFU=";

	public Thanksgiving(Service service, String key){
		super(service);
		keyword = key;
	}

	@Override
	public void payload(){
		if(keyword.contains("accept")){
			// Date of party
			Calendar partyDate = Calendar.getInstance();
			partyDate.set(2017,Calendar.NOVEMBER,27);
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String formatted = format1.format(partyDate.getTime());

			/*HTTP http = new HTTP(URI);
			http.setHeaders("Content-Type","application/x-www-form-urlencoded");
			http.setParams("grant_type", "refresh_token");
			http.setParams("client_id",client_id);
			http.setParams("client_secret",client_secret);*/
			HTTP http = new HTTP(userURI);
			http.setHeaders("Authorization", "Bearer " + access_token);
			System.out.println("Result of http get: " + http.get());


			String[] arrayKeyword = keyword.split(" ");

			MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));

			BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
			ArrayList<Document> user = MongoDB.get(mongo.getCollection("party").find(SELF));

			if (user.size() == 0) {
				if(!keyword.contains("by") || !keyword.contains(" ")){
					this.fulfillment = "Thank you for your join! But please let us know who are you! :)\n" +
							"For example, accept by Bear";
					return;
				}
				Document data = new Document();
				data.append("uid", this.getParam("uid").toString());
				if(arrayKeyword[1].equals("by"))
					data.append("name", arrayKeyword[2]);
				else
					data.append("name", arrayKeyword[1]);
				mongo.getCollection("party").insertOne(data);
				this.fulfillment = "Thank you for your join! Have a fun night! See you on " + formatted;
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
				this.fulfillment = "You haven't accept the party invitation! Please enter Accpet by (your name) to join the party!";
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

	@Override
	public Service chain(){
		return this;
	}

}
