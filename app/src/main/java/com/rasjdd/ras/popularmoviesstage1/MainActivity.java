package com.rasjdd.ras.popularmoviesstage1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rasjdd.ras.popularmoviesstage1.Adapters.MainViewAdapter;
import com.rasjdd.ras.popularmoviesstage1.Models.MovieDetails;
import com.rasjdd.ras.popularmoviesstage1.Models.MovieList;
import com.rasjdd.ras.popularmoviesstage1.Utilities.Constants;
import com.rasjdd.ras.popularmoviesstage1.Utilities.NetUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MainViewAdapter.MainAdapterOnClickHandler {

    private int mPageNumber;
    private String mSortType;
    private String mSortOrder;

    private RecyclerView mRecyclerView;
    private MainViewAdapter mMainViewAdapter;
    private TextView mErrorMessage;
    private ProgressBar mLoadingBar;

    private RequestQueue requestQueue;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mPageNumber == 0) {
            mPageNumber = 1;
        }
        if (mSortType == null) {
            mSortType = Constants.TMDBAPIQueryKeyGetPopular;
        }
        if (mSortOrder == null) {
            mSortOrder = Constants.sortDescending;
        }

        mErrorMessage = findViewById(R.id.tv_errorDisplay);

        mLoadingBar = findViewById(R.id.pb_mainLoadScreen);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        mRecyclerView = findViewById(R.id.recycler_posterGrid);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gson = gsonBuilder.create();

        //LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);

        mRecyclerView.setLayoutManager(layoutManager);
        //mRecyclerView.hasFixedSize();

        mMainViewAdapter = new MainViewAdapter(this);

        mRecyclerView.setAdapter(mMainViewAdapter);

        URL mURL = NetUtils.buildAPIGetURL(Constants.TMDBMovieType, mPageNumber, mSortType, mSortOrder);
        getMovieList(mURL.toString());
    }

    private void getMovieList(String s) {
        StringRequest listRequest = new StringRequest(Request.Method.GET, s, new webResponseListener(), new webErrorListener());
        requestQueue.add(listRequest);
        mLoadingBar.setVisibility(View.VISIBLE);
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
                mErrorMessage.setVisibility(View.GONE);
                mLoadingBar.setVisibility(View.VISIBLE);
                getMovieList(getURL.toString());
                break;
            case R.id.menu_sort_popularity:
                getURL = NetUtils.buildAPIGetURL(Constants.TMDBMovieType, 1, Constants.TMDBAPIQueryKeyGetPopular, null);
                mSortType = Constants.TMDBAPIQueryKeyGetPopular;
                mMainViewAdapter.setMovieList(null);
                mErrorMessage.setVisibility(View.GONE);
                mLoadingBar.setVisibility(View.VISIBLE);
                getMovieList(getURL.toString());
                break;
            case R.id.menu_sort_rating:
                getURL = NetUtils.buildAPIGetURL(Constants.TMDBMovieType, 1, Constants.TMDBAPIQueryKeyGetRating, null);
                mSortType = Constants.TMDBAPIQueryKeyGetRating;
                mMainViewAdapter.setMovieList(null);
                mErrorMessage.setVisibility(View.GONE);
                mLoadingBar.setVisibility(View.VISIBLE);
                getMovieList(getURL.toString());
                break;
            case R.id.menu_sort_name:
                getURL = NetUtils.buildAPIGetURL(Constants.TMDBMovieType, 1, Constants.TMDBAPIQueryKeyGetNowPlaying, null);
                mSortType = Constants.TMDBAPIQueryKeyGetNowPlaying;
                mMainViewAdapter.setMovieList(null);
                mErrorMessage.setVisibility(View.GONE);
                mLoadingBar.setVisibility(View.VISIBLE);
                getMovieList(getURL.toString());
                break;

        }

        return true;
    }

    @Override
    public void onMovieDetailsClick(MovieDetails result) {
        Intent detailIntent = new Intent(this, ShowMovieDetails.class);
        detailIntent.putExtra(MovieDetails.MyParcelName, result);
        startActivity(detailIntent);
    }

    private class webResponseListener implements Response.Listener<String> {

        @NonNull
        @Override
        public void onResponse(String response) {
            MovieList moviesList = gson.fromJson(response,MovieList.class);
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
        mLoadingBar.setVisibility(View.GONE);

        mErrorMessage.setVisibility(View.GONE);

        mRecyclerView.setVisibility(View.VISIBLE);
    }

}
