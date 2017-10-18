package com.pwned.line.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/***
 * Service interface
 * @author Christopher Wong
 */
public interface Service {

	/***
	 * Main payload of service.
	 * @return async method
	 */
	CompletableFuture<Service> resolve();

	/***
	 * Setter for parameters.
	 * @param key key
	 * @param value value
	 */
	void setParam(String key, Object value);

	/***
	 * Getter for parameters.
	 * @param key key
	 * @return
	 */
	Object getParam(String key);

	/***
	 * Getter for data member 'fulfillment'.
	 * @return
	 */
	String getFulfillment();

	/***
	 * Getter for data member 'args'.
	 * @return
	 */
	Map<String, Object> getArgs();

}
