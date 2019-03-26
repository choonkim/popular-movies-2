package com.example.popularmoviespart1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

// sandwich club
// sunshine
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Movie[] mMovies;
    private MovieOnClickHandler mMovieOnClickHandler;

    public MovieAdapter(Movie[] movie, MovieOnClickHandler clickHandler) {
        mMovies = movie;
        mMovieOnClickHandler = clickHandler;
    }

    public interface MovieOnClickHandler {
        void onClick(int position);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mMovieOnClickHandler.onClick(adapterPosition);
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutForListItem = R.layout.movies_list;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutForListItem, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
        String movie = mMovies[position].getPoster();
        Picasso.get().load(movie).into(movieViewHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        if(null == mMovies) {
            return 0;
        }
        return mMovies.length;
    }

}

