package com.pwned.line.service;
import com.mongodb.BasicDBObject;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import java.util.ArrayList;

/***
 * Service for course information.
 * Required params: [uid]
 * Reserved tokens: [@kmb::notify]
 * Resolved params: []
 * @author Timothy Pak
 */

public class KMBNotify extends DefaultService{

    public KMBNotify(Service service){
        super(service);
    }

    @Override
    public void payload() throws Exception{
        MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));
        BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
        ArrayList<Document> kmb = MongoDB.get(mongo.getCollection("kmb").find(SELF));
        if(kmb.size() == 0){
            Document data = new Document();
            data.append("uid", this.getParam("uid").toString());
            mongo.getCollection("user").insertOne(data);
        }
	    this.fulfillment = this.fulfillment.replace("@kmb::notify", "We will start notifying you about the arrvial time. Please tell us to stop if you no longer need this service.");
    }

    @Override
    public Service chain() throws Exception{
        return this;
    }
}
