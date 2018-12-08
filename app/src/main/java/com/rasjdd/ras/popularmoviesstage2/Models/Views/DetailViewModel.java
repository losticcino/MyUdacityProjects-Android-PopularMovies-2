package com.rasjdd.ras.popularmoviesstage2.Models.Views;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.rasjdd.ras.popularmoviesstage2.DatabaseFunctions.FavoriteMovieDetails;
import com.rasjdd.ras.popularmoviesstage2.DatabaseFunctions.FavoriteUtilities;
import com.rasjdd.ras.popularmoviesstage2.Models.MovieDetails;

public class DetailViewModel extends AndroidViewModel {

    private FavoriteUtilities favoriteUtilities;

    public DetailViewModel(@NonNull Application application){
        super(application);
        favoriteUtilities = new FavoriteUtilities(application);
    }

    public boolean isFavorited(int movieID) {
        return favoriteUtilities.movieIsFavorited(movieID);
    }

    public boolean addFavorite(MovieDetails movieDetails) {
        FavoriteMovieDetails favoriteMovieDetails = favoriteUtilities.convertToFavoriteMovie(movieDetails);
        favoriteUtilities.addFavorite(favoriteMovieDetails);
        return isFavorited(movieDetails.getId());
    }

    public boolean delFavorite(MovieDetails movieDetails) {
        FavoriteMovieDetails favoriteMovieDetails = favoriteUtilities.convertToFavoriteMovie(movieDetails);
        favoriteUtilities.deleteFavorite(favoriteMovieDetails);
        return !isFavorited(movieDetails.getId());

    }
}
