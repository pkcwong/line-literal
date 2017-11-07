package com.pwned.line.service;



/***
 * Service for small talk.
 * Required params: [parameters]
 * Reserved tokens: []
 * Resolved params: []
 * @author Bear
 */
public class Smalltalk extends DefaultService {



	public Smalltalk(Service service) {
		super(service);
	}


	@Override
	public void payload() throws Exception {
		return;
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

}
