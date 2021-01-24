package com.devahmed.techx.onlineshop.Models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class SubCategory {
    private String id;
    private String title;
    private String image;
    private String category;
    private boolean inOffer;

    public String getCategory() {
        return category;
    }

    public boolean isInOffer() {
        return inOffer;
    }

    public void setInOffer(boolean inOffer) {
        this.inOffer = inOffer;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public SubCategory() {
    }

    public SubCategory(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("title", title);
        result.put("image", image);
        result.put("category" , category);
        result.put("inOffer" , inOffer);
        return result;
    }
}
