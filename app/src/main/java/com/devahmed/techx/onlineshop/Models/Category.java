package com.devahmed.techx.onlineshop.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class Category {
    String  id;
    String title;
    String image;
    Object timeStamp;

    public Category() {
    }

    public Category(String title, String image) {
        this.title = title;
        this.image = image;
        this.timeStamp = ServerValue.TIMESTAMP;
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
        result.put("timeStamp" , timeStamp);
        return result;
    }
}
