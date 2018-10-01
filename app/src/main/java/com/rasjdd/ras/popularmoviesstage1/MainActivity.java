package com.rasjdd.ras.popularmoviesstage1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.rasjdd.ras.popularmoviesstage1.Models.MovieList;
import com.rasjdd.ras.popularmoviesstage1.Utilities.Constants;
import com.rasjdd.ras.popularmoviesstage1.Utilities.NetUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String sortingParameter;
    private int mPageNumber;
    private String mSortType;
    private String mSortOrder;

    private RecyclerView mRecyclerView;
    private TextView mErrorMessage;
    private ProgressBar mLoadingBar;
    private ImageView mPosterView;

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
            mSortType = Constants.sortByPopularity;
        }
        if (mSortOrder == null) {
            mSortOrder = Constants.sortDescending;
        }

        mErrorMessage = (TextView) findViewById(R.id.tv_errorDisplay);

        mLoadingBar = (ProgressBar) findViewById(R.id.pb_mainLoadScreen);

        mPosterView = (ImageView) findViewById(R.id.iv_gallery_poster);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_posterGrid);

        URL mURL = NetUtils.buildAPIURL(Constants.TMDBMovieType, mPageNumber, mSortType, mSortOrder);
        getMovieList(mURL.toString());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gson = gsonBuilder.create();

    }

    private void getMovieList(String s) {
        StringRequest listRequest = new StringRequest(Request.Method.GET, s, new webResponseListener(), new webErrorListener());
        requestQueue.add(listRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mainMenuInflater = getMenuInflater();
        mainMenuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        URL getURL = null;

        switch (item.getItemId()) {
            case R.id.menu_action_refresh:
                getURL = NetUtils.buildAPIURL(Constants.TMDBMovieType, mPageNumber, mSortType, mSortOrder);
                mErrorMessage.setVisibility(View.GONE);
                mLoadingBar.setVisibility(View.VISIBLE);
                getMovieList(getURL.toString());
                break;
            case R.id.menu_sort_popularity:
                getURL = NetUtils.buildAPIURL(Constants.TMDBMovieType, 871, Constants.sortByPopularity, Constants.sortDescending);
                mErrorMessage.setVisibility(View.GONE);
                mLoadingBar.setVisibility(View.VISIBLE);
                getMovieList(getURL.toString());
                break;
            case R.id.menu_sort_rating:
                getURL = NetUtils.buildAPIURL(Constants.TMDBMovieType, 1, Constants.sortByRating, Constants.sortDescending);
                mErrorMessage.setVisibility(View.GONE);
                mLoadingBar.setVisibility(View.VISIBLE);
                getMovieList(getURL.toString());
                break;
            case R.id.menu_sort_name:
                getURL = NetUtils.buildAPIURL(Constants.TMDBMovieType, 1, Constants.sortByTitle, Constants.sortAscending);
                mErrorMessage.setVisibility(View.GONE);
                mLoadingBar.setVisibility(View.VISIBLE);
                getMovieList(getURL.toString());
                break;

        }

        return true;
    }

    private class webResponseListener implements Response.Listener<String> {

        @Override
        public void onResponse(String response) {
            mLoadingBar.setVisibility(View.GONE);
            Log.e("response","success");
            Log.e("response",response);
            MovieList moviesList = gson.fromJson(response,MovieList.class);
            String msg = "";

            for (MovieList.ResultList resultList : moviesList.getResults()) {
                msg += resultList.getTitle() + "  " + resultList.getPoster_path() + "\n\n";
            }

            mErrorMessage.setText(msg);
            mErrorMessage.setVisibility(View.VISIBLE);
        }
    }

    private class webErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            mLoadingBar.setVisibility(View.GONE);
            mErrorMessage.setText(R.string.error_message);
            mErrorMessage.setVisibility(View.VISIBLE);
        }
    }

    private void showMovieList () {
        mErrorMessage.setVisibility(View.GONE);
    }

    private class MovieListObject extends JsonObjectRequest {

        public MovieListObject(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> mlob = new HashMap<String, String>();
            mlob.put("Accept","application/json");
            return mlob;
        }
    }

}
