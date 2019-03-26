package com.example.popularmoviespart1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {

    // http://jakewharton.github.io/butterknife/
    // combination of other examples found on the web
    // first time discovering and using butterknife. pretty slick :)
    @BindView(R.id.details_movie_image)
    ImageView movieImage;
    @BindView(R.id.details_movie_title)
    TextView movieTitle;
    @BindView(R.id.details_movie_rating)
    TextView movieRating;
    @BindView(R.id.details_movie_releaseDate)
    TextView movieReleaseDate;
    @BindView(R.id.details_movie_plot)
    TextView moviePlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        ButterKnife.bind(this);

        String poster = getIntent().getStringExtra("poster");
        String title = getIntent().getStringExtra("title");
        String rating = getIntent().getStringExtra("rate");
        String release = getIntent().getStringExtra("release");
        String overview = getIntent().getStringExtra("overview");

        movieTitle.setText(title);
        moviePlot.setText(overview);
        movieRating.setText(rating);
        movieReleaseDate.setText(release);
        Picasso.get()
                .load(poster)
                .into(movieImage);
    }
}

