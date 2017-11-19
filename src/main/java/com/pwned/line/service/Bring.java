package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.pwned.line.web.MongoDB;
import org.bson.Document;

import java.util.ArrayList;

/**
 * Service for getting what user will bring for thanksgiving party.
 * Required params: [uid]
 * Reserved tokens: [@thanksgiving::bring]
 * Resolved params: []
 * @author Timothy Pak
 */

public class Bring extends DefaultService{

	/**
	 * Constructor
	 * @param service
	 */
	public Bring(Service service){
		super(service);
	}

	/**
	 * Payload of Service module.
	 * @throws Exception Exception
	 */
	@Override
	public void payload() throws Exception{
		System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||");
		this.fulfillment = this.fulfillment.replace("@thanksgiving::bring", bring(this.getParam("food").toString(), this.getParam("uid").toString()));
	}

	/**
	 * Adding what the user will bring to the party to the database
	 * @param bring What food will the user bring
	 * @param uid
	 * @return
	 */
	public static String bring(String bring, String uid){
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		BasicDBObject SELF = new BasicDBObject().append("uid", uid);
		ArrayList<Document> user = MongoDB.get(mongo.getCollection("party").find(SELF));
		if (user.get(0).getString("Accept").equals("N")) {
			return "You haven't accept the invitation to the party! Please enter Accpet to join the party!";
		}else{
			BasicDBObject FOOD = new BasicDBObject().append("type", bring.toLowerCase());
			ArrayList<Document> food = MongoDB.get(mongo.getCollection("food").find(FOOD));
			if(food.size() == 0){
				Document data = new Document();
				data.append("type", bring.toLowerCase());
				mongo.getCollection("food").insertOne(data);
				return "That's good! Please prepare five serves of " + bring + " to the party.";
			}else{
				return "Someone is bringing " + bring.toLowerCase() + " already, please bring another food.";
			}
		}
	}

	/**
	 * Request processing from next Service module.
	 * @return Service state
	 * @throws Exception Exception
	 */
	@Override
	public Service chain() throws Exception {
		return this;
	}
}
