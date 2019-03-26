package com.example.popularmoviespart1;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieMetaData {
    public static Movie[] getMovieMetaData(Context context, String json) throws JSONException {

        final String TMDB_BASE_URL = "https://image.tmdb.org/t/p/";
        final String TMDB_POSTER_SIZE = "w500";

        JSONObject movieJson = new JSONObject(json);

        JSONArray movieArray = movieJson.getJSONArray("results");

        Movie[] movieResults = new Movie[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {
            String poster_path, title, vote_average, overview, release_date;

            Movie movie = new Movie();

            poster_path = movieArray.getJSONObject(i).optString("poster_path");
            title = movieArray.getJSONObject(i).optString("title");
            release_date = movieArray.getJSONObject(i).optString("release_date");
            vote_average = movieArray.getJSONObject(i).optString("vote_average");
            overview = movieArray.getJSONObject(i).optString("overview");

            movie.setPoster(TMDB_BASE_URL + TMDB_POSTER_SIZE + poster_path);
            movie.setTitle(title);
            movie.setReleaseDate(release_date);
            movie.setRated(vote_average);
            movie.setAbout(overview);
            movieResults[i] = movie;
        }
        return movieResults;
    }
}

