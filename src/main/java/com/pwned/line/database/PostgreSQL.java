package com.pwned.line.database;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/***
 * Helper class for accessing PostgreSQL database
 * @author Christopher Wong
 */
public class PostgreSQL {

	private URI dbUri = null;

	public PostgreSQL(String uri) throws URISyntaxException {
		this.dbUri = new URI(uri);
	}

	public PostgreSQL(URI uri) {
		this.dbUri = uri;
	}

	/***
	 * Opens a connection to PostgreSQL database.
	 * @return driver connection
	 */
	public Connection getConnection() {
		try {
			String username = dbUri.getUserInfo().split(":")[0];
			String password = dbUri.getUserInfo().split(":")[1];
			String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
			return DriverManager.getConnection(dbUrl, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
