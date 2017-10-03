package com.pwned.line.handler;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.pwned.line.KitchenSinkController;
import com.pwned.line.database.PostgreSQL;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TextHandler {

	public static void handle(LineMessagingClient lineMessagingClient, MessageEvent<TextMessageContent> event) throws URISyntaxException, SQLException {
		String keyword = null;
		String response = null;
		String resolve = null;
		TextMessageContent message = event.getMessage();
		resolve = message.getText();
		PostgreSQL postgreSQL = new PostgreSQL();
		Connection connection = postgreSQL.getConnection();
		PreparedStatement sql = connection.prepareStatement("SELECT * FROM collection");
		ResultSet result = sql.executeQuery();
		while (result.next()) {
			keyword = result.getString(1);
			response = result.getString(2);
			if (keyword.equals(resolve)) {
				resolve = response;
				break;
			}
		}
		KitchenSinkController.reply(lineMessagingClient, event.getReplyToken(), new TextMessage(resolve));
	}

}
