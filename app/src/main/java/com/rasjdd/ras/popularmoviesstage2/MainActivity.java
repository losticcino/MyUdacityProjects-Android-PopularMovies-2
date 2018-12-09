package com.rasjdd.ras.popularmoviesstage2;

import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rasjdd.ras.popularmoviesstage2.Adapters.MainViewAdapter;
import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.MovieListDetailResponse;
import com.rasjdd.ras.popularmoviesstage2.Models.MovieList;
import com.rasjdd.ras.popularmoviesstage2.Models.Views.MainViewModel;
import com.rasjdd.ras.popularmoviesstage2.Utilities.Constants;
import com.rasjdd.ras.popularmoviesstage2.Utilities.NetUtils;
import com.rasjdd.ras.popularmoviesstage2.databinding.ActivityMainBinding;

import java.net.URL;


// TODO Pagination
// TODO display favorite list

public class MainActivity extends AppCompatActivity implements MainViewAdapter.MainAdapterOnClickHandler {

    private int mPageNumber;
    private String mSortType;
    private String mSortOrder;
    private String mTitle;

    private MainViewAdapter mMainViewAdapter;

    private RequestQueue requestQueue;
    private Gson gMovieList;

    ActivityMainBinding mainBinding;

    private MovieList mFavList;
    private ViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (mPageNumber == 0) mPageNumber = 1;
        if (mSortType == null) mSortType = Constants.TMDBAPIQueryKeyGetPopular;
        if (mSortOrder == null) mSortOrder = Constants.sortDescending;

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        mViewModel = new MainViewModel(getApplication());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gMovieList = gsonBuilder.create();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        GridLayoutManager posterLayout = new GridLayoutManager(this,(metrics.widthPixels / 350));

        mainBinding.recyclerPosterGrid.setLayoutManager(posterLayout);

        mMainViewAdapter = new MainViewAdapter(this);

        mainBinding.recyclerPosterGrid.setAdapter(mMainViewAdapter);

        URL mURL = NetUtils.buildAPIGetURL(Constants.TMDBMovieType, mPageNumber, mSortType, mSortOrder);

        if (NetUtils.testConnectivityBasic(this) && !mSortType.equals(Constants.sortByFavorites)) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            getMovieList(mURL.toString());
            updateTitle();
        }
        else if (!NetUtils.testConnectivityBasic(this) || mSortType == Constants.sortByFavorites){
            mViewModel = new MainViewModel(this.getApplication());
            mFavList = ((MainViewModel) mViewModel).getFavoriteList();
            mMainViewAdapter.setMovieList(mFavList.getResults());
            updateTitle();
        }
        

    }

    private void getMovieList(String s) {
        if (NetUtils.testConnectivityBasic(this)) {
            StringRequest listRequest = new StringRequest(Request.Method.GET, s, new webResponseListener(), new webErrorListener());
            requestQueue.add(listRequest);
            mainBinding.staticLoadingScreen.setVisibility(View.VISIBLE);
        }
        else {
            Toast.makeText(this,
                    getString(R.string.no_connectivity),
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mainMenuInflater = getMenuInflater();
        mainMenuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        URL getURL;
        // TODO Change Query Keys back to use the Discover entry point since it provides much more capability.

        switch (item.getItemId()) {
            case R.id.menu_action_refresh:
                getURL = NetUtils.buildAPIGetURL(Constants.TMDBMovieType, mPageNumber, mSortType, mSortOrder);
                mMainViewAdapter.setMovieList(null);
                mainBinding.staticErrorDisplay.setVisibility(View.GONE);
                mainBinding.staticLoadingScreen.setVisibility(View.VISIBLE);
                if (mSortType.equals(Constants.sortByFavorites)) mFavList = ((MainViewModel) mViewModel).getFavoriteList();
                else getMovieList(getURL.toString());
                break;
            case R.id.menu_sort_favorites:
                mMainViewAdapter.setMovieList(null);
                mSortType = Constants.sortByFavorites;
                updateTitle();
                mainBinding.staticErrorDisplay.setVisibility(View.GONE);
                mainBinding.staticLoadingScreen.setVisibility(View.VISIBLE);
                mFavList = ((MainViewModel) mViewModel).getFavoriteList();
                mMainViewAdapter.setMovieList(mFavList.getResults());
                break;
            case R.id.menu_sort_popularity:
                getURL = NetUtils.buildAPIGetURL(Constants.TMDBMovieType, 1, Constants.TMDBAPIQueryKeyGetPopular, null);
                mSortType = Constants.TMDBAPIQueryKeyGetPopular;
                updateTitle();
                mMainViewAdapter.setMovieList(null);
                mainBinding.staticErrorDisplay.setVisibility(View.GONE);
                mainBinding.staticLoadingScreen.setVisibility(View.VISIBLE);
                getMovieList(getURL.toString());
                break;
            case R.id.menu_sort_rating:
                getURL = NetUtils.buildAPIGetURL(Constants.TMDBMovieType, 1, Constants.TMDBAPIQueryKeyGetRating, null);
                mSortType = Constants.TMDBAPIQueryKeyGetRating;
                updateTitle();
                mMainViewAdapter.setMovieList(null);
                mainBinding.staticErrorDisplay.setVisibility(View.GONE);
                mainBinding.staticLoadingScreen.setVisibility(View.VISIBLE);
                getMovieList(getURL.toString());
                break;
            case R.id.menu_sort_name:
                getURL = NetUtils.buildAPIGetURL(Constants.TMDBMovieType, 1, Constants.TMDBAPIQueryKeyGetNowPlaying, null);
                mSortType = Constants.TMDBAPIQueryKeyGetNowPlaying;
                updateTitle();
                mMainViewAdapter.setMovieList(null);
                mainBinding.staticErrorDisplay.setVisibility(View.GONE);
                mainBinding.staticLoadingScreen.setVisibility(View.VISIBLE);
                getMovieList(getURL.toString());
                break;

        }

        return true;
    }

    @Override
    public void onMovieDetailsClick(MovieListDetailResponse result) {
        Intent detailIntent = new Intent(this, ShowMovieDetails.class);
        detailIntent.putExtra(Constants.movieIdIntent, result.getId());
        startActivity(detailIntent);
    }

    private class webResponseListener implements Response.Listener<String> {

        @NonNull
        @Override
        public void onResponse(String response) {
            MovieList moviesList = gMovieList.fromJson(response,MovieList.class);
            mMainViewAdapter.setMovieList(moviesList.getResults());
            showMovieGrid();
        }
    }

    private class webErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(MainActivity.this,
                    getString(R.string.cant_fetch_data),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void showMovieGrid() {
        mainBinding.staticLoadingScreen.setVisibility(View.GONE);
        mainBinding.staticErrorDisplay.setVisibility(View.GONE);
        mainBinding.recyclerPosterGrid.setVisibility(View.VISIBLE);
    }

    private void updateTitle(){
        String mTitleAppend = Constants.textJoint;
        switch (mSortType) {
            case Constants.TMDBAPIQueryKeyGetPopular:
                mTitleAppend += getString(R.string.sortBy_popularity);
                break;
            case Constants.sortByFavorites:
                mTitleAppend += getString(R.string.sortBy_favorites);
                break;
            case Constants.TMDBAPIQueryKeyGetRating:
                mTitleAppend += getString(R.string.sortBy_rating);
                break;
            case Constants.TMDBAPIQueryKeyGetNowPlaying:
                mTitleAppend += getString(R.string.sortBy_name);
                break;
            default:
                mTitleAppend = "";
        }
        setTitle(getString(R.string.app_name) + mTitleAppend);
    }

}
