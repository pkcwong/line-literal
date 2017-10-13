package com.pwned.line.service;

import java.util.HashMap;
import java.util.Map;

/***
 * Service prototype for chat-bot engine.
 * @author Christopher Wong
 */
public class Service {

	protected String fulfillment = null;
	protected Map<String, Object> args = null;

	public Service(String query) {
		this.fulfillment = query;
		this.args = new HashMap<>();
	}

	public Service(String query, Map<String, Object> args) {
		this.fulfillment = query;
		this.args = args;
	}

	/***
	 * Default fulfillment with no processing.
	 * @return
	 */
	public String resolve() {
		return this.fulfillment;
	}

	/***
	 * Sets a variable.
	 * @param key key
	 * @param value value
	 */
	public void setArgs(String key, Object value) {
		this.args.put(key, value);
	}

	/***
	 * Gets a passed or resolved variable.
	 * @param key key
	 * @return value
	 */
	public Object getArgs(String key) {
		return this.args.get(key);
	}

}
