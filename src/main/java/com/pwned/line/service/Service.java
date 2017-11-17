package com.pwned.line.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/***
 * Service interface.
 * @author Christopher Wong
 */
public interface Service {

	/***
	 * Threading manager for Service modules.
	 * @return CompletableFuture
	 */
	CompletableFuture<Service> resolve() throws Exception;

	/***
	 * Payload of Service module.
	 */
	void payload() throws Exception;

	/***
	 * Request processing from next Service module.
	 * @return Service state
	 * @throws Exception Exception
	 */
	Service chain() throws Exception;

	/***
	 * Sets resolved parameters.
	 * @param key key
	 * @param value value
	 */
	void setParam(String key, Object value);

	/***
	 * Gets resolved parameters.
	 * @param key key
	 * @return Object
	 */
	Object getParam(String key);

	/***
	 * Getter for fulfillment.
	 * @return String
	 */
	String getFulfillment();

	/***
	 * Getter for args.
	 * @return Map
	 */
	Map<String, Object> getArgs();

	/***
	 * Debug messages dump.
	 */
	void dump() throws Exception;

}
