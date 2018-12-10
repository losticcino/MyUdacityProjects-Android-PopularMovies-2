package com.rasjdd.ras.popularmoviesstage2.DatabaseFunctions;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.MovieListDetailResponse;
import com.rasjdd.ras.popularmoviesstage2.Models.MovieDetails;
import com.rasjdd.ras.popularmoviesstage2.Utilities.AppExecutors;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FavoriteUtilities {
    private MovieDAO movieDAO;
    private AppExecutors appExecutors;
    private FavoriteMovieDetails favoriteMovieDetails;

    public FavoriteUtilities(Application application) {
        movieDAO = FavoriteMoviesDatabase.getInstance(application).movieDAO();
        appExecutors = AppExecutors.getExecInstance();
    }

    public boolean movieIsFavorited(int id) {
        FavoriteMovieDetails favoriteMovie = movieDAO.loadFavMovieById(id);
        return favoriteMovie != null;
    }

    public LiveData<List<FavoriteMovieDetails>> getMovieList() {
        return movieDAO.loadAllFavMovies();
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

    public MovieDetails convertFromFavorite(FavoriteMovieDetails favoriteMovieDetails) {
        MovieDetails movieDetails = new MovieDetails();
        
        // Convert the date string to something more human friendly.
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String s = "";
        s = dateFormat.format(favoriteMovieDetails.getReleaseDate());

        movieDetails.setId(favoriteMovieDetails.getId());
        movieDetails.setAdult(favoriteMovieDetails.isAdult());
        movieDetails.setReleaseDate(s);
        movieDetails.setBackdropPath(favoriteMovieDetails.getBackdropPath());
        movieDetails.setBudget(favoriteMovieDetails.getBudget());
        movieDetails.setHomepage(favoriteMovieDetails.getHomepage());
        movieDetails.setImdbId(favoriteMovieDetails.getImdbId());
        movieDetails.setOriginalLanguage(favoriteMovieDetails.getOriginalLanguage());
        movieDetails.setOriginalTitle(favoriteMovieDetails.getOriginalTitle());
        movieDetails.setOverview(favoriteMovieDetails.getOverview());
        movieDetails.setPopularity(favoriteMovieDetails.getPopularity());
        movieDetails.setPosterPath(favoriteMovieDetails.getPosterPath());
        movieDetails.setRevenue(favoriteMovieDetails.getRevenue());
        movieDetails.setRuntime(favoriteMovieDetails.getRuntime());
        movieDetails.setStatus(favoriteMovieDetails.getStatus());
        movieDetails.setTagline(favoriteMovieDetails.getTagline());
        movieDetails.setTitle(favoriteMovieDetails.getTitle());
        movieDetails.setVoteAverage(favoriteMovieDetails.getVoteAverage());
        movieDetails.setVoteCount(favoriteMovieDetails.getVoteCount());
        
        return movieDetails;
    }

    public MovieListDetailResponse covertMovieListItemFromFavorite(FavoriteMovieDetails favoriteMovieDetails) {
        MovieListDetailResponse movieListDetailResponse = new MovieListDetailResponse();

        // Convert the date string to something more human friendly.
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String s = "";
        s = dateFormat.format(favoriteMovieDetails.getReleaseDate());

        movieListDetailResponse.setId(favoriteMovieDetails.getId());
        movieListDetailResponse.setBackdrop_path(favoriteMovieDetails.getBackdropPath());
        movieListDetailResponse.setPoster_path(favoriteMovieDetails.getPosterPath());
        movieListDetailResponse.setTitle(favoriteMovieDetails.getTitle());

        return movieListDetailResponse;
    }

    public void addFavorite(FavoriteMovieDetails favoriteMovieDetails) {
        appExecutors.getDiskIO().execute(() -> movieDAO.insertFavMovie(favoriteMovieDetails));
    }

    public void deleteFavorite(FavoriteMovieDetails favoriteMovieDetails) {
        appExecutors.getDiskIO().execute(() -> movieDAO.deleteFavMovie(favoriteMovieDetails));
    }

    public FavoriteMovieDetails getFavorite(int id) {
        return movieDAO.loadFavMovieById(id);
//        return favoriteMovieDetails;
    }
}
