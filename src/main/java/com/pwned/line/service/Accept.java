package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.pwned.line.job.PushThanksgiving;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Service for accepting thanksgiving party invitation.
 * Required params: [uid]
 * Reserved tokens: [@thanksgiving::accept]
 * Resolved params: []
 * @author Timothy Pak
 */

public class Accept extends DefaultService{

	/**
	 * Constructor
	 * @param service
	 */
	public Accept(Service service){
		super(service);
	}

	/**
	 * Payload of Service module.
	 * @throws Exception Exception
	 */
	@Override
	public void payload() throws Exception{
		this.fulfillment = this.fulfillment.replace("@thanksgiving::accept", accept(this.getParam("uid").toString()));
	}

	/**
	 * User accepts the invitation
	 * @param uid User uid
	 * @return Replying user
	 * @throws JSONException
	 */
	public static String accept(String uid) throws JSONException{
		Calendar partyDate = Calendar.getInstance();
		partyDate.set(2017, Calendar.NOVEMBER, 23);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(partyDate.getTime());
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		BasicDBObject SELF = new BasicDBObject().append("uid", uid);
		ArrayList<Document> user = MongoDB.get(mongo.getCollection("party").find(SELF));
		if(user.size() == 0) {
			PushThanksgiving.addUserToParty(mongo);
			user = MongoDB.get(mongo.getCollection("party").find(SELF));
		}
		if (user.get(0).getString("Accept").equals("N")) {
			mongo.getCollection("party").findOneAndUpdate(new BasicDBObject().append("uid", uid), new BasicDBObject("$set", new BasicDBObject().append("Accept", "Y")));
			return "Thank you for joining, " + PushThanksgiving.getName(uid) + "! See you on " + date + "!";
		} else {
			return "You have already accepted the invitation to the thanksgiving party. Please be reminded that the party will be held on " + date;
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
