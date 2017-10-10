package com.pwned.line.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MongoDB {

	private MongoClient mongo = null;

	public MongoDB(String uri) {
		MongoClientURI link = new MongoClientURI(uri);
		this.mongo = new MongoClient(link);
	}

	public MongoDatabase selectDB(String db) {
		return this.mongo.getDatabase(db);
	}

}
