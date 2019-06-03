package com.example.popularmoviespart1.model;

public class Trailer {
    private String id;
    private String name;
    private String key;
    private String url;

    public Trailer() {
        // constructor
    }

    public Trailer(String id, String name, String key){
        this.id = id;
        this.name = name;
        this.key = key;
        this.url = "https://www.youtube.com/watch?v=" + key;
    }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
