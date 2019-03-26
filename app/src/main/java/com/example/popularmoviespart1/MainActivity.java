package com.example.popularmoviespart1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

// sandwich club
// sunshine app
// various other projects found from the web
public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickHandler {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private Movie[] mMovies;

    @BindView(R.id.no_data_error_text)
    TextView errorMessage;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    String query = "popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_movies);

        ButterKnife.bind(this);

        int numberOfColumsToDisplay = calculateNoOfColumns(getApplicationContext());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumsToDisplay);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);

        loadMovieData();
    }

    private void loadMovieData() {
        String movieQuery = query;
        showJsonData();
        new FetchMovie().execute(movieQuery);
    }

    @Override
    public void onClick(int adapterPosition) {
        Context context = this;
        Class destinationClass = MovieDetailsActivity.class;

        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, adapterPosition);
        intentToStartDetailActivity.putExtra("title", mMovies[adapterPosition].getTitle());
        intentToStartDetailActivity.putExtra("poster", mMovies[adapterPosition].getPoster());
        intentToStartDetailActivity.putExtra("rate", mMovies[adapterPosition].getRated());
        intentToStartDetailActivity.putExtra("release", mMovies[adapterPosition].getReleaseDate());
        intentToStartDetailActivity.putExtra("overview", mMovies[adapterPosition].getAbout());

        startActivity(intentToStartDetailActivity);
    }

    private void showJsonData() {
        errorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showError() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    public class FetchMovie extends AsyncTask<String, Void, Movie[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String sortBy = params[0];
            URL movieRequestUrl = NetworkHelper.buildUrl(sortBy);

            try {
                String jsonMovieResponse = NetworkHelper.getResponseFromHttpUrl(movieRequestUrl);

                mMovies = MovieMetaData.getMovieMetaData(MainActivity.this, jsonMovieResponse);

                return mMovies;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (movies != null) {
                showJsonData();
                mMovieAdapter = new MovieAdapter(movies, MainActivity.this);
                mRecyclerView.setAdapter(mMovieAdapter);
            } else {
                showError();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();

        if (menuItemSelected == R.id.popular_movies) {
            query = "popular";
            loadMovieData();
            return true;
        }

        if (menuItemSelected == R.id.top_movies) {
            query = "top_rated";
            loadMovieData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // https://github.com/03rgun/Popular-Movies-1/blob/master/app/src/main/java/com/example/oktay/popularmovies1/MainActivity.java#L173
    // via https://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
}

