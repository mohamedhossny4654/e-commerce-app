package com.devahmed.techx.onlineshop.Models;

public class ProductSubCategory {
    public static final int TYPE_PRODUCT = 0;
    public static final int TYPE_SUB_CATEGORY = 1;
    //a mix between product and subCategory
    private String id;
    private String title;
    private String subCategory;
    private String image;
    private double price;
    private int type; // 0 => product ,  1 => subCategory

    public ProductSubCategory() {
    }

    public ProductSubCategory(String id, String title, String subCategory, String image, int price, int type) {
        this.id = id;
        this.title = title;
        this.subCategory = subCategory;
        this.image = image;
        this.price = price;
        this.type = type;
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

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
