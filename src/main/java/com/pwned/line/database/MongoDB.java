package com.pwned.line.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONObject;

import java.util.Map;

public class MongoDB {

	private MongoClient mongo = null;
	private MongoDatabase db = null;

	public MongoDB(String uri, String database) {
		MongoClientURI link = new MongoClientURI(uri);
		this.mongo = new MongoClient(link);
		this.db = this.mongo.getDatabase(database);
	}

	public void insert(String collection, Map<String, Object> map) {
		Document doc = new Document(map);
		this.db.getCollection(collection).insertOne(doc);
	}

	public void insert(String collection, JSONObject json) {
		Document doc = Document.parse(json.toString());
		this.db.getCollection(collection).insertOne(doc);
	}

}
