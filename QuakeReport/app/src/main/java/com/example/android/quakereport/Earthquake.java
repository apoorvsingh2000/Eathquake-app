package com.example.android.quakereport;

public class Earthquake {
    private double mMagnitude;
    private String mLocation;
    private Long mDate;
    private String mUrl;

    //Constructor to initialize the global variables
    public Earthquake(double magnitude, String location, Long date, String url){
        mMagnitude=magnitude;
        mLocation=location;
        mDate=date;
        mUrl=url;
    }

    //get the magnitude of the earthquake
    public double getMagnitude(){
        return mMagnitude;
    }

    //get the location of the earthquake
    public String getLocation(){
        return mLocation;
    }

    //get the date of the earthquake
    public long getTimeInMilliseconds(){
        return mDate;
    }

    //get the url of the earthquake
    public String getUrl(){
        return mUrl;
    }
}

