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
        String[] route = {"KMB 91M", "KMB 91"};
        String bus = new JSONObject(this.getParam("parameters").toString()).getString("bus");
        String busstop = new JSONObject(this.getParam("parameters").toString()).getString("busstop");
        String eta = "Sorry, there is no bus arriving";
        int etaminutes = 200;
        if(bus.equals("")||busstop.equals("")){
            eta = "Please enter a valid bus route or bus stop";
        }
        if(busstop.equals("HKUST South Gate")){
            String link = "https://citymapper.com/api/1/departures?headways=1&ids=HKStop_HkustSouth_NW_1&region_id=hk-hongkong";
            HTTP http = new HTTP(link);
            String info = http.get();
            JSONObject stop = new JSONObject(info);
            JSONArray stops = stop.getJSONArray("stops");
            JSONObject layer = stops.getJSONObject(0);
            JSONArray services = layer.getJSONArray("services");
            String[] route_id = new String[services.length()];
            String[] etasecond = new String[services.length()];
            for(int i = 0; i < services.length(); i++){
                route_id[i] = services.getJSONObject(i).getString("route_id");
                if(services.getJSONObject(i).has("live_departures_seconds")){
                    etasecond[i] = services.getJSONObject(i).getString("live_departures_seconds");
                    etasecond[i] = etasecond[i].substring(1, etasecond[i].length() - 1);
                }else if(services.getJSONObject(i).has("headway_seconds_range")){
                    etasecond[i] = "1200";
                }
            }
            for(int k = 0; k < 20; k++) {
                System.out.println(bus);
                System.out.println(route[1]);
            }
            if(bus.equals(route[0])){
                for(int i = 0; i < services.length(); i++){
                    if(route[i].equals("KMBBus91M")){
                        for(int k = 0; k < 50; k++)
                            System.out.println(etaminutes + "before");
                        etaminutes = Integer.parseInt(etasecond[i]) / 60;
                        for(int k = 0; k < 50; k++)
                            System.out.println(etaminutes + "after");
                    }
                }
            }else if(bus.equals(route[1])){
                for(int i = 0; i < services.length(); i++){
                    if(route[i].equals("KMBBus91")){
                        for(int k = 0; k < 50; k++)
                            System.out.println(etaminutes + "before");
                        etaminutes = Integer.parseInt(etasecond[i]) / 60;
                        for(int k = 0; k < 50; k++)
                            System.out.println(etaminutes + "after");
                    }
                }
            }
            this.fulfillment = this.fulfillment.replace("@kmb::eta", "You requested for the arrival time of the next " + bus + " to " + busstop + ", the estimated time of arrival is " + etaminutes + " minutes.");
        }
        this.fulfillment = this.fulfillment.replace("@kmb::eta", eta);
    }


    @Override
    public Service chain() throws Exception {
        return this;
    }
}
