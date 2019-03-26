package com.example.popularmoviespart1;

// sandwich club
public class Movie {

    String title, poster, releaseDate, rated, overview;

    // constructor
    // sandwich app
    public Movie() {
    }

    public Movie(String title, String poster, String releaseDate, String rated, String overview) {
        this.title = title;
        this.poster = poster;
        this.releaseDate = releaseDate;
        this.rated = rated;
        this.overview = overview;
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

    public String getAbout() {
        return overview;
    }

    public void setAbout(String overview) {
        this.overview = overview;
    }
}

