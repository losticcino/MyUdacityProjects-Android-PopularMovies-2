package com.rasjdd.ras.popularmoviesstage2.Models.Views;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.rasjdd.ras.popularmoviesstage2.DatabaseFunctions.FavoriteMovieDetails;
import com.rasjdd.ras.popularmoviesstage2.DatabaseFunctions.FavoriteUtilities;
import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.MovieListDetailResponse;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private FavoriteUtilities favoriteUtilities;

    private Bundle bundle;

    public MainViewModel(@NonNull Application application) {
        super(application);
        favoriteUtilities = new FavoriteUtilities(application);
    }

    public LiveData<List<FavoriteMovieDetails>> getFavoriteDetailsList(){
        return favoriteUtilities.getMovieList();
    }

    public ArrayList<MovieListDetailResponse> getMovieList(List<FavoriteMovieDetails> favoriteMovieDetailsList){
        ArrayList<MovieListDetailResponse> movieListDetailResponses = new ArrayList<>();
        for (FavoriteMovieDetails favoriteMovieDetails : favoriteMovieDetailsList) {
            movieListDetailResponses.add(favoriteUtilities.covertMovieListItemFromFavorite(favoriteMovieDetails));
        }
        return movieListDetailResponses;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }
}
