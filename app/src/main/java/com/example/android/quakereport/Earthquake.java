package com.example.android.quakereport;

import java.util.Date;

/**
 * Created by pankaj on 5/4/2017.
 */
public class Earthquake {

    String intensity;
    String location;
    Date time;

    Earthquake(String aIntensity, String aLocation, Date aTime) {

        this.intensity = aIntensity;
        this.location = aLocation;
        this.time = aTime;
    }

    public String getIntensity() {
        return intensity;
    }

    public String getLocation() {
        return location;
    }

    public Date getTime() {
        return time;
    }
}
