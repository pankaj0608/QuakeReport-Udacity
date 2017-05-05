package com.example.android.quakereport;

/**
 * Created by pankaj on 5/4/2017.
 */
public class Earthquake {

    String intensity;
    String location;
    String time;

    Earthquake(String aIntensity, String aLocation, String aTime) {

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

    public String getTime() {
        return time;
    }
}
