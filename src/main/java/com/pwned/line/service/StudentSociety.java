package com.pwned.line.service;

import com.pwned.line.http.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Service for sending requests to HKUST QUOTA WEBSITE.
 * Required params: [parameters]
 * Reserved tokens: [@Society::name]
 * Resolved params: []
 * @author Calvin Ku, Christopher Wong
 */
public class StudentSociety extends DefaultService {

    private static final String SocInfo_URL = "http://ihome.ust.hk/~su_ucoun/link.html";

    public StudentSociety(Service service) {
        super(service);
    }

    /***
     * Fetch the course information from HKUST Course Quota Page.
     */
    @Override
    public void payload() throws Exception {
        String SocietyCode = new JSONObject(this.getParam("parameters").toString()).getString("society");

        HTTP link = new HTTP(SocInfo_URL);
        String societypage = link.get();
        String SocietyName = "";
        String[] keywords = {"<a href=\"http://ihome.ust.hk/~","\" target=\"_blank\">", ", HKUSTSU", "</a>"};
        String societyURL = SocietyCode+keywords[1];
        if(societyURL.equals(keywords[1])){
            SocietyName = "Sorry, i can't find a suitable one for you.";
        }else{
            String societyweb = societypage.substring(societypage.indexOf(societyURL),societypage.lastIndexOf(SocietyCode));
            if(societypage.contains(societyURL)){
                String societynamecode = societyweb.substring(societyweb.indexOf(societyURL)+societyURL.length(), societyweb.indexOf(keywords[3]));
                SocietyName = "I found some societies for you:\n"+societynamecode;
                SocietyName = SocietyName+"\ntheir website is\nhttp://ihome.ust.hk/~"+SocietyCode;
            }
        }
        this.fulfillment=this.fulfillment.replace("@Society::Name", SocietyName);


    }

    /***
     * Returns the name of the course.
     * @param httpResponse http response
     * @return Course name
     */
    private String getSocietyInfo(String httpResponse) throws Exception {
        JSONObject apiParam = new JSONObject(this.getParam("parameters").toString());
        String SocietyCode = apiParam.getString("society");  //get the entity "society" from Dialogflow
        System.out.println("SocietyCode is "+SocietyCode);   //e.g SocietyCode = su_civil;
        String regex = "target=(.+?)>(.+?)</a></td>\\s(.+?)<td>(.+?)</td>\\s(.+?)<td>"+SocietyCode+"</td>"; //<tr>\s(.+?)<td><a href=(.+?)target="_blank">(.+?)<\/a><\/td>\s(.+?)<td>(.+?)<\/td>\s(.+?)<td>(.+?)<\/td>
        String SocietyName = "";
        Pattern SocietyInfoPattern = Pattern.compile(regex);
        Matcher SocietyMatcher = SocietyInfoPattern.matcher(httpResponse);
        while(SocietyMatcher.find()) {
            System.out.println("regex is "+regex);
            SocietyName = SocietyMatcher.group(2);
        }

        System.out.println("SocietyName = "+SocietyName);
        return this.fulfillment.replace("@Society::Name", SocietyName);
    }

    @Override
    public Service chain() throws Exception {
        return this;
    }

}
