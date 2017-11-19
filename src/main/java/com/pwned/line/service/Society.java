package com.pwned.line.service;

import com.pwned.line.http.HTTP;
import org.json.JSONObject;

/***
 * Service for sending requests to HKUSTSU Society WEBSITE.
 * Required params: [parameters]
 * Reserved tokens: [@society::name]
 * Resolved params: []
 * @author Eric Kwan
 */
public class Society extends DefaultService{

    private static final String SocInfo_URL = "http://ihome.ust.hk/~su_ucoun/link.html";

    /**
     * Constructor
     * @param service
     */
    public Society(Service service){
        super(service);
    }

    /***
     * Fetch the Society information from HKUST Society Page.
     */
    @Override
    public void payload() throws Exception{
        String societyCode = new JSONObject(this.getParam("parameters").toString()).getString("society");
        HTTP link = new HTTP(SocInfo_URL);
        String societyPage = link.get();
        String societyName = "";
        String[] keywords = {"<a href=\"http://ihome.ust.hk/~", "\" target=\"_blank\">", ", HKUSTSU", "</a>"};
        String societyURL = societyCode + keywords[1];  //e.g. su_civil" target="_blank">
        if (societyURL.equals(keywords[1])) {
            societyName = "I am sorry, the society that you entered is not valid.";
        }else{
            if (societyPage.contains(societyURL)) {
	            String societyWeb = societyPage.substring(societyPage.indexOf(societyURL), societyPage.lastIndexOf(societyCode));
                String societyNameCode = societyWeb.substring(societyWeb.indexOf(societyURL) + societyURL.length(), societyWeb.indexOf(keywords[3]));
                societyName = "I have found your desired society:\n" + societyNameCode;
                societyName = societyName + "\nTheir website is\nhttp://ihome.ust.hk/~" + societyCode;
            }else{
            	societyName = "I am sorry, the society that you entered is not in our database.";
            }
        }
        this.fulfillment = this.fulfillment.replace("@society::name", societyName);
    }

    /**
     * Request processing from next Service module.
     * @return Service state
     * @throws Exception Exception
     */
    @Override
    public Service chain() throws Exception{
        return this;
    }

}
