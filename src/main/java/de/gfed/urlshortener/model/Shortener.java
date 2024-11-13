package de.gfed.urlshortener.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class Shortener {


    public static final int ID_LENGTH=6;
    static final String localURL="http://localhost:8080/";

    private String shortID;
    @Id
    private String url;


    public Shortener(String url, String shortID){
        this.url = url;
        this.shortID = shortID;
    }

    public Shortener() {}

    public String getFullURL(){
        return localURL+shortID;
    }

    public String getOriginalURL(){
        return url;
    }

    public void setShortID(String key) {
        this.shortID = key;
    }

    public String getShortID() {
        return shortID;
    }

}
