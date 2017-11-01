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
        HTTP httpClient = new HTTP(SocInfo_URL);
        this.fulfillment = getSocietyInfo(httpClient.get());
    }

    /***
     * Returns the name of the course.
     * @param httpResponse http response
     * @return Course name
     */
    private String getSocietyInfo(String httpResponse) throws Exception {
        JSONObject apiParam = new JSONObject(this.getParam("parameters").toString());
        String SocietyCode = apiParam.getString("society");  //get the entity "society" from Dialogflow
        System.out.println(SocietyCode);
        String regex = "<tr>\\s(.+?)<td><a href=(.+?)target="+"_blank"+">(.+?)</a></td>\\s(.+?)<td>(.+?)</td>\\s(.+?)<td>"+SocietyCode+"</td>"; //<tr>\s(.+?)<td><a href=(.+?)target="_blank">(.+?)<\/a><\/td>\s(.+?)<td>(.+?)<\/td>\s(.+?)<td>(.+?)<\/td>
        String SocietyName = "null";
        System.out.println(regex);
        Pattern SocietyInfoPattern = Pattern.compile(regex);
        System.out.println(SocietyInfoPattern);
        Matcher SocietyMatcher = SocietyInfoPattern.matcher(httpResponse);
        System.out.println(SocietyMatcher.groupCount());
        System.out.println(SocietyMatcher);
        while (SocietyMatcher.find()) {
            System.out.println("regex is"+regex);
            SocietyName = SocietyMatcher.group(3);
        }

        System.out.println("SocietyName = "+SocietyName);
        return this.fulfillment.replace("@Society::Name", SocietyName);
    }

    @Override
    public Service chain() throws Exception {
        return this;
    }

}
