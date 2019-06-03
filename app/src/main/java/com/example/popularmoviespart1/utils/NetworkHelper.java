package com.example.popularmoviespart1.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

// sandwich club
// sunshine app
// various other projects found from the web
public class NetworkHelper {
    final static String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie";
    final static String PARAM_API_KEY = "api_key";
    final static String BASE_URL = "http://image.tmdb.org/t/p/";
    private final static String WIDTH = "w500";

    public static URL buildUrl(String theMovieDbSearchQuery, String apiKey) {
        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon() // this may or may not need to be updated!!!
                .appendEncodedPath(theMovieDbSearchQuery)
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String buildPosterUrl(String poster) {
        String finalPath = BASE_URL + WIDTH + "/" + poster;
        return finalPath;
    }
}

