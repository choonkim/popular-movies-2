package com.example.popularmoviespart1.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName="FavMovies")
public class FavoriteMovie {

    @PrimaryKey
    private int id;
    private String title;
    private String releaseDate;
    private String rated;
    private String overview;
    private String poster;

    public FavoriteMovie(int id, String title, String releaseDate, String rated, String overview, String poster) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.rated = rated;
        this.overview = overview;
        this.poster = poster;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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

    public String getPoster() {
        return poster;
    }
    public void setPoster(String poster) {
        this.poster = poster;
    }

}
