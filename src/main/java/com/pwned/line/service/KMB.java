package com.pwned.line.service;

import com.pwned.line.http.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/***
 * Service for course information.
 * Required params: [uid]
 * Reserved tokens: [@kmb::eta]
 * Resolved params: []
 * @author Timothy Pak
 */

public class KMB extends DefaultService{

    public KMB(Service service){
        super(service);
    }

    @Override
    public void payload() throws Exception{
        String eta = getETA(new JSONObject(this.getParam("parameters").toString()).getString("busstop").toString());
        this.fulfillment = this.fulfillment.replace("@kmb::eta", eta);
    }

    public static String getETA(String busstop) throws JSONException{
        String eta = "Sorry, there is no bus arriving";
        if(busstop.equals("")){
            eta = "Please enter a valid bus route or bus stop";
            return eta;
        }
        if(Link.getBusStopLink(busstop) == null) {
            eta = "The busstop in not in our database.";
            return eta;
        }
        HTTP http = new HTTP(Link.getBusStopLink(busstop));
        String info = http.get();
        JSONObject stop = new JSONObject(info);
        JSONArray stops = stop.getJSONArray("stops");
        JSONObject layer = stops.getJSONObject(0);
        JSONArray services = layer.getJSONArray("services");
        String[] route_id = new String[services.length()];
        String[] etasecond = new String[services.length()];
        eta = "The following are the arrival time of the buses at " + busstop + ":";
        for(int i = 0; i < services.length(); i++){
            route_id[i] = services.getJSONObject(i).getString("route_id");
            if(services.getJSONObject(i).has("live_departures_seconds")){
                etasecond[i] = services.getJSONObject(i).getString("live_departures_seconds");
                etasecond[i] = etasecond[i].substring(1, etasecond[i].length() - 1);
                if(etasecond[i].contains(",")) {
                    etasecond[i] = etasecond[i].substring(0, etasecond[i].indexOf(","));
                }
                eta = eta + "\n" + route_id[i] + " will arrive in " + Integer.parseInt(etasecond[i]) / 60 + " minutes.";
            }else if(services.getJSONObject(i).has("headway_seconds_range")){
                etasecond[i] = services.getJSONObject(i).getString("headway_seconds_range");
                etasecond[i] = etasecond[i].substring(1, etasecond[i].length() - 1);
                if(etasecond[i].contains(",")) {
                    etasecond[i] = etasecond[i].substring(0, etasecond[i].indexOf(","));
                }
                eta = eta + "\n" + route_id[i] + " will arrive in " + Integer.parseInt(etasecond[i]) / 60 + " minutes.";
            }else{
                etasecond[i] = null;
            }
        }

        return eta;

    }

    @Override
    public Service chain() throws Exception {
        return this;
    }
}
