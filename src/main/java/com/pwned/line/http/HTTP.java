package com.pwned.line.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HTTP {

	private String url = null;
	private Map<String, Object> headers = null;
	private Map<String, Object> params = null;

	public HTTP(String url) {
		this.url = url;
		this.headers = new HashMap<>();
		this.params = new HashMap<>();
	}

	/***
	 * Sets additional headers
	 * @param key header
	 * @param value value
	 */
	public void setHeaders(String key, Object value) {
		this.headers.put(key, value);
	}

	/***
	 * Sets additional parameters
	 * @param key key
	 * @param value value
	 */
	public void setParams(String key, Object value) {
		this.params.put(key, value);
	}

	/***
	 * Executes a blocking GET request
	 * @return response
	 */
	public String get() {
		try {
			HttpURLConnection conn = null;
			URL uri = null;
			String encoded;
			String readLine;
			StringBuilder url = new StringBuilder(this.url);
			StringBuilder param = new StringBuilder("");
			StringBuilder response = new StringBuilder("");
			BufferedReader reader = null;
			for (Map.Entry<String, Object> item : this.params.entrySet()) {
				if (param.toString().length() != 0) {
					param.append('&');
				}
				param.append(item.getKey());
				param.append('=');
				param.append(item.getValue().toString());
			}
			encoded = param.toString().replace(' ', '+').replace("?", "%3F");
			url.append('?');
			url.append(encoded);
			uri = new URL(url.toString());
			conn = (HttpURLConnection) (uri.openConnection());
			conn.setRequestMethod("GET");
			for (Map.Entry<String, Object> item : this.headers.entrySet()) {
				conn.setRequestProperty(item.getKey(), item.getValue().toString());
			}
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((readLine = reader.readLine()) != null) {
				response.append(readLine);
			}
			reader.close();
			System.out.println(response.toString());
			return response.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
