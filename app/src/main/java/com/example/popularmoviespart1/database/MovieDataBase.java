package com.example.popularmoviespart1.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {FavoriteMovie.class}, version = 3, exportSchema = false)
public abstract class MovieDataBase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movieslist";
    private static MovieDataBase sInstance;

    public static MovieDataBase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDataBase.class, MovieDataBase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract MovieDao movieDao();
}
