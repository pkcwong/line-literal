package com.pwned.line.service;

import com.pwned.line.http.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Service for course information.
 * Required params: [uid]
 * Reserved tokens: [@kmb::eta]
 * Resolved params: []
 * @author Timothy Pak
 */

public class KMB extends DefaultService{

	/**
	 * Constructor
	 * @param service
	 */
    public KMB(Service service){
        super(service);
    }

	/**
	 * Payload of Service module.
	 * @throws Exception Exception
	 */
    @Override
    public void payload() throws Exception{
        this.fulfillment = this.fulfillment.replace("@kmb::eta", getETA(new JSONObject(this.getParam("parameters").toString()).getString("busstop").toString()));
    }

	/**
	 *
	 * @param busstop Bus stop name
	 * @return Estimate time of arrival of all buses at the bus stop
	 * @throws JSONException
	 */
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
        String[] etaSecond = new String[services.length()];
        eta = "The following are the arrival time of the buses at " + busstop + ":";
        for(int i = 0; i < services.length(); i++){
            route_id[i] = services.getJSONObject(i).getString("route_id");
            if(services.getJSONObject(i).has("live_departures_seconds")){
                etaSecond[i] = services.getJSONObject(i).getString("live_departures_seconds");
                etaSecond[i] = etaSecond[i].substring(1, etaSecond[i].length() - 1);
                if(etaSecond[i].contains(",")) {
                    etaSecond[i] = etaSecond[i].substring(0, etaSecond[i].indexOf(","));
                }
                eta = eta + "\n" + route_id[i] + " will arrive in " + Integer.parseInt(etaSecond[i]) / 60 + " minutes.";
            }else if(services.getJSONObject(i).has("headway_seconds_range")){
                etaSecond[i] = services.getJSONObject(i).getString("headway_seconds_range");
                etaSecond[i] = etaSecond[i].substring(1, etaSecond[i].length() - 1);
                if(etaSecond[i].contains(",")) {
                    etaSecond[i] = etaSecond[i].substring(0, etaSecond[i].indexOf(","));
                }
                eta = eta + "\n" + route_id[i] + " will arrive in " + Integer.parseInt(etaSecond[i]) / 60 + " minutes.";
            }else if(services.getJSONObject(i).has("next_departures")){
            	etaSecond[i] = null;
            }else{
                etaSecond[i] = null;
            }
        }
        return eta;
    }

	/**
	 *
	 * @param route_id Bus route name
	 * @param busstop Bus stop name
	 * @return Estimate time of arrival of a bus at a busstop
	 * @throws JSONException
	 */
	public static String getBusETA(String route_id, String busstop) throws JSONException{
        HTTP http = new HTTP(Link.getBusStopLink(busstop));
        String info = http.get();
        JSONObject stop = new JSONObject(info);
        JSONArray stops = stop.getJSONArray("stops");
        JSONObject layer = stops.getJSONObject(0);
        JSONArray services = layer.getJSONArray("services");
        String etaSecond = "";
        for(int i = 0; i < services.length(); i++){
            if(route_id.equals(services.getJSONObject(i).getString("route_id"))){
                if(services.getJSONObject(i).has("live_departures_seconds")){
                    etaSecond = services.getJSONObject(i).getString("live_departures_seconds");
                    etaSecond = etaSecond.substring(1, etaSecond.length() - 1);
                    if(etaSecond.contains(",")) {
                        etaSecond = etaSecond.substring(0, etaSecond.indexOf(","));
                    }
                }else if(services.getJSONObject(i).has("headway_seconds_range")){
                    etaSecond = services.getJSONObject(i).getString("headway_seconds_range");
                    etaSecond = etaSecond.substring(1, etaSecond.length() - 1);
                    if(etaSecond.contains(",")) {
                        etaSecond = etaSecond.substring(0, etaSecond.indexOf(","));
                    }
                }else if(services.getJSONObject(i).has("next_departures")){
                	etaSecond = null;
                }else{
                    etaSecond = null;
                }
            }

        }
        return etaSecond;
    }

	/**
	 * Request processing from next Service module.
	 * @return Service state
	 * @throws Exception Exception
	 */
    @Override
    public Service chain() throws Exception {
        return this;
    }
}
