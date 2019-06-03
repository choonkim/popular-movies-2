package com.example.popularmoviespart1.model;

import java.io.Serializable;

// sandwich club
public class Movie implements Serializable {

    String title, poster, releaseDate, rated, overview, id;

    // constructor
    // sandwich app
    public Movie() {
    }

    public Movie(String id, String title, String releaseDate, String rated, String overview, String poster) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.rated = rated;
        this.overview = overview;
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.title = id;
    }

}

