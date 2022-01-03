package com.example.books_management.entity;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.sql.Blob;

public class Book implements Serializable {
    private String id;
    private String name;
    private String category;
    private String author;
    private String press;
    private Float price;
    private Float recommendation;
    private String introduction;
    private byte[] profile;

    public Book(String id, String name, String category, String author, String press, Float price, Float recommendation, String introduction, byte[] profile) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.author = author;
        this.press = press;
        this.price = price;
        this.recommendation = recommendation;
        this.introduction = introduction;
        this.profile = profile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Float recommendation) {
        this.recommendation = recommendation;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public byte[] getProfile() {
        return profile;
    }

    public void setProfile(byte[] profile)  {
        this.profile = profile;
    }
}
