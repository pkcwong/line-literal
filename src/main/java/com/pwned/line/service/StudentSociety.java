package com.pwned.line.service;

import com.pwned.line.http.HTTP;
import org.json.JSONException;
import org.json.JSONObject;


/***
 * Service for sending requests to HKUST QUOTA WEBSITE.
 * Required params: [parameters]
 * Reserved tokens: [@Society::name]
 * Resolved params: []
 * @author Eric Kwan
 */
public class StudentSociety extends DefaultService {

    private static final String SocInfo_URL = "http://ihome.ust.hk/~su_ucoun/link.html";

    public StudentSociety(Service service) {
        super(service);
    }

    /***
     * Fetch the course information from HKUST Society Page.
     */
    @Override
    public void payload() throws Exception {
        System.out.println("trying");
        String SocietyCode = new JSONObject(this.getParam("parameters").toString()).getString("society");
        System.out.println("get soc code");
        HTTP link = new HTTP(SocInfo_URL);
        String societypage = link.get();
        String SocietyName = "";
        String[] keywords = {"<a href=\"http://ihome.ust.hk/~", "\" target=\"_blank\">", ", HKUSTSU", "</a>"};
        String societyURL = SocietyCode + keywords[1];  //e.g. su_civil" target="_blank">
        if (societyURL.equals(keywords[1])) {
            SocietyName = "Sorry, i can't find a suitable one for you.";
        } else {
            String societyweb = societypage.substring(societypage.indexOf(societyURL), societypage.lastIndexOf(SocietyCode));
            if (societypage.contains(societyURL)) {
                String societynamecode = societyweb.substring(societyweb.indexOf(societyURL) + societyURL.length(), societyweb.indexOf(keywords[3]));
                SocietyName = "I found some societies for you:\n" + societynamecode;
                SocietyName = SocietyName + "\ntheir website is\nhttp://ihome.ust.hk/~" + SocietyCode;
            }
        }
        this.fulfillment = this.fulfillment.replace("@Society::Name", SocietyName);
    }

    @Override
    public Service chain() throws Exception {
        return this;
    }

}
