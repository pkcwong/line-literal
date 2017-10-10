package com.pwned.line.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;

public class MongoDB {

	private MongoClient mongo = null;

	public MongoDB(String host, int port) {
		this.mongo = new MongoClient(host, port);
	}

	public MongoDatabase selectDB(String db) {
		return this.mongo.getDatabase(db);
	}

	public MongoDatabase selectDB(String db, String username, String password) {
		MongoCredential.createCredential(username, db, password.toCharArray());
		return this.selectDB(db);
	}

}
