package com.pwned.line.service;

import com.pwned.line.web.ApiAI;
import org.json.JSONObject;

/***
 * Service for Society information.
 * Required params: [parameters]
 * Reserved tokens: []
 * Resolved params: []
 * @author Eric Kwan
 */
public class DialogFlowSociety extends DefaultService {

    private static final String ACCESS_TOKEN = "c567ffb3521c41dd859a6e4ab9dbfaf0"; //API Client Key of Society agent

    /**
     * Constructor
     * @param service
     */
    public DialogFlowSociety(Service service) {
        super(service);
    }

	/**
	 * Payload of Service module.
	 * @throws Exception Exception
	 */
    @Override
    public void payload() throws Exception {
        JSONObject json = new ApiAI(ACCESS_TOKEN, this.getParam("uid").toString(), this.fulfillment).execute();
        this.fulfillment = json.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
        this.setParam("parameters", json.getJSONObject("result").getJSONObject("parameters"));
    }

	/**
	 * Request processing from next Service module.
	 * @return Service state
	 * @throws Exception Exception
	 */
	@Override
    public Service chain() throws Exception {
        return new Society(this).resolve().get();
    }

}
