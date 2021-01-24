package com.devahmed.techx.onlineshop.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class Product implements Comparable<Product> {

    private String id;
    private String title;
    private double price;
    private String image;
    private String subCategory;
    private Object timeStamp;
    private String branch;
    private int maxCount;
    private int points;


    public Product() {
    }

    public Product(String title, double price, String image, String subCategory) {
        this.title = title;
        this.price = price;
        this.image = image;
        this.subCategory = subCategory;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        this.title = mTitle;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("title", title);
        result.put("image", image);
        result.put("price" , price);
        result.put("subCategory" , subCategory);
        result.put("timeStamp" , timeStamp);
        result.put("branch" , branch);
        result.put("maxCount" , maxCount);
        result.put("points" , points);
        return result;
    }

    @Override
    public int compareTo(Product o) {
        if(((long)getTimeStamp()) > ((long)o.getTimeStamp()))
            return 1;
        return 0;
    }
}
