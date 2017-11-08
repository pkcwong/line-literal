package com.pwned.line.service;

import com.pwned.line.http.HTTP;
import org.json.JSONObject;

/***
 * Service for course information.
 * Required params: [uid]
 * Reserved tokens: [@kmb::eta]
 * Resolved params: []
 * @author Timothy Pak
 */

public class KMB extends DefaultService{


    public KMB(Service service) {
        super(service);
    }
    @Override
    public void payload() throws Exception {
        String bus = new JSONObject(this.getParam("parameters").toString()).getString("bus");
        String busstop = new JSONObject(this.getParam("parameters").toString()).getString("busstop");
        String link = "http://www.hko.gov.hk/wxinfo/currwx/flw.htm";
        HTTP http = new HTTP(link);
        String weather = http.get();
        String[] messages = {"Weather forecast", "<br/><br/>Outlook"};
        weather = weather.substring(weather.indexOf(messages[0]), weather.indexOf(messages[1]));
        weather = weather.replace("<br/>", "\n");
        while (weather.contains("<")){
            weather = weather.substring(0, weather.indexOf("<")) + weather.substring(weather.indexOf(">") + 1);
        }
        this.fulfillment = this.fulfillment.replace("@kmb::eta", weather);
    }


    @Override
    public Service chain() throws Exception {
        return this;
    }
}
