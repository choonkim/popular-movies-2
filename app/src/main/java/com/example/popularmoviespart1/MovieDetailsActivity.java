package com.example.popularmoviespart1;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmoviespart1.database.FavoriteMovie;
import com.example.popularmoviespart1.database.MovieDataBase;
import com.example.popularmoviespart1.model.Movie;
import com.example.popularmoviespart1.model.Review;
import com.example.popularmoviespart1.model.Trailer;
import com.example.popularmoviespart1.utils.JsonUtils;
import com.example.popularmoviespart1.utils.NetworkHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener {

    private Movie movieItem;
    private ArrayList<Review> reviewList;
    private ArrayList<Trailer> trailerList;

    private RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private MovieDataBase mDb;
    // private ImageView mFavButton;
    private Boolean isFav = false;

    @BindView(R.id.add_to_favorites)
    TextView favorites;

    private static final String TAG = "MovieDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError("Intent is null");
        }

        movieItem = (Movie) intent.getSerializableExtra("movieItem");
        if (movieItem == null) {
            closeOnError("An error has occurred.");
            return;
        }

        // Recycler view for trailers
        mTrailerRecyclerView = findViewById(R.id.recyclerview_trailers);
        mTrailerAdapter = new TrailerAdapter(this, trailerList, this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mTrailerRecyclerView.setLayoutManager(mLayoutManager);

        // favorite movies
        favorites = findViewById(R.id.add_to_favorites);
        mDb = MovieDataBase.getInstance(getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final FavoriteMovie fmov = mDb.movieDao().loadMovieById(Integer.parseInt(movieItem.getId()));
                setFavorite((fmov != null) ? true : false);
            }
        });

        // fetch data
        getMoreDetails(movieItem.getId());
    }

    private void setFavorite(Boolean fav) {
        if (fav) {
            isFav = true;
            favorites.setText("Remove from Favorites");
        } else {
            isFav = false;
            favorites.setText("Add to Favorites");
        }
    }

    private static class SearchURLs {
        URL reviewSearchUrl;
        URL trailerSearchUrl;

        SearchURLs(URL reviewSearchUrl, URL trailerSearchUrl) {
            this.reviewSearchUrl = reviewSearchUrl;
            this.trailerSearchUrl = trailerSearchUrl;
        }
    }

    private static class ResultsStrings {
        String reviewString;
        String trailerString;

        ResultsStrings(String reviewString, String trailerString) {
            this.reviewString = reviewString;
            this.trailerString = trailerString;
        }
    }

    private void getMoreDetails(String id) {
        String reviewQuery = id + File.separator + "reviews";
        String trailerQuery = id + File.separator + "videos";
        SearchURLs searchURLs = new SearchURLs(
                NetworkHelper.buildUrl(reviewQuery, getText(R.string.api_key).toString()),
                NetworkHelper.buildUrl(trailerQuery, getText(R.string.api_key).toString())
        );
        new ReviewsQueryTask().execute(searchURLs);
    }

    // AsyncTask to perform query
    public class ReviewsQueryTask extends AsyncTask<SearchURLs, Void, ResultsStrings> {
        @Override
        protected ResultsStrings doInBackground(SearchURLs... params) {
            URL reviewsearchUrl = params[0].reviewSearchUrl;
            URL trailersearchUrl = params[0].trailerSearchUrl;

            String reviewResults = null;
            try {
                reviewResults = NetworkHelper.getResponseFromHttpUrl(reviewsearchUrl);
                reviewList = JsonUtils.parseReviewsJson(reviewResults);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String trailerResults = null;
            try {
                trailerResults = NetworkHelper.getResponseFromHttpUrl(trailersearchUrl);
                trailerList = JsonUtils.parseTrailersJson(trailerResults);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ResultsStrings results = new ResultsStrings(reviewResults, trailerResults);

            return results;
        }

        @Override
        protected void onPostExecute(ResultsStrings results) {
            String searchResults = results.reviewString;
            if (searchResults != null && !searchResults.equals("")) {
                reviewList = JsonUtils.parseReviewsJson(searchResults);
                populateDetails();
            }
        }
    }

    private void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        webIntent.putExtra("finish_on_ended", true);
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    @Override
    public void OnListItemClick(Trailer trailerItem) {
        watchYoutubeVideo(trailerItem.getKey());
    }

    private void populateDetails() {

        ((TextView) findViewById(R.id.details_movie_title)).setText(movieItem.getTitle());
        ((TextView) findViewById(R.id.details_movie_rating)).append(" (" + movieItem.getRated() + "/10)");
        ((TextView) findViewById(R.id.details_movie_releaseDate)).setText(movieItem.getReleaseDate());
        ((TextView) findViewById(R.id.details_movie_plot)).setText(movieItem.getOverview());

        // Favorite
        favorites.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                final FavoriteMovie mov = new FavoriteMovie(
                        Integer.parseInt(movieItem.getId()),
                        movieItem.getTitle(),
                        movieItem.getReleaseDate(),
                        movieItem.getRated(),
                        movieItem.getOverview(),
                        movieItem.getPoster()
                );
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (isFav) {
                            // delete item
                            mDb.movieDao().deleteMovie(mov);
                        } else {
                            // insert item
                            mDb.movieDao().insertMovie(mov);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setFavorite(!isFav);
                            }
                        });
                    }

                });
            }
        });


        // Trailers
        mTrailerAdapter.setTrailerData(trailerList);

        // Reviews
        ((TextView) findViewById(R.id.tv_reviews)).setText("");
        for (int i = 0; i < reviewList.size(); i++) {
            ((TextView) findViewById(R.id.tv_reviews)).append("\n");
            ((TextView) findViewById(R.id.tv_reviews)).append(reviewList.get(i).getContent());
            ((TextView) findViewById(R.id.tv_reviews)).append("\n\n");
            ((TextView) findViewById(R.id.tv_reviews)).append(" - Reviewed by ");
            ((TextView) findViewById(R.id.tv_reviews)).append(reviewList.get(i).getAuth());
            ((TextView) findViewById(R.id.tv_reviews)).append("\n\n--------------\n");
        }

        String imagePathURL = NetworkHelper.buildPosterUrl(movieItem.getPoster());

        try {
            Picasso.with(this)
                    .load(imagePathURL)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into((ImageView) this.findViewById(R.id.details_movie_image));
        } catch (Exception ex) {
            Log.d(TAG, "populateDetails: Error loading poster image");
        }
    }

    private void closeOnError(String msg) {
        finish();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

