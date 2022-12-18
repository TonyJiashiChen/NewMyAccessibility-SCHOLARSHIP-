package com.example.try1.model;

public class Explore {

    String name;
    Integer imageUrl;
    String restorantname;

    public Explore(String name, String restorantname, Integer imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.restorantname = restorantname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRestorantname() {
        return restorantname;
    }

    public void setRestorantname(String restorantname) {
        this.restorantname = restorantname;
    }

    public Integer getImageUrl() {return imageUrl;}

    public void setImageUrl(Integer imageUrl) {this.imageUrl = imageUrl;}
}
