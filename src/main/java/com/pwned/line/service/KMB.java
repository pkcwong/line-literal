package com.pwned.line.service;

import com.pwned.line.http.HTTP;
import org.json.JSONArray;
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
            JSONObject stop = new JSONObject(info);
            System.out.println(stop);
            System.out.println(stop);
            System.out.println(stop);
            System.out.println(stop);
            System.out.println(stop);
            System.out.println(stop);
            System.out.println(stop);
            JSONObject stops = stop.getJSONObject("stops");
            System.out.println(stops);
            System.out.println(stops);
            System.out.println(stops);
            System.out.println(stops);
            System.out.println(stops);
            System.out.println(stops);
            System.out.println(stops);
            System.out.println(stops);
            System.out.println(stops);
            System.out.println(stops);
            System.out.println(stops);
            System.out.println(stops);
            System.out.println(stops);
            JSONArray services = stops.getJSONArray("services");
            System.out.println(services);
            System.out.println(services);
            System.out.println(services);
            System.out.println(services);
            System.out.println(services);
            System.out.println(services);
            System.out.println(services);
            System.out.println(services);
            System.out.println(services);
            for(int i = 0; i < services.length(); i++){
                String route_id = services.getJSONObject(i).getString("route_id");
                String live_departures_seconds = services.getJSONObject(i).getString("live_departures_seconds");
                String headway_seconds_range = services.getJSONObject(i).getString("headway_seconds_range");
                System.out.println(route_id);
                System.out.println(route_id);
                System.out.println(route_id);
                System.out.println(route_id);
                System.out.println(route_id);
                System.out.println(route_id);
                System.out.println(route_id);
                System.out.println(route_id);
                System.out.println(live_departures_seconds);
                System.out.println(live_departures_seconds);
                System.out.println(live_departures_seconds);
                System.out.println(live_departures_seconds);
                System.out.println(live_departures_seconds);
                System.out.println(live_departures_seconds);
                System.out.println(live_departures_seconds);
                System.out.println(live_departures_seconds);
                System.out.println(live_departures_seconds);
                System.out.println(live_departures_seconds);
                System.out.println(live_departures_seconds);
                System.out.println(live_departures_seconds);
                System.out.println(headway_seconds_range);
                System.out.println(headway_seconds_range);
                System.out.println(headway_seconds_range);
            }
            this.fulfillment = this.fulfillment.replace("@kmb::eta", "You requested for the arrival time of the next " + bus + " to " + busstop + ", the eta is  seconds.");
        }
        this.fulfillment = this.fulfillment.replace("@kmb::eta", eta);
    }


    @Override
    public Service chain() throws Exception {
        return this;
    }
}
