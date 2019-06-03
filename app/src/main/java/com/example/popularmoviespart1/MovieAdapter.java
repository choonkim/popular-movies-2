package com.example.popularmoviespart1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.popularmoviespart1.model.Movie;
import com.example.popularmoviespart1.utils.NetworkHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();

    private List<Movie> mMovieItemList;
    private final Context mContext;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void OnListItemClick(Movie movieItem);
    }

    public MovieAdapter(List<Movie> movieItemList, ListItemClickListener listener, Context context) {

        mMovieItemList = movieItemList;

        mOnClickListener = listener;
        mContext = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movies_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mMovieItemList == null ? 0 : mMovieItemList.size();
    }

    public void setMovieData(List<Movie> movieItemList) {
        mMovieItemList = movieItemList;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView listMovieItemView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            listMovieItemView = itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            Movie movieItem = mMovieItemList.get(listIndex);
            listMovieItemView = itemView.findViewById(R.id.movie_image);
            String posterPathURL = NetworkHelper.buildPosterUrl(movieItem.getPoster());
            try {
                Picasso.with(mContext)
                        .load(posterPathURL)
                        .into(listMovieItemView);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.OnListItemClick(mMovieItemList.get(clickedPosition));
        }
    }

}
