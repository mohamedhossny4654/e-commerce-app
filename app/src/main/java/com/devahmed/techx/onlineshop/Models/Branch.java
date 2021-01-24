package com.devahmed.techx.onlineshop.Models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Branch {
    private String id;
    private String name;
    private double mLat , mLong;
    private String gpsLocation;
    private double acceptedOrdersRange;
    private boolean isOpenNow;
    private String openAt;


    public Branch() {
    }

    public Branch(String name, double mLat, double mLong, String gpsLocation, double acceptedOrdersRange) {
        this.name = name;
        this.mLat = mLat;
        this.mLong = mLong;
        this.gpsLocation = gpsLocation;
        this.acceptedOrdersRange = acceptedOrdersRange;
        this.isOpenNow = true;
    }

    public String getOpenAt() {
        return openAt;
    }

    public void setOpenAt(String openAt) {
        this.openAt = openAt;
    }

    public boolean getIsOpenNow() {
        return isOpenNow;
    }

    public void setOpenNow(boolean openNow) {
        isOpenNow = openNow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getmLat() {
        return mLat;
    }

    public void setmLat(double mLat) {
        this.mLat = mLat;
    }

    public double getmLong() {
        return mLong;
    }

    public void setmLong(double mLong) {
        this.mLong = mLong;
    }

    public String getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(String gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public double getAcceptedOrdersRange() {
        return acceptedOrdersRange;
    }

    public void setAcceptedOrdersRange(double acceptedOrdersRange) {
        this.acceptedOrdersRange = acceptedOrdersRange;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("mLat", mLat);
        result.put("mLong" , mLong);
        result.put("gpsLocation" ,gpsLocation);
        result.put("acceptedOrdersRange" , acceptedOrdersRange);
        result.put("isOpenNow" , isOpenNow);
        result.put("openAt" , openAt);
        return result;
    }
}
