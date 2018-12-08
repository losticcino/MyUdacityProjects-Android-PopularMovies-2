package com.rasjdd.ras.popularmoviesstage2.DatabaseFunctions;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDAO {

    @Query("SELECT * FROM favMovie ORDER BY release_date")
    List<FavoriteMovieDetails> loadAllFavMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavMovie(FavoriteMovieDetails favoriteMovieDetails);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavMovie(FavoriteMovieDetails favoriteMovieDetails);

    @Delete
    void deleteFavMovie(FavoriteMovieDetails favoriteMovieDetails);

    @Query("SELECT * FROM favMovie WHERE id = :id")
    FavoriteMovieDetails loadFavMovieById(int id);
}
