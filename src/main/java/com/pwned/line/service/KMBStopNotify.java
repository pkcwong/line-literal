package com.pwned.line.service;
import com.mongodb.BasicDBObject;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import java.util.ArrayList;

/***
 * Service for course information.
 * Required params: [uid]
 * Reserved tokens: [@kmb::stop]
 * Resolved params: []
 * @author Timothy Pak
 */

public class KMBStopNotify extends DefaultService{

	/**
	 * Constructor
	 * @param service
	 */
	public KMBStopNotify(Service service){
		super(service);
	}

	/**
	 * Payload of Service module.
	 * @throws Exception Exception
	 */
	@Override
	public void payload() throws Exception{
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
		this.fulfillment = stopNotification(mongo, SELF);
	}

	/**
	 *
	 * @param mongo Database
	 * @param SELF User's uid
	 * @return Reply whether will stop the service for the user
	 */
	public static String stopNotification(MongoDB mongo, BasicDBObject SELF){
		String stop = "You are not in our list of notification.";
		ArrayList<Document> kmb = MongoDB.get(mongo.getCollection("kmb").find(SELF));
		if(kmb.size() != 0){
			mongo.getCollection("kmb").deleteOne(SELF);
			stop = "Ok. We will stop notify you about bus arrival time.";
		}
		return stop;
	}

	/**
	 * Request processing from next Service module.
	 * @return Service state
	 * @throws Exception Exception
	 */
	@Override
	public Service chain() throws Exception{
		return this;
	}
}
