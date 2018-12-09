package com.rasjdd.ras.popularmoviesstage2.DatabaseFunctions;

import android.app.Application;

import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.MovieListDetailResponse;
import com.rasjdd.ras.popularmoviesstage2.Models.MovieDetails;
import com.rasjdd.ras.popularmoviesstage2.Models.MovieList;
import com.rasjdd.ras.popularmoviesstage2.Utilities.AppExecutors;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FavoriteUtilities {
    private final ThreadLocal<MovieDAO> movieDAO = new ThreadLocal<MovieDAO>();
    private final AppExecutors appExecutors;

    public FavoriteUtilities(Application application) {
        movieDAO.set(FavoriteMoviesDatabase.getInstance(application).movieDAO());
        appExecutors = AppExecutors.getExecInstance();
    }

    public boolean movieIsFavorited(int id) {
        FavoriteMovieDetails favoriteMovie = movieDAO.get().loadFavMovieById(id);
        return favoriteMovie != null;
    }

    public List<FavoriteMovieDetails> getMovieList() {
        return movieDAO.get().loadAllFavMovies();
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

    public MovieList convertFavListToMovieList(List<FavoriteMovieDetails> favoriteList) {
        ArrayList<MovieListDetailResponse> favMovArLst = null;
        MovieList mFavs = new MovieList();

        if (null != favoriteList) {
            for (FavoriteMovieDetails favMov : favoriteList) {

                // Convert the date string to something more program friendly.
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String s = "";
                s = dateFormat.format(favMov.getReleaseDate());

                MovieListDetailResponse movie = null;

                movie.setId(favMov.getId());
                movie.setAdult(favMov.isAdult());
                movie.setRelease_date(s);
                movie.setBackdrop_path(favMov.getBackdropPath());
                movie.setOriginal_language(favMov.getOriginalLanguage());
                movie.setOriginal_title(favMov.getOriginalTitle());
                movie.setOverview(favMov.getOverview());
                movie.setPopularity(favMov.getPopularity());
                movie.setPoster_path(favMov.getPosterPath());
                movie.setTitle(favMov.getTitle());
                movie.setVote_average(favMov.getVoteAverage());
                movie.setVote_count(favMov.getVoteCount());

                favMovArLst.add(movie);
            }
            int mMovies = favoriteList.size();
            mFavs.setTotal_pages(1);
            mFavs.setTotal_pages(1);
            mFavs.setTotal_results(mMovies);
            mFavs.setResults(favMovArLst);
        }

        return mFavs;
    }

    public void addFavorite(FavoriteMovieDetails favoriteMovieDetails) {
        appExecutors.getDiskIO().execute(() -> movieDAO.get().insertFavMovie(favoriteMovieDetails));
    }

    public void deleteFavorite(FavoriteMovieDetails favoriteMovieDetails) {
        appExecutors.getDiskIO().execute(() -> movieDAO.get().deleteFavMovie(favoriteMovieDetails));
    }
}
