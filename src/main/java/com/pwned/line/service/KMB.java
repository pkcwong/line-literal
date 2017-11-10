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
            for(int i = 0 ; i < 15; i++){
                System.out.println(bus);
            }
            for(int i = 0 ; i < 15; i++){
                System.out.println(busstop);
            }

            JSONObject stop = new JSONObject(info);
            JSONObject services = stop.getJSONObject("services");
            String etasecond = services.getJSONArray("headway_seconds_range").toString();
            for(int i = 0 ; i < 15; i++){
                System.out.println(stop);
            }
            for(int i = 0 ; i < 15; i++){
                System.out.println(services);
            }
            for(int i = 0 ; i < 15; i++){
                System.out.println(etasecond);
            }
            this.fulfillment = this.fulfillment.replace("@kmb::eta", "You requested for the arrival time of the next " + bus + " to " + busstop + ", the eta is " + etasecond + " seconds.");
        }
        this.fulfillment = this.fulfillment.replace("@kmb::eta", eta);
    }


    @Override
    public Service chain() throws Exception {
        return this;
    }
}
