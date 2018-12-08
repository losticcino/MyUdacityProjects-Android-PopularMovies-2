package com.rasjdd.ras.popularmoviesstage2.DatabaseFunctions;

import android.app.Application;

import com.rasjdd.ras.popularmoviesstage2.Models.FavoriteList;
import com.rasjdd.ras.popularmoviesstage2.Models.MovieDetails;
import com.rasjdd.ras.popularmoviesstage2.Utilities.AppExecutors;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FavoriteUtilities {
    private MovieDAO movieDAO;
    private AppExecutors appExecutors;

    public FavoriteUtilities(Application application) {
        movieDAO = FavoriteMoviesDatabase.getInstance(application).movieDAO();
        appExecutors = AppExecutors.getExecInstance();
    }

    public boolean movieIsFavorited(int id) {
        FavoriteMovieDetails favoriteMovie = movieDAO.loadFavMovieById(id);
        return favoriteMovie != null;
    }

    public FavoriteMovieDetails convertToFavoriteMovie(MovieDetails movieDetails) {
        FavoriteMovieDetails favoriteMovie = new FavoriteMovieDetails();

        // Convert the date string to something more program friendly.
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(movieDetails.getReleaseDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        favoriteMovie.setId(movieDetails.getId());
        favoriteMovie.setAdult(movieDetails.isAdult());
        favoriteMovie.setReleaseDate(date);
        favoriteMovie.setBackdropPath(movieDetails.getBackdropPath());
        favoriteMovie.setBudget(movieDetails.getBudget());
        favoriteMovie.setHomepage(movieDetails.getHomepage());
        favoriteMovie.setImdbId(movieDetails.getImdbId());
        favoriteMovie.setOriginalLanguage(movieDetails.getOriginalLanguage());
        favoriteMovie.setOriginalTitle(movieDetails.getOriginalTitle());
        favoriteMovie.setOverview(movieDetails.getOverview());
        favoriteMovie.setPopularity(movieDetails.getPopularity());
        favoriteMovie.setPosterPath(movieDetails.getPosterPath());
        favoriteMovie.setRevenue(movieDetails.getRevenue());
        favoriteMovie.setRuntime(movieDetails.getRuntime());
        favoriteMovie.setStatus(movieDetails.getStatus());
        favoriteMovie.setTagline(movieDetails.getTagline());
        favoriteMovie.setTitle(movieDetails.getTitle());
        favoriteMovie.setVoteAverage(movieDetails.getVoteAverage());
        favoriteMovie.setVoteCount(movieDetails.getVoteCount());

        return favoriteMovie;

    }

    public FavoriteList getAllFavorites(){
        List<FavoriteMovieDetails> favoriteMovieDetailsList = movieDAO.loadAllFavMovies();
        ArrayList<FavoriteMovieDetails> favMovArLst = null;
        FavoriteList mFavs = new FavoriteList();
        if (null != favoriteMovieDetailsList) {
            for (FavoriteMovieDetails favoriteMovieDetails : favoriteMovieDetailsList) {
                favMovArLst.add(favoriteMovieDetails);
            }
            int mMovies = favoriteMovieDetailsList.size();
            mFavs.setTotal_pages(1);
            mFavs.setTotal_pages(1);
            mFavs.setTotal_results(mMovies);
            mFavs.setResults(favMovArLst);
        }

        return mFavs;
    }

    public void addFavorite(FavoriteMovieDetails favoriteMovieDetails) {
        appExecutors.getDiskIO().execute(() -> movieDAO.insertFavMovie(favoriteMovieDetails));
    }

    public void deleteFavorite(FavoriteMovieDetails favoriteMovieDetails) {
        appExecutors.getDiskIO().execute(() -> movieDAO.deleteFavMovie(favoriteMovieDetails));
    }
}
