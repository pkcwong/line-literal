package com.pwned.line.service;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/***
 * Default Service module.
 * Required params: []
 * Reserved tokens: []
 * Resolved params: []
 * @author Christopher Wong
 */
public class DefaultService implements Service {

	protected String fulfillment = null;
	protected Map<String, Object> args = null;

	/***
	 * Entry for service module chain
	 * @param query query
	 */
	public DefaultService(String query) {
		this.fulfillment = query;
		this.args = new HashMap<>();
	}

	/***
	 * Copy constructor
	 * @param service instance
	 */
	public DefaultService(Service service) {
		this.fulfillment = service.getFulfillment();
		this.args = service.getArgs();
	}

	/***
	 * Allocates a new Promise object for a task.
	 * @return Promise
	 * @throws Exception Exception
	 */
	@Override
	public final CompletableFuture<Service> resolve() throws Exception {
		this.dump();
		this.payload();
		this.dump();
		return CompletableFuture.supplyAsync(() -> {
			try {
				return this.chain();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		});
	}

	/***
	 * Default Service provides no processing.
	 */
	@Override
	public void payload() throws Exception {

	}

	/***
	 * Pass query to API.AI
	 * @return Service
	 * @throws Exception Exception
	 */
	@Override
	public Service chain() throws Exception {
		return this;
	}

	/***
	 * Sets environment variables for a key
	 * @param key key
	 * @param value value
	 */
	@Override
	public final void setParam(String key, Object value) {
		this.args.put(key, value);
	}

	/***
	 * Retrieves a value for key
	 * @param key key
	 * @return value
	 */
	@Override
	public final Object getParam(String key) {
		return this.args.get(key);
	}

	/***
	 * Getter for fulfillment
	 * @return fulfillment
	 */
	@Override
	public final String getFulfillment() {
		return this.fulfillment;
	}

	/***
	 * Getter for environment variable hash map
	 * @return Map
	 */
	@Override
	public final Map<String, Object> getArgs() {
		return this.args;
	}

	/***
	 * Memory dump for pipeline entry and exit nodes
	 * @throws Exception Exception
	 */
	@Override
	public final void dump() throws Exception {
		JSONObject dump = new JSONObject();
		JSONObject mem = new JSONObject(this.getArgs());
		dump.put("args", mem);
		dump.put("fulfillment", this.getFulfillment());
		dump.put("class", this.getClass().getSimpleName());
		System.out.println(dump.toString());
	}

}
