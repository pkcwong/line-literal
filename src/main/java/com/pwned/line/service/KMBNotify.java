package com.pwned.line.service;
import com.mongodb.BasicDBObject;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import java.util.ArrayList;

/**
 * Service for course information.
 * Required params: [uid]
 * Reserved tokens: [@kmb::notify]
 * Resolved params: []
 * @author Timothy Pak
 */

public class KMBNotify extends DefaultService{

	/**
	 * Constructor
	 */
    public KMBNotify(Service service){
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
        this.fulfillment = startNotification(mongo, SELF, this.getParam("uid").toString());
    }

	/**
	 *
	 * @param mongo Database
	 * @param SELF User's uid
	 * @param uid uid
	 * @return Whether we will notify the user
	 */
	public static String startNotification(MongoDB mongo, BasicDBObject SELF, String uid){
	    String notify = "You are already in our list of notification.";
	    ArrayList<Document> kmb = MongoDB.get(mongo.getCollection("kmb").find(SELF));
	    if(kmb.size() == 0){
		    Document data = new Document();
		    data.append("uid", uid);
		    mongo.getCollection("kmb").insertOne(data);
		    notify = "We will start notifying you about the arrvial time. Please tell us to stop if you no longer need this service.";
	    }
		return notify;
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
