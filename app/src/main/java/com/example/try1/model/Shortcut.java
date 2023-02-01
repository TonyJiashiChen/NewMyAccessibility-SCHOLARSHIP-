package com.example.try1.model;

public class Shortcut {
    String name;
    //Integer imageUrl;
    String restorantname;

    String screenSize;
    String actions;
//    String height;
//    String width;

    public Shortcut(String name, String restorantname, String screenSize, String actions) {
        this.name = name;
        //this.imageUrl = imageUrl;
        this.restorantname = restorantname;
        this.screenSize = screenSize;
        this.actions = actions;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
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

    //public Integer getImageUrl() {return imageUrl;}

    //public void setImageUrl(Integer imageUrl) {this.imageUrl = imageUrl;}
}
