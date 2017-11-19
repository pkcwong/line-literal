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
        this.fulfillment = this.fulfillment.replace("@kmb::eta", getETA(new JSONObject(this.getParam("parameters").toString()).getString("busstop").toString()));
    }

    public static String getETA(String busstop) throws JSONException{
        String eta = "Sorry, there is no bus arriving";
        if(busstop.equals("")){
            eta = "Please enter a valid bus stop";
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

    public static String getBusETA(String route_id, String busstop) throws JSONException{
        HTTP http = new HTTP(Link.getBusStopLink(busstop));
        String info = http.get();
        JSONObject stop = new JSONObject(info);
        JSONArray stops = stop.getJSONArray("stops");
        JSONObject layer = stops.getJSONObject(0);
        JSONArray services = layer.getJSONArray("services");
        String etasecond = "";
        for(int i = 0; i < services.length(); i++){
            if(route_id.equals(services.getJSONObject(i).getString("route_id"))){
                if(services.getJSONObject(i).has("live_departures_seconds")){
                    etasecond = services.getJSONObject(i).getString("live_departures_seconds");
                    etasecond = etasecond.substring(1, etasecond.length() - 1);
                    if(etasecond.contains(",")) {
                        etasecond = etasecond.substring(0, etasecond.indexOf(","));
                    }
                }else if(services.getJSONObject(i).has("headway_seconds_range")){
                    etasecond = services.getJSONObject(i).getString("headway_seconds_range");
                    etasecond = etasecond.substring(1, etasecond.length() - 1);
                    if(etasecond.contains(",")) {
                        etasecond = etasecond.substring(0, etasecond.indexOf(","));
                    }
                }else{
                    etasecond = null;
                }
            }

        }
        return etasecond;
    }

    @Override
    public Service chain() throws Exception {
        return this;
    }
}
