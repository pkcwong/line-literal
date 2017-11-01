package com.pwned.line.service;

import com.pwned.line.web.ApiAI;
import org.json.JSONObject;

/***
 * Service for course information.
 * Required params: [uid]
 * Reserved tokens: []
 * Resolved params: [parameters]
 * @author Christopher Wong, Calvin Ku
 */
public class DialogFlowSociety extends DefaultService {

    private static final String ACCESS_TOKEN = "c567ffb3521c41dd859a6e4ab9dbfaf0"; //API Client Key of Society agent

    public DialogFlowSociety(Service service) {
        super(service);
    }

    /***
     * DialogFlow payload
     */
    @Override
    public void payload() throws Exception {
        JSONObject json = new ApiAI(ACCESS_TOKEN, this.getParam("uid").toString(), this.fulfillment).execute();
        this.fulfillment = json.getJSONObject("result").getJSONObject("fulfillment").getString("speech");
        this.setParam("parameters", json.getJSONObject("result").getJSONObject("parameters"));
    }

    @Override
    public Service chain() throws Exception {
        return new StudentSociety(this).resolve().get();
    }

}
