package com.pwned.line.service;

import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.pwned.line.KitchenSinkController;

public class AnonymousChat extends DefaultService {

	public AnonymousChat(Service service) {
		super(service);
	}

	@Override
	public void payload() throws Exception {
		ConfirmTemplate confirmTemplate = null;
		if (this.getParam("uid").toString().equals(this.getParam("bind").toString())) {
			this.fulfillment = "*** Tap \'connect\' to chat with a random user! ***";
			confirmTemplate = new ConfirmTemplate("Anonymous Chat System", new MessageAction("connect", "Connect"), new MessageAction("cancel", "Cancel"));
		} else {
			this.fulfillment = "*** Tap \'Terminate\' to close the session. ***";;
			confirmTemplate = new ConfirmTemplate("Anonymous Chat System", new MessageAction("terminate", "Terminate"), new MessageAction("cancel", "Cancel"));
		}
		KitchenSinkController.push(this.getParam("uid").toString(), new TemplateMessage("Anonymous Chat System", confirmTemplate));
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

}
