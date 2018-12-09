package com.rasjdd.ras.popularmoviesstage2;

import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.Review;
import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.Video;
import com.rasjdd.ras.popularmoviesstage2.Models.MovieDetails;
import com.rasjdd.ras.popularmoviesstage2.Models.Views.DetailViewModel;
import com.rasjdd.ras.popularmoviesstage2.Utilities.Constants;
import com.rasjdd.ras.popularmoviesstage2.Utilities.DetailViewUtilities;
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
    private MovieDetails movieDetails;
    private boolean favd;
    private ViewModel viewModel;
    private DetailViewUtilities detailViewUtilities = new DetailViewUtilities();
    private int mMovieId;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // view setup
        setContentView(R.layout.detail_layout);
        detailView = DataBindingUtil.setContentView(this, R.layout.detail_layout);
        detailViewUtilities.setContext(this);

        // data access setup
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        Intent parentIntent = getIntent();

        if (parentIntent == null){
            Toast.makeText(ShowMovieDetails.this,
                    getString(R.string.intent_failed),
                    Toast.LENGTH_LONG).show();
            NavUtils.navigateUpFromSameTask(this);
        }
        else mMovieId = parentIntent.getIntExtra(Constants.movieIdIntent, Constants.TMDBDefaultID);

        // data view setup.
        detailView.DetailParentScrollView.getViewTreeObserver().addOnScrollChangedListener(new ScrollPositionObserver());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gMovieDetails = gsonBuilder.create();


        trailerSpinnerAdapter = new TrailerSpinnerAdapter(this);
        GridLayoutManager trailerSpinner = new GridLayoutManager(this, 1);
        detailView.recyclerTrailerSpinnerView.setLayoutManager(trailerSpinner);
        detailView.recyclerTrailerSpinnerView.setAdapter(trailerSpinnerAdapter);

        reviewSpinnerAdapter = new ReviewSpinnerAdapter(this);
        GridLayoutManager reviewSpinner = new GridLayoutManager(this,1);
        detailView.recyclerReviewSpinner.setLayoutManager(reviewSpinner);
        detailView.recyclerReviewSpinner.setAdapter(reviewSpinnerAdapter);

        viewModel = new DetailViewModel(this.getApplication());

        URL movieDetailUrl = NetUtils.buildMediaDetailUrl(Constants.TMDBMovieType, Integer.toString(mMovieId));
        getMovieDetails(movieDetailUrl.toString());
        favd = ((DetailViewModel) viewModel).isFavorited(mMovieId);
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
            URL url;
            movieDetails = gMovieDetails.fromJson(response,MovieDetails.class);

            detailView.contentTitle.setText(movieDetails.getTitle());
            detailViewUtilities.SetRating(detailView.contentRating, movieDetails.getVoteAverage());
            detailViewUtilities.SetSynopsisText(detailView.contentSynopsis, movieDetails.getOverview());
            detailViewUtilities.SetMoreInfo(detailView.contentMoreInfo, movieDetails.getReleaseDate(), movieDetails.getOriginalLanguage(), movieDetails.isAdult());

            //Build Backdrop URL
            url = NetUtils.buildImageURL(Constants.TMDBBackdropWidthBig,movieDetails.getBackdropPath());
            Picasso.get()
                    .load(url.toString())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(detailView.imageBackdrop);

            //Build Poster URL
            url = NetUtils.buildImageURL(Constants.TMDBPosterWidthBig,movieDetails.getPosterPath());
            Picasso.get()
                    .load(url.toString())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(detailView.imagePoster);

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
            int scrollPos = detailView.DetailParentScrollView.getScrollY();
            detailView.imageBackdrop.setTranslationY(scrollY / 3);
            detailView.whitebackground.setTop(detailView.contentTitle.getTop());
            if (scrollPos > detailView.contentTitle.getBottom()) {
                setTitle(movieDetails.getTitle());
            }
            if (scrollPos < detailView.contentTitle.getBottom()) {
                setTitle(getString(R.string.details_actionbar_name));
            }
            if (scrollPos > detailView.contentTitle.getTop()){
                detailView.imagePoster.setBottom(detailView.contentRating.getBottom() - (scrollPos - detailView.contentTitle.getTop()));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater detailMenuInflater = getMenuInflater();
        detailMenuInflater.inflate(R.menu.detail_menu, menu);
        MenuItem item = menu.findItem(R.id.btnFavoriteMovie);
        if (favd) item.setIcon(R.drawable.ic_fav_on);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnFavoriteMovie:
                if (favd){
                    ((DetailViewModel) viewModel).delFavorite(movieDetails);
                    item.setIcon(R.drawable.ic_fav_off);
                }
                if (!favd){
                    ((DetailViewModel) viewModel).addFavorite(movieDetails);
                    item.setIcon(R.drawable.ic_fav_on);
                }
                favd = ((DetailViewModel) viewModel).isFavorited(movieDetails.getId());
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return true;
    }
}
