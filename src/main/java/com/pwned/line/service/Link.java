package com.pwned.line.service;

public class Link {
    public static String HKUST_SOUTH_GATE = "https://citymapper.com/api/1/departures?headways=1&ids=HKStop_HkustSouth_NW_1&region_id=hk-hongkong";
    public static String HKUST_NORTH_GATE = "https://citymapper.com/api/1/departures?headways=1&ids=HKStop_XiangGangKeJiDaXueHkust&region_id=hk-hongkong";

    public static String getBusStopLink(String busstop){
        if(busstop.equals("HKUST South Gate")){
            return HKUST_SOUTH_GATE;
        }else if(busstop.equals("HKUST North Gate")){
            return HKUST_NORTH_GATE;
        }else{
            return null;
        }
    }
}
