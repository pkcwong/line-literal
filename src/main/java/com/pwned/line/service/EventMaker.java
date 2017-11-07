package com.pwned.line.service;

/***
 * Service for event maker.
 * Required params: [params]
 * Reserved tokens: []
 * Resolved params: []
 * @author Bear
 */

public class EventMaker extends DefaultService{
	public EventMaker(Service service){
		super(service);
	}

	@Override
	public void payload() throws Exception{


	}


	@Override
	public Service chain() throws Exception {
		return this;
	}
}
