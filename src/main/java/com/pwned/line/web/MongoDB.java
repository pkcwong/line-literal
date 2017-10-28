package com.pwned.line.web;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONObject;

import java.util.Map;

/***
 * Helper class for accessing MongoDB
 * @author Christopher Wong
 */
public class MongoDB {

	private MongoClient mongo = null;
	private MongoDatabase db = null;

	public MongoDB(String uri, String database) {
		MongoClientURI link = new MongoClientURI(uri);
		this.mongo = new MongoClient(link);
		this.db = this.mongo.getDatabase(database);
	}

	/***
	 * Inserts a document into mongodb by Map.
	 * @param collection collection name
	 * @param map data
	 */
	public void insert(String collection, Map<String, Object> map) {
		Document doc = new Document(map);
		this.db.getCollection(collection).insertOne(doc);
	}

	/***
	 * Inserts a document into mongodb by JSON.
	 * @param collection collection name
	 * @param json data
	 */
	public void insert(String collection, JSONObject json) {
		Document doc = Document.parse(json.toString());
		this.db.getCollection(collection).insertOne(doc);
	}

	/***
	 * Returns a Mongo Collection
	 * @param collection collection name
	 * @return MongoCollection
	 */
	public MongoCollection<Document> getCollection(String collection) {
		return this.db.getCollection(collection);
	}

}
