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
        String eta = "Sorry, there is no bus arriving";
        if(bus.equals("")||busstop.equals("")){
            eta = "Please enter a valid bus route or bus stop";
        }
        if(busstop.equals("HKUST South Gate")){
            String link = "https://citymapper.com/api/1/departures?headways=1&ids=HKStop_HkustSouth_NW_1&region_id=hk-hongkong";
            HTTP http = new HTTP(link);
            String info = http.get();
            String range = info.substring(info.indexOf("headway_seconds_range") + 27, info.indexOf("headsign") - 4);
            for(int i = 0 ; i < 15; i++){
                System.out.println(info.indexOf("headway_seconds_range") + 27);
                System.out.println(info.indexOf("headsign") - 4);
            }
            System.out.println(range);
            this.fulfillment = this.fulfillment.replace("@kmb::eta", "You requested for the arrival time of the next " + bus + " to " + busstop + ", the eta is " + range + " seconds.");
        }
        this.fulfillment = this.fulfillment.replace("@kmb::eta", eta);
    }


    @Override
    public Service chain() throws Exception {
        return this;
    }
}
