package com.example.try1.model;

public class Explore {

//    String name;
//    Integer imageUrl;
//    String restorantname;

    String link;
    String downloadLink;


    public Explore(String link, String downloadLink) {
        this.link = link;
        this.downloadLink = downloadLink;
    }

    public Explore() {

    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }
}
