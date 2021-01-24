package com.devahmed.techx.onlineshop.Models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Notification {
    private String title;
    private String body;
    private String photoUrl;


    public Notification() {
    }


    public Notification(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("body", body);
        result.put("photoUrl", photoUrl);
        return result;
    }
}
