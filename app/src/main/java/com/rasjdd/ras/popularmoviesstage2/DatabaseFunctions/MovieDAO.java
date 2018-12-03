package com.rasjdd.ras.popularmoviesstage2.DatabaseFunctions;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.v4.view.animation.FastOutLinearInInterpolator;

import java.util.List;

@Dao
public interface MovieDAO {

    @Query("SELECT * FROM FavMovie ORDER BY DateAdded")
    LiveData<List<FavoriteMovieDetails>> loadAllFavMovies();

    @Insert
    void insertFavMovie(FavoriteMovieDetails favoriteMovieDetails);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavMovie(FavoriteMovieDetails favoriteMovieDetails);

    @Delete
    void deleteFavMovie(FavoriteMovieDetails favoriteMovieDetails);

    @Query("SELECT * FROM FavMovie WHERE id = :id")
    LiveData<FavoriteMovieDetails> loadFavMovieById(int id);
}
