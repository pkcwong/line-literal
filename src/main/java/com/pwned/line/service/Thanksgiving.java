package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import java.util.ArrayList;

/***
 * Accept/Bring food for thanksgiving
 * Required params: []
 * Reserved tokens: []
 * Resolved params: []
 * @author Bear
 */

public class Thanksgiving extends DefaultService{
	private String keyword;
	public Thanksgiving(Service service, String key){
		super(service);
		keyword = key;
	}

	@Override
	public void payload(){
		if(keyword.contains("accept")){
			MongoDB mongo = new MongoDB(System.getenv("MONGODB_URI"));

			BasicDBObject SELF = new BasicDBObject().append("uid", this.getParam("uid").toString());
			ArrayList<Document> user = MongoDB.get(mongo.getCollection("party").find(SELF));

			if (user.size() == 0) {
				Document data = new Document();
				data.append("uid", this.getParam("uid").toString());
				mongo.getCollection("party").insertOne(data);
				this.fulfillment = "Thank you for your join! Have a fun night!";
			} else {
				this.fulfillment = "Already accept the party!";
			}
		}
	}

	@Override
	public Service chain(){
		return this;
	}

}
