package com.example.popularmoviespart1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.popularmoviespart1.database.FavoriteMovie;
import com.example.popularmoviespart1.model.Movie;
import com.example.popularmoviespart1.utils.JsonUtils;
import com.example.popularmoviespart1.utils.NetworkHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// sandwich club
// sunshine app
// various other projects found from the web
//
public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ArrayList<Movie> movieList;
    private static final String CALLBACKS = "callbacks";

    @BindView(R.id.no_data_error_text)
    TextView errorMessage;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    String query = "popular";
    private List<FavoriteMovie> favMovs;
    private static final String SORT_POPULAR = "popular";
    private static final String SORT_TOP_RATED = "top_rated";
    private static final String SORT_FAVORITE = "favorite";
    private static String currentSort = SORT_POPULAR;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            query = savedInstanceState.getString(CALLBACKS);
        }

        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_movies);

        ButterKnife.bind(this);

        int numberOfColumsToDisplay = calculateNoOfColumns(getApplicationContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumsToDisplay);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(movieList, this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        favMovs = new ArrayList<>();



        setupViewModel();
    }

    private void loadMovies() {
        makeMovieSearchQuery();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.popular_movies && !currentSort.equals(SORT_POPULAR)) {
            ClearMovieItemList();
            currentSort = SORT_POPULAR;
            setTitle(getString(R.string.app_name) + " - Popular");
            loadMovies();
            return true;
        }
        if (id == R.id.top_movies && !currentSort.equals(SORT_TOP_RATED)) {
            ClearMovieItemList();
            currentSort = SORT_TOP_RATED;
            setTitle(getString(R.string.app_name) + " - Top rated");
            loadMovies();
            return true;
        }
        if (id == R.id.favorite_movies && !currentSort.equals(SORT_FAVORITE)) {
            ClearMovieItemList();
            currentSort = SORT_FAVORITE;
            setTitle(getString(R.string.app_name) + " - Favorite");
            loadMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ClearMovieItemList() {
        if (movieList != null) {
            movieList.clear();
        } else {
            movieList = new ArrayList<Movie>();
        }
    }

    private void makeMovieSearchQuery() {
        if (currentSort.equals(SORT_FAVORITE)) {
            ClearMovieItemList();
            for (int i = 0; i< favMovs.size(); i++) {
                Movie mov = new Movie(
                        String.valueOf(favMovs.get(i).getId()),
                        favMovs.get(i).getTitle(),
                        favMovs.get(i).getReleaseDate(),
                        favMovs.get(i).getRated(),
                        favMovs.get(i).getOverview(),
                        favMovs.get(i).getPoster()
                );
                movieList.add( mov );
            }
            mMovieAdapter.setMovieData(movieList);
        } else {
            String movieQuery = currentSort;
            URL movieSearchUrl = NetworkHelper.buildUrl(movieQuery, getText(R.string.api_key).toString());
            new MoviesQueryTask().execute(movieSearchUrl);
        }
    }

    public class MoviesQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String searchResults = null;
            try {
                searchResults = NetworkHelper.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            if (searchResults != null && !searchResults.equals("")) {
                movieList = JsonUtils.parseMoviesJson(searchResults);
                mMovieAdapter.setMovieData(movieList);
            }
        }
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovie> favs) {
                if(favs.size()>0) {
                    favMovs.clear();
                    favMovs = favs;
                }
                for (int i=0; i<favMovs.size(); i++) {
                    Log.d(TAG,favMovs.get(i).getTitle());
                }
                loadMovies();
            }
        });
    }

    // https://github.com/03rgun/Popular-Movies-1/blob/master/app/src/main/java/com/example/oktay/popularmovies1/MainActivity.java#L173
    // via https://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    @Override
    public void OnListItemClick(Movie movieItem) {
        Intent myIntent = new Intent(this, MovieDetailsActivity.class);
        myIntent.putExtra("movieItem", movieItem);
        startActivity(myIntent);
    }
}

