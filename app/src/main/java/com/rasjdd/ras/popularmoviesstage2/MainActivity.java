package com.rasjdd.ras.popularmoviesstage2;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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
import com.rasjdd.ras.popularmoviesstage2.Adapters.MainViewAdapter;
import com.rasjdd.ras.popularmoviesstage2.DatabaseFunctions.FavoriteMovieDetails;
import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.MovieListDetailResponse;
import com.rasjdd.ras.popularmoviesstage2.Models.MovieList;
import com.rasjdd.ras.popularmoviesstage2.Models.Views.MainViewModel;
import com.rasjdd.ras.popularmoviesstage2.Utilities.Constants;
import com.rasjdd.ras.popularmoviesstage2.Utilities.NetUtils;
import com.rasjdd.ras.popularmoviesstage2.Utilities.PreferenceUtilities;
import com.rasjdd.ras.popularmoviesstage2.databinding.ActivityMainBinding;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        MainViewAdapter.MainAdapterOnClickHandler {

    private int mPageNumber;
    private int mPagesAvail;
    private int mNextPage;
    private long loadtimer;
    private String mSortType;
    private String mSortOrder;

    private MainViewAdapter mMainViewAdapter;

    private RequestQueue requestQueue;
    private Gson gMovieList;

    ActivityMainBinding mainBinding;

    private List<FavoriteMovieDetails> mFavs;
    private MainViewModel mViewModel;

    private boolean keepDataOnResume;

    PreferenceUtilities preferenceUtilities = new PreferenceUtilities();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferenceUtilities.setContext(this);
        loadtimer = System.currentTimeMillis() + 2000;

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);



        if (mPageNumber == 0) mPageNumber = 1;
        if (mSortType == null) mSortType = preferenceUtilities.readSharedPreferenceString
                (Constants.prefSortTypeKey,Constants.TMDBAPIQueryKeyGetPopular);
        if (mSortOrder == null) mSortOrder = preferenceUtilities.readSharedPreferenceString
                (Constants.prefSortOrderKey,Constants.sortDescending);

        if (requestQueue == null) requestQueue = Volley.newRequestQueue(getApplicationContext());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gMovieList = gsonBuilder.create();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        GridLayoutManager posterLayout = new GridLayoutManager(this,(metrics.widthPixels / 350));

        mainBinding.recyclerPosterGrid.setLayoutManager(posterLayout);
        mainBinding.recyclerPosterGrid.getViewTreeObserver().addOnScrollChangedListener(new MovieScrollListener());

        mMainViewAdapter = new MainViewAdapter(this);

        mainBinding.recyclerPosterGrid.setAdapter(mMainViewAdapter);

        if (null == mSortType){
            if (NetUtils.testConnectivityBasic(this)) mSortType = Constants.TMDBAPIQueryKeyGetPopular;
            else mSortType = Constants.sortByFavorites;
        }
        if (!NetUtils.testConnectivityBasic(this)) {
            Toast.makeText(this,
                    getString(R.string.no_connectivity) + "`n" + getString(R.string.show_cachce),
                    Toast.LENGTH_LONG).show();
        }

        if (mSortType.equals(Constants.sortByFavorites)) getFavMovieList();
        else {
            URL mURL = NetUtils.buildAPIGetURL(Constants.TMDBMovieType, mPageNumber, mSortType, mSortOrder);
            getLiveMovieList(mURL.toString());
        }
        updateTitle();

    }

    private void getFavMovieList() {

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.getFavoriteDetailsList();

        mViewModel.getFavoriteDetailsList().observe(this, favList -> {
            mFavs = favList;
            if (mSortType.equals(Constants.sortByFavorites) && !keepDataOnResume) mMainViewAdapter.setMovieList(mViewModel.getMovieList(mFavs));
        });

        updateTitle();
        keepDataOnResume = false;
    }

    private void getLiveMovieList(String s) {
        if (NetUtils.testConnectivityBasic(this)) {
            StringRequest listRequest = new StringRequest(Request.Method.GET, s,
                    new webResponseListener(), new webErrorListener());
            requestQueue.add(listRequest);
//            mainBinding.staticLoadingScreen.setVisibility(View.VISIBLE);
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
        // TODO Change Query Keys back to use the Discover entry point since it provides much more capability and efficiency.

        switch (item.getItemId()) {
            case R.id.menu_action_refresh:
                mMainViewAdapter.setMovieList(null);
                mainBinding.staticErrorDisplay.setVisibility(View.GONE);
                mainBinding.staticLoadingScreen.setVisibility(View.VISIBLE);
                if (mSortType.equals(Constants.sortByFavorites)) getFavMovieList();
                else getLiveMovieList(NetUtils.buildAPIGetURL(Constants.TMDBMovieType, mPageNumber, mSortType, mSortOrder).toString());
                break;
            case R.id.menu_sort_favorites:
                mMainViewAdapter.setMovieList(null);
                mSortType = Constants.sortByFavorites;
                updateTitle();
                mainBinding.staticErrorDisplay.setVisibility(View.GONE);
                mainBinding.staticLoadingScreen.setVisibility(View.VISIBLE);
                getFavMovieList();
                break;
            case R.id.menu_sort_popularity:
                getURL = NetUtils.buildAPIGetURL(Constants.TMDBMovieType, 1,
                        Constants.TMDBAPIQueryKeyGetPopular, null);
                mSortType = Constants.TMDBAPIQueryKeyGetPopular;
                updateTitle();
                mMainViewAdapter.setMovieList(null);
                mainBinding.staticErrorDisplay.setVisibility(View.GONE);
                mainBinding.staticLoadingScreen.setVisibility(View.VISIBLE);
                getLiveMovieList(getURL.toString());
                break;
            case R.id.menu_sort_rating:
                getURL = NetUtils.buildAPIGetURL(Constants.TMDBMovieType, 1,
                        Constants.TMDBAPIQueryKeyGetRating, null);
                mSortType = Constants.TMDBAPIQueryKeyGetRating;
                updateTitle();
                mMainViewAdapter.setMovieList(null);
                mainBinding.staticErrorDisplay.setVisibility(View.GONE);
                mainBinding.staticLoadingScreen.setVisibility(View.VISIBLE);
                getLiveMovieList(getURL.toString());
                break;
            case R.id.menu_sort_name:
                getURL = NetUtils.buildAPIGetURL(Constants.TMDBMovieType, 1,
                        Constants.TMDBAPIQueryKeyGetNowPlaying, null);
                mSortType = Constants.TMDBAPIQueryKeyGetNowPlaying;
                mMainViewAdapter.setMovieList(null);
                mainBinding.staticErrorDisplay.setVisibility(View.GONE);
                mainBinding.staticLoadingScreen.setVisibility(View.VISIBLE);
                getLiveMovieList(getURL.toString());
                break;

        }

        preferenceUtilities.setSharedPreferenceString(Constants.prefSortTypeKey, mSortType);

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
            mPagesAvail = moviesList.getTotal_pages();
            if (!keepDataOnResume) {
                if (moviesList.getPage() > 1) {
                    mMainViewAdapter.appendMovieList(moviesList.getResults());
                    mNextPage = moviesList.getPage() + 1;
                }
                else if (moviesList.getPage() == 1) {
                    mMainViewAdapter.setMovieList(moviesList.getResults());
                    mNextPage = moviesList.getPage() + 1;
                }
            }
            showMovieGrid();
            updateTitle();
            keepDataOnResume = false;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.sortingKey, mSortType);
        if (null != mainBinding.recyclerPosterGrid.getLayoutManager()) {
            outState.putParcelable(Constants.layoutKey,
                    mainBinding.recyclerPosterGrid.getLayoutManager().onSaveInstanceState());
        }
        if (null != mMainViewAdapter.getMovieList()){
            outState.putSerializable(Constants.movieListObjectKey, mMainViewAdapter.getMovieList());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSortType = savedInstanceState.getString(Constants.sortingKey);
        keepDataOnResume = true;

        if (mainBinding.recyclerPosterGrid.getLayoutManager() != null) {
            mainBinding.recyclerPosterGrid.getLayoutManager()
                    .onRestoreInstanceState(savedInstanceState.getParcelable(Constants.layoutKey));
        }
        if (null != savedInstanceState.getSerializable(Constants.movieListObjectKey)){
            mMainViewAdapter.setMovieList((ArrayList<MovieListDetailResponse>)
                    savedInstanceState.getSerializable(Constants.movieListObjectKey));
        }
        updateTitle();
    }

    private class MovieScrollListener implements ViewTreeObserver.OnScrollChangedListener {
        @Override
        public void onScrollChanged() {
            if (!mSortType.equals(Constants.sortByFavorites)) {
                int lastVisible = 0;
                int viewThresh = 0;
                int total = 0;
                boolean b = false;

                RecyclerView.LayoutManager mLayoutManager = mainBinding.recyclerPosterGrid.getLayoutManager();
                viewThresh = ((GridLayoutManager) mLayoutManager).getSpanCount() * 2;
                lastVisible = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                total = mMainViewAdapter.getItemCount();

                b = (lastVisible > 1) && (viewThresh > 2);
                b = b && ((lastVisible + viewThresh) >= total);
                b = b && (mNextPage <= mPagesAvail);
                b = b && (loadtimer <= System.currentTimeMillis());

                if (b) {
                    loadtimer = System.currentTimeMillis() + 2000;
                    getLiveMovieList(NetUtils.buildAPIGetURL(Constants.TMDBMovieType, mNextPage, mSortType, mSortOrder).toString());
                }
            }
        }
    }
}
