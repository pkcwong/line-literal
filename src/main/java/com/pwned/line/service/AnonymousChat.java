package com.pwned.line.service;

import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.pwned.line.KitchenSinkController;

public class AnonymousChat extends DefaultService {

	public AnonymousChat(Service service) {
		super(service);
	}

	@Override
	public void payload() throws Exception {
		if (this.fulfillment.equals("anonymous")) {
			ConfirmTemplate confirmTemplate;
			if (this.getParam("uid").toString().equals(this.getParam("bind").toString())) {
				this.fulfillment = "***\nTap \'Connect\' to chat with a random user!\n***";
				confirmTemplate = new ConfirmTemplate("Anonymous Chat System", new PostbackAction("connect", "anonymous::connect"), new PostbackAction("cancel", "anonymous::cancel"));
			} else {
				this.fulfillment = "***\nTap \'Terminate\' to close the session.\n***";
				confirmTemplate = new ConfirmTemplate("Anonymous Chat System", new PostbackAction("terminate", "anonymous::terminate"), new PostbackAction("cancel", "anonymous::cancel"));
			}
			KitchenSinkController.push(this.getParam("uid").toString(), new TemplateMessage("Anonymous Chat System", confirmTemplate));
		} else if (this.fulfillment.equals("anonymous::connect")) {
			this.fulfillment = "connection here.";
		} else if (this.fulfillment.equals("anonymous::terminate")) {
			this.fulfillment = "termination here";
		}
	}

	@Override
	public Service chain() throws Exception {
		return this;
	}

}
