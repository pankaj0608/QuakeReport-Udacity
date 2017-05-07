package com.example.android.quakereport;

import java.util.Date;

/**
 * Created by pankaj on 5/4/2017.
 */
public class Earthquake {

    String magnitude;
    String location;
    Date time;
    String url;

    Earthquake(String aMagnitude, String aLocation, Date aTime, String aUrl) {

        this.magnitude = aMagnitude;
        this.location = aLocation;
        this.time = aTime;
        this.url = aUrl;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    public Date getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }
}
