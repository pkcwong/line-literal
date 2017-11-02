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
        JSONObject apiParam = new JSONObject(this.getParam("parameters").toString());
        String SocietyCode = apiParam.getString("society");
        HTTP link = new HTTP(SocInfo_URL);
        String societypage = link.get();

        String SocietyName = "";

        String[] keywords = {"<a href=\"http://ihome.ust.hk/~","\" target=\"_blank\">", ", HKUSTSU", "</a>"};
        String societyURL = SocietyCode+keywords[1];
        System.out.println(societypage.indexOf(SocietyCode)+keywords[1].length()+" "+ societypage.lastIndexOf(SocietyCode));


        System.out.println(societypage.indexOf(societyURL)+"  keywords[2]+9 = "+(societypage.indexOf(keywords[2])+9));
        String societyweb = societypage.substring(societypage.indexOf(societyURL),societypage.lastIndexOf(SocietyCode));
        System.out.println("societyweb = "+societyweb);
        if(societypage.contains(societyURL)){
            System.out.println("contains");
            String societynamecode = societyweb.substring(societyweb.indexOf(societyURL)+SocietyCode.length()+keywords[1].length(), societyweb.indexOf(keywords[3]));
            SocietyName = societynamecode;
            System.out.println("SocietyName = "+ SocietyName);

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
