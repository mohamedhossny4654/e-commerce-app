package com.devahmed.techx.onlineshop.Models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class DeliverCost {
    private String id;
    private int from;
    private int to;
    private int price;


    public DeliverCost() {
    }

    public DeliverCost(String id, int from, int to, int price) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.price = price;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("from", from);
        result.put("to", to);
        result.put("price", price);
        return result;
    }
}
