package com.pwned.line.web;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Helper class for accessing MongoDB
 * @author Christopher Wong
 */
public class MongoDB extends MongoClient {

	private final String host;
	private final String port;
	private final String user;
	private final String password;
	private final String database;

	public MongoDB(String uri) {
		super(new MongoClientURI(uri));
		String host = "";
		String port = "";
		String user = "";
		String password = "";
		String database = "";
		Pattern regex = Pattern.compile("mongodb://(.+):(.+)@(.+):(.+)/(.+)");
		Matcher matcher = regex.matcher(uri);
		while (matcher.find()) {
			host = matcher.group(3);
			port = matcher.group(4);
			user = matcher.group(1);
			password = matcher.group(2);
			database = matcher.group(5);
		}
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.database = database;
	}

	/***
	 * Returns a Mongo Collection
	 * @param collection collection name
	 * @return MongoCollection
	 */
	public MongoCollection<Document> getCollection(String collection) {
		return this.getDatabase(this.database).getCollection(collection);
	}

	public String getHost() {
		return this.host;
	}

	public String getPort() {
		return this.port;
	}

	public String getUser() {
		return this.user;
	}

	public String getPassword() {
		return this.password;
	}

	public String getDatabase() {
		return this.database;
	}

	public static ArrayList<Document> get(FindIterable<Document> iterable) {
		ArrayList<Document> list = new ArrayList<>();
		MongoCursor<Document> cursor = iterable.iterator();
		while (cursor.hasNext()) {
			list.add(cursor.next());
		}
		cursor.close();
		return list;
	}

}
