package com.devahmed.techx.onlineshop.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class Order {

    private String id;
    private String userId;
    private double totalPrice;
    private Object timeStamp;
    private Map<String , Integer> orderedItemsWithCounts;
    //working on order ... delivering now ... done
    private String orderState;
    //user may want to order now or at a specific time
    private String orderAtTime;
    private int discount;
    private double deliveryCost;

    public Order() {
    }

    public Order(String userId, int totalPrice, Map<String, Integer> orderedItemsWithCounts
            , String orderState
            , String orderAtTime, int discount, int deliveryCost) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.timeStamp = ServerValue.TIMESTAMP;
        this.orderedItemsWithCounts = orderedItemsWithCounts;
        this.orderState = orderState;
        this.orderAtTime = orderAtTime;
        this.discount = discount;
        this.deliveryCost = deliveryCost;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Map<String, Integer> getOrderedItemsWithCounts() {
        return orderedItemsWithCounts;
    }

    public void setOrderedItemsWithCounts(Map<String, Integer> orderedItemsWithCounts) {
        this.orderedItemsWithCounts = orderedItemsWithCounts;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderAtTime() {
        return orderAtTime;
    }

    public void setOrderAtTime(String orderAtTime) {
        this.orderAtTime = orderAtTime;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(int deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("id" , id);
        result.put("totalPrice", totalPrice);
        result.put("timeStamp", timeStamp);
        result.put("orderedItemsWithCounts" , orderedItemsWithCounts);
        result.put("orderState" , orderState);
        result.put("orderAtTime" , orderAtTime);
        result.put("discount" , discount);
        result.put("deliveryCost" , deliveryCost);
        return result;
    }

}
