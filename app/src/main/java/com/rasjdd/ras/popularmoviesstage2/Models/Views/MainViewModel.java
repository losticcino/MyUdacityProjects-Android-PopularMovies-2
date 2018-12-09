package com.rasjdd.ras.popularmoviesstage2.Models.Views;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.rasjdd.ras.popularmoviesstage2.DatabaseFunctions.FavoriteMovieDetails;
import com.rasjdd.ras.popularmoviesstage2.DatabaseFunctions.FavoriteUtilities;
import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.MovieListDetailResponse;
import com.rasjdd.ras.popularmoviesstage2.Models.MovieList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    public List<FavoriteMovieDetails> favMovsList;
    public FavoriteUtilities favoriteUtilities;

    public MainViewModel(@NonNull Application application) {
        super(application);
        favoriteUtilities = new FavoriteUtilities(application);
        favMovsList = favoriteUtilities.getMovieList();
    }

    public MovieList getFavoriteList(){

        ArrayList<MovieListDetailResponse> favMovArLst = new ArrayList<>();
        MovieList mFavs = new MovieList();

        if (null != favMovsList) {
            for (FavoriteMovieDetails favMov : favMovsList) {

                // Convert the date string to something more program friendly.
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String s = "";
                s = dateFormat.format(favMov.getReleaseDate());

                MovieListDetailResponse movie = new MovieListDetailResponse();

                int i = favMov.getId();

                movie.setId(i);
                movie.setAdult(favMov.isAdult());
                movie.setRelease_date(s);
                movie.setBackdrop_path(favMov.getBackdropPath());
                movie.setOriginal_language(favMov.getOriginalLanguage());
                movie.setPoster_path(favMov.getPosterPath());
                movie.setTitle(favMov.getTitle());

                favMovArLst.add(movie);
            }
            int mMovies = favMovsList.size();
            mFavs.setTotal_pages(1);
            mFavs.setTotal_pages(1);
            mFavs.setTotal_results(mMovies);
            mFavs.setResults(favMovArLst);
        }

        return mFavs;
    }
}
