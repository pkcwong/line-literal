package com.pwned.line.service;

import com.mongodb.client.MongoCollection;
import com.pwned.line.web.MongoDB;
import org.bson.Document;

/*******
 * Service for finding closest lift.
 * Required params: [location]
 * Reserved tokens: [@LiftAdvisor]
 * Resolved params: []
 * @author Bear
 */

public class LiftAdvisor extends DefaultService {


    public LiftAdvisor(Service service) {
        super(service);
    }

    @Override
    public void payload() throws Exception {
        String lift = "";
        String ending = "}}";
        MongoDB mongo = new MongoDB("mongodb://admin:admin@ds243085.mlab.com:43085/lift");  //Connecting DB
        MongoCollection collection =  mongo.getCollection("lift");   // Get DB called lift
        Document myDoc = (Document)collection.find().first();     //Get the document; need to use get() to get more documents
        lift = myDoc.toString();
        int index = lift.indexOf("Lift");
        lift = lift.substring(index , index + 4) + lift.substring(index + 5, lift.indexOf(ending));
        this.fulfillment = this.fulfillment.replace("@LiftAdvisor", lift);
    }

    @Override
    public Service chain() throws Exception {
        return this;
    }

}
