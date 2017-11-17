package com.pwned.line.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.pwned.line.web.MongoDB;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;

/***
 * Adding review to MongoDB.
 * Required params: []
 * Reserved tokens: []
 * Resolved params: []
 * @author Bear
 */

public class EventAdd extends DefaultService {

	public EventAdd(Service service) {
		super(service);
	}

	@Override
	public void payload() throws Exception {
		System.out.println(this.fulfillment);
		this.fulfillment = "Your event had been added";
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

}
