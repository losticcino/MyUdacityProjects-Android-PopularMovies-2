package com.rasjdd.ras.popularmoviesstage2;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rasjdd.ras.popularmoviesstage2.Adapters.ReviewSpinnerAdapter;
import com.rasjdd.ras.popularmoviesstage2.Adapters.TrailerSpinnerAdapter;
import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.MovieListDetailResponse;
import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.Review;
import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.Video;
import com.rasjdd.ras.popularmoviesstage2.Models.MovieDetails;
import com.rasjdd.ras.popularmoviesstage2.Utilities.Constants;
import com.rasjdd.ras.popularmoviesstage2.Utilities.NetUtils;
import com.rasjdd.ras.popularmoviesstage2.databinding.DetailLayoutBinding;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class ShowMovieDetails extends AppCompatActivity implements
        TrailerSpinnerAdapter.TrailerOnClickHandler, ReviewSpinnerAdapter.ReviewOnClickHandler {

    private DetailLayoutBinding detailView;

    private RequestQueue mRequestQueue;

    private Gson gMovieDetails;

    private TrailerSpinnerAdapter trailerSpinnerAdapter;
    private ReviewSpinnerAdapter reviewSpinnerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);

        detailView = DataBindingUtil.setContentView(this, R.layout.detail_layout);
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        detailView.DetailParentScrollView.getViewTreeObserver().addOnScrollChangedListener(new ScrollPositionObserver());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gMovieDetails = gsonBuilder.create();

        Intent parentIntent = getIntent();

        trailerSpinnerAdapter = new TrailerSpinnerAdapter(this);
        GridLayoutManager trailerSpinner = new GridLayoutManager(this, 1);
        detailView.recyclerTrailerSpinnerView.setLayoutManager(trailerSpinner);
        detailView.recyclerTrailerSpinnerView.setAdapter(trailerSpinnerAdapter);

        reviewSpinnerAdapter = new ReviewSpinnerAdapter(this);
        GridLayoutManager reviewSpinner = new GridLayoutManager(this,1);
        detailView.recyclerReviewSpinner.setLayoutManager(reviewSpinner);
        detailView.recyclerReviewSpinner.setAdapter(reviewSpinnerAdapter);

        StyleSpan boldTypeface = new StyleSpan(Typeface.BOLD);

        SpannableStringBuilder sbSynopsis = new SpannableStringBuilder(getString(R.string.details_heading_synopsis) + ": ");
        sbSynopsis.setSpan(boldTypeface, 0, getString(R.string.details_heading_synopsis).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        // TODO Implement Favorites


        if (parentIntent != null) {

            // TODO Clean this all up and use the Volley response
            // TODO follow-on: remove parceling and only put the movie ID as an extra.

            URL url;
            String isAdult = "";
            MovieListDetailResponse mSelectedMovie = parentIntent.getParcelableExtra(MovieListDetailResponse.MyParcelName);

            URL movieDetailUrl = NetUtils.buildMediaDetailUrl(Constants.TMDBMovieType, Integer.toString(mSelectedMovie.getId()));
            getMovieDetails(movieDetailUrl.toString());

            if (mSelectedMovie.isAdult()) isAdult += " | " + getString(R.string.details_movie_is_adult);

            detailView.contentTitle.setText(mSelectedMovie.getTitle());

            //Set Voter Rating
            detailView.contentRating.setText(String.valueOf(mSelectedMovie.getVote_average()));
            int ratingColor = (Integer) new ArgbEvaluator().evaluate((mSelectedMovie.getVote_average() / 10 + 0.1f),getResources().getColor(R.color.colorBad), getResources().getColor(R.color.colorGood));
            detailView.contentRating.setTextColor(ratingColor);
//            detailView.hBreak1.setVisibility(View.GONE);


            //Set Popularity
//            detailView.contentPopularity.setText("pop: (" + String.valueOf(mSelectedMovie.getPopularity()) + ")");

            //Set Extra info ~~ date | lang | ?adult
            detailView.contentMoreInfo.setText(new StringBuilder(getString(R.string.details_heading_release)
                    + ": " + mSelectedMovie.getRelease_date()
                    + " | " + mSelectedMovie.getOriginal_language() + isAdult));

            //populate trailer info


            //Set Synopsis info
            sbSynopsis.append("\n\t\t" + mSelectedMovie.getOverview());
            detailView.contentSynopsis.setText(sbSynopsis);

            //Build Backdrop URL
            url = NetUtils.buildImageURL(Constants.TMDBBackdropWidthBig,mSelectedMovie.getBackdrop_path());

            Picasso.get()
                    .load(url.toString())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(detailView.imageBackdrop);

            //Build Poster URL
            url = NetUtils.buildImageURL(Constants.TMDBPosterWidthBig,mSelectedMovie.getPoster_path());

            Picasso.get()
                    .load(url.toString())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(detailView.imagePoster);


        }
    }

    private void getMovieDetails(String s) {
        StringRequest listRequest = new StringRequest(Request.Method.GET, s, new ShowMovieDetails.webResponseListener(), new ShowMovieDetails.webErrorListener());
        mRequestQueue.add(listRequest);
    }

    @Override
    public void onTrailerDetailsClick(Video result) {
        Uri mYTURl = NetUtils.buildYouTubeWatchURI(result.getKey());
        Intent intent = new Intent(Intent.ACTION_VIEW, mYTURl);
        startActivity(intent);
    }

    @Override
    public void onReviewClick(Review review) {
        Uri mReviewUrl = Uri.parse(review.getUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, mReviewUrl);
        startActivity(intent);
    }


    private class webResponseListener implements Response.Listener<String> {

        @NonNull
        @Override
        public void onResponse(String response) {
            MovieDetails movieDetails = gMovieDetails.fromJson(response,MovieDetails.class);

            // TODO Clean this up with some helper functions

            ArrayList<Video> trailerList = new ArrayList<>();
            for (Video video : movieDetails.getVideoList().getVideos()) {
                if (video.getType().toLowerCase().contains("trailer") || video.getType().toLowerCase().contains("teaser")) trailerList.add(video);
            }
            movieDetails.getVideoList().getVideos();
            if (trailerList.size() >= 1){
                detailView.hBreak3.setVisibility(View.VISIBLE);
                detailView.recyclerTrailerSpinnerView.setVisibility(View.VISIBLE);
                GridLayoutManager trailerManager = (GridLayoutManager) detailView.recyclerTrailerSpinnerView.getLayoutManager();
                trailerManager.setSpanCount(trailerList.size());
                trailerSpinnerAdapter.setTrailerList(trailerList);
            }

            if (null != movieDetails.getReviewList()){
                if (movieDetails.getReviewList().getTotalResults() > 0) {
                    GridLayoutManager reviewManager = (GridLayoutManager) detailView.recyclerReviewSpinner.getLayoutManager();
                    reviewManager.setSpanCount(movieDetails.getReviewList().getTotalResults());
                    ArrayList<Review> reviewList = movieDetails.getReviewList().getResults();
                    detailView.lblNoResults.setVisibility(View.GONE);
                    detailView.recyclerReviewSpinner.setVisibility(View.VISIBLE);
                    reviewSpinnerAdapter.setReviewList(reviewList);
                }
            }
        }
    }

    private class webErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(ShowMovieDetails.this,
                    getString(R.string.cant_fetch_data),
                    Toast.LENGTH_LONG).show();
        }
    }

    private class ScrollPositionObserver implements ViewTreeObserver.OnScrollChangedListener {

        @Override
        public void onScrollChanged() {
            int scrollY = Math.min(Math.max(detailView.DetailParentScrollView.getScrollY(), 0), getResources().getDimensionPixelSize(R.dimen.DetailBackdropHeight));
            detailView.imageBackdrop.setTranslationY(scrollY / 2);
        }
    }

    private void onFavoriteToggle() {

    }
}
