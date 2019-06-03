package com.example.popularmoviespart1.model;

public class Review {
    private String auth;
    private String content;
    private String id;
    private String url;

    public Review() {
        // constructor
    }

    public Review(String auth, String content, String id, String url) {
        this.auth = auth;
        this.content = content;
        this.id = id;
        this.url = url;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getAuth() { return auth; }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
