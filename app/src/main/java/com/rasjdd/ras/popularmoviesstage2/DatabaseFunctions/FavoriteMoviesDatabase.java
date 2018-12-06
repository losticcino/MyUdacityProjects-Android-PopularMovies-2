package com.rasjdd.ras.popularmoviesstage2.DatabaseFunctions;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {FavoriteMovieDetails.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class FavoriteMoviesDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String FavMovDatabaseName = "FavoriteMovies";
    private static FavoriteMoviesDatabase favoriteMoviesDatabase;

    public static FavoriteMoviesDatabase getInstance(Context context) {
        if (favoriteMoviesDatabase == null) {
            synchronized (LOCK) {
                favoriteMoviesDatabase = Room.databaseBuilder(context.getApplicationContext(),
                FavoriteMoviesDatabase.class, FavoriteMoviesDatabase.FavMovDatabaseName)
                .build();
            }
        }
        return favoriteMoviesDatabase;
    }

    public abstract MovieDAO movieDAO();
}
