package com.devahmed.techx.onlineshop.Models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;
    private String userId;
    private String phone;
    private String device_token;
    private double xPos;
    private double yPos;
    private String GpsAddress;
    private String area;
    private String street;
    private String flat;
    private String floor;
    private String building;
    private String nearestLandmark;
    private int points;
    private boolean isBlocked;
    private String nearestBranch;

    public User() {
        this.userId = "";
        this.userId = "";
        this.name = "";
        this.phone = "";
        this.xPos = 0;
        this.yPos = 0;
        this.GpsAddress = "";
        this.area = "";
        this.street = "";
        this.flat = "";
        this.nearestLandmark = "";
        this.points = 0;
        this.isBlocked =false;
        this.nearestBranch = "";
        this.device_token = "";
        this.building = "";
        this.floor = "";
    }

    public User(String name, String userId, String phone, String device_token
            , double xPos, double yPos, String gpsAddress, String area, String street
            , String flat, String nearestLandmark, int points, boolean isBlocked, String nearestBranch) {
        this.name = name;
        this.userId = userId;
        this.phone = phone;
        this.device_token = device_token;
        this.xPos = xPos;
        this.yPos = yPos;
        GpsAddress = gpsAddress;
        this.area = area;
        this.street = street;
        this.flat = flat;
        this.nearestLandmark = nearestLandmark;
        this.points = points;
        this.isBlocked = isBlocked;
        this.nearestBranch = nearestBranch;
    }

    public User(String phone, int points) {
        this.phone = phone;
        this.points = points;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getNearestLandmark() {
        return nearestLandmark;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getNearestBranch() {
        return nearestBranch;
    }

    public void setNearestBranch(String nearestBranch) {
        this.nearestBranch = nearestBranch;
    }

    public boolean getIsBlocked() {
        return this.isBlocked;
    }

    public void setIsBlocked(boolean blocked) {
        this.isBlocked = blocked;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public String getGpsAddress() {
        return GpsAddress;
    }

    public void setGpsAddress(String gpsAddress) {
        GpsAddress = gpsAddress;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public void setnearestLandmark(String nearestLandmark) {
        this.nearestLandmark = nearestLandmark;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("isBlocked" , isBlocked);
        result.put("userId", userId);
        result.put("name" , name);
        result.put("phone", phone);
        result.put("xPos", xPos);
        result.put("yPos" , yPos);
        result.put("GpsAddress" , GpsAddress);
        result.put("area" , area);
        result.put("street" , street);
        result.put("flat" , flat);
        result.put("nearestLandmark" , nearestLandmark);
        result.put("points" , points);
        result.put("nearestBranch" , nearestBranch);
        result.put("device_token" , device_token);
        result.put("floor", floor);
        result.put("building" , building);
        return result;
    }
}
