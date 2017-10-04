package com.pwned.line.database;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQL {

	private URI dbUri = null;

	public PostgreSQL() throws URISyntaxException {
		this.dbUri = new URI("postgres://cvklzahjcmqbjh:5beda633cb38a5d82f5293c3c675b383ed15fe0cc62136f5e931fb7304329443@ec2-54-163-233-201.compute-1.amazonaws.com:5432/ddr2s35rtr8an9");
	}

	public PostgreSQL(String uri) throws URISyntaxException {
		this.dbUri = new URI(uri);
	}

	public PostgreSQL(URI uri) {
		this.dbUri = uri;
	}

	public Connection getConnection() throws SQLException {
		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
		return  DriverManager.getConnection(dbUrl, username, password);
	}
}
