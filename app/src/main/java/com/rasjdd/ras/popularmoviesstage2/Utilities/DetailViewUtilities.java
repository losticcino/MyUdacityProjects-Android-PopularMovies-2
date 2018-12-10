package com.rasjdd.ras.popularmoviesstage2.Utilities;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.rasjdd.ras.popularmoviesstage2.DatabaseFunctions.FavoriteMovieDetails;
import com.rasjdd.ras.popularmoviesstage2.Models.MovieDetails;
import com.rasjdd.ras.popularmoviesstage2.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DetailViewUtilities {
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void SetSynopsisText(TextView textView, String s) {
        StyleSpan boldTypeface = new StyleSpan(Typeface.BOLD);

        SpannableStringBuilder sbSynopsis = new SpannableStringBuilder(
                context.getString(R.string.details_heading_synopsis) + ": ");
        sbSynopsis.setSpan(boldTypeface, 0,
                context.getString(R.string.details_heading_synopsis).length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sbSynopsis.append("\n\t\t").append(s);
        textView.setText(sbSynopsis);
    }

    public void SetRating(TextView textView, float f) {
        int ratingColor = (Integer) new ArgbEvaluator().evaluate((f / 10 + 0.1f),
                context.getColor(R.color.colorBad), context.getColor(R.color.colorGood));
        textView.setTextColor(ratingColor);
        textView.setText(String.valueOf(f));
    }

    public void SetMoreInfo(TextView textView, String releaseDate, String originalLanguage, Boolean isAdult){
        String s = context.getString(R.string.details_heading_release) + ": ";
        s += releaseDate + Constants.textJoint;
        s += originalLanguage;
        if (isAdult) s += Constants.textJoint + context.getString(R.string.details_movie_is_adult);
        textView.setText(s);
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
}
