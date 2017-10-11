package com.pwned.line.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HTTP {

	private HttpClient client = null;
	private String url = null;
	private Map<String, Object> headers = null;
	private Map<String, Object> params = null;

	public HTTP(String url) {
		this.url = url;
		this.headers = new HashMap<>();
		this.params = new HashMap<>();
	}

	public void setHeaders(String key, Object value) {
		this.headers.put(key, value);
	}

	public void setParams(String key, Object value) {
		this.params.put(key, value);
	}

	public String get() {
		try {
			HttpGet request = null;
			HttpResponse response = null;
			BufferedReader reader = null;
			StringBuffer result = new StringBuffer();
			StringBuilder url = new StringBuilder(this.url);
			String line = "";
			url.append('?');
			for (Map.Entry<String, Object> item : this.params.entrySet()) {
				if (url.toString().charAt(url.toString().length() - 1) != '?') {
					url.append('&');
				}
				url.append(item.getKey());
				url.append('=');
				url.append(item.getValue().toString());
			}
			request = new HttpGet(url.toString());
			for (Map.Entry<String, Object> item : this.headers.entrySet()) {
				request.addHeader(item.getKey(), item.getValue().toString());
			}
			this.client = HttpClientBuilder.create().build();
			response = this.client.execute(request);
			reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}