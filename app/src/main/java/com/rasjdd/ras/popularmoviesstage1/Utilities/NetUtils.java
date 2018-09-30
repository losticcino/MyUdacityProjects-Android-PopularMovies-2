package com.rasjdd.ras.popularmoviesstage1.Utilities;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rasjdd.ras.popularmoviesstage1.MainActivity;

import java.net.MalformedURLException;
import java.net.URL;

public class NetUtils {

    /*

    To avoid accidentally uploading my API key, I created a class which is ignored by git-commit.
    The class name is "APIKeys"
    The class reads as follows:

    public class APIKeys {

        public static final String TheMovieDbAPIKey = "(API KEY)";

    }

     */

    public static URL buildAPIURL(String mediaType, Integer pageNum, String sortType, String sortOrder){
        //Set Defaults in case they're forgotten
        if (mediaType == null) {mediaType = Constants.TMDBMovieType;}
        if (sortType == null) {sortType = Constants.sortByPopularity;}
        if (sortOrder == null) {sortOrder = Constants.sortDescending;}
        if (pageNum == null || pageNum <= 1) {pageNum = 1;}

        Uri.Builder formedURI = new Uri.Builder();
        formedURI.scheme("https")
                .authority(Constants.TMDBAPIServer)
                .appendPath(Constants.TMDBAPIVer)
                .appendPath(Constants.TMDBAPImode)
                .appendPath(mediaType)
                .appendQueryParameter("sort_by",sortType+sortOrder)
                .appendQueryParameter("page",pageNum.toString())
                .appendQueryParameter("api_key", APIKeys.TheMovieDbAPIKey)
                .build();

        URL url = null;
        try {
            url = new URL(formedURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

}
