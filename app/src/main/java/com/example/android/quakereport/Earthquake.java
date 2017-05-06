package com.example.android.quakereport;

import java.util.Date;

/**
 * Created by pankaj on 5/4/2017.
 */
public class Earthquake {

    String magnitude;
    String location;
    Date time;

    Earthquake(String aMagnitude, String aLocation, Date aTime) {

        this.magnitude = aMagnitude;
        this.location = aLocation;
        this.time = aTime;
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
}
