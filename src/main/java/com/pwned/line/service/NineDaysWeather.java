package com.pwned.line.service;

import com.pwned.line.job.PushNineDaysWeather;

/***
 * Service for course information.
 * Required params: [uid]
 * Reserved tokens: [@weather::ninedaysweather]
 * Resolved params: []
 * @author Timothy Pak
 */

public class NineDaysWeather extends DefaultService{

    public NineDaysWeather(Service service){
        super(service);
    }

    @Override
    public void payload() throws Exception{
        this.fulfillment = this.fulfillment.replace("@weather::ninedaysweather", "The above is the weather forecast for next 9 days:");
        PushNineDaysWeather.nineDaysWeather(this.getParam("uid").toString());
    }


    @Override
    public Service chain() throws Exception {
        return this;
    }
}
