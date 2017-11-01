package com.pwned.line.service;

import com.mongodb.client.MongoCollection;
import com.pwned.line.web.MongoDB;
import com.pwned.line.web.YandexTranslate;
import org.json.JSONObject;

public class LiftAdvisor extends DefaultService {


    public LiftAdvisor(Service service) {
        super(service);
    }

    @Override
    public void payload() throws Exception {
        MongoDB mongo = new MongoDB("mongodb://admin:admin@ds243085.mlab.com:43085/lift");
        MongoCollection collection =  mongo.getCollection("lift");
        String lift = mongo.get(collection.find()).toString();
        this.fulfillment = this.fulfillment.replace("@LiftAdvisor", lift);
    }

    @Override
    public Service chain() throws Exception {
        return this;
    }

}
