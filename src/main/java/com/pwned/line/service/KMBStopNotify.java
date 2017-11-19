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

	public KMBStopNotify(Service service){
		super(service);
	}

	@Override
	public void payload() throws Exception{
		this.fulfillment = this.fulfillment.replace("@kmb::stop", "You are not in our list of notification.");
		MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
		for(int i = 0; i < 20; i++)
			System.out.println("1st");
		BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
		for(int i = 0; i < 20; i++)
			System.out.println("2nd");
		ArrayList<Document> kmb = MongoDB.get(mongo.getCollection("kmb").find(SELF));
		for(int i = 0; i < 20; i++)
			System.out.println("3rd");
		if(kmb.size() != 0){
			for(int i = 0; i < 20; i++)
				System.out.println("4th");
			mongo.getCollection("user").deleteOne(SELF);
			this.fulfillment = this.fulfillment.replace("@kmb::stop", "Ok. We will stop notify you about bus arrival time.");
		}

	}

	@Override
	public Service chain() throws Exception{
		return this;
	}
}
