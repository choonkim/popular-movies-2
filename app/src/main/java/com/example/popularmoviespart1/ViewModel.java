package com.example.popularmoviespart1;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.popularmoviespart1.database.FavoriteMovie;
import com.example.popularmoviespart1.database.MovieDataBase;

import java.util.List;

public class ViewModel extends AndroidViewModel {

    private LiveData<List<FavoriteMovie>> movies;

    public ViewModel(@NonNull Application application) {
        super(application);

        MovieDataBase database = MovieDataBase.getInstance(this.getApplication());
        movies = database.movieDao().loadAllMovies();
    }
}
