package com.rasjdd.ras.popularmoviesstage1.Utilities;

import android.net.Uri;

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

    public static URL buildAPIDiscoverURL(String mediaType, Integer pageNum, String sortType, String sortOrder){
        //Set Defaults in case they're forgotten
        if (mediaType == null) mediaType = Constants.TMDBMovieType;
        if (sortType == null) sortType = Constants.sortByPopularity;
        if (sortOrder == null) sortOrder = Constants.sortDescending;
        if (pageNum == null || pageNum <= 1) pageNum = 1;

        Uri.Builder formedURI = new Uri.Builder();
        formedURI.scheme("https")
                .authority(Constants.TMDBAPIServer)
                .appendPath(Constants.TMDBAPIVer)
                .appendPath(Constants.TMDBAPImode)
                .appendPath(mediaType)
                .appendQueryParameter(Constants.TMDBAPIQueryKeySortBy,sortType+sortOrder)
                .appendQueryParameter(Constants.TMDBAPIQueryKeyPage,pageNum.toString())
                .appendQueryParameter(Constants.TMDBAPIQueryKeyApi, APIKeys.TheMovieDbAPIKey)
                .build();

        URL url = null;
        try {
            url = new URL(formedURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildImageURL(String imgWidth, String imagePath) {
        //Set Defaults in case they're forgotten
        URL url = null;

        //Only try building a custom URL when there is a real image path pushed in.
        if (imagePath != null) {
            if (imgWidth == null) imgWidth = Constants.TMDBPosterWidthMed;
            imagePath = imagePath.replace("\\", "");
            imagePath = imagePath.replace("/", "");

            Uri.Builder formedURI = new Uri.Builder();
            formedURI.scheme("https")
                    .authority(Constants.TMDBImageServer)
                    .appendPath(Constants.TMDBImageServerPathAppend1)
                    .appendPath(Constants.TMDBImageServerPathAppend2)
                    .appendPath(imgWidth)
                    .appendPath(imagePath)
                    .build();
            try {
                url = new URL(formedURI.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        } else { //If there is an invalid string passed in, return TMDB logo URL
            Uri mURI;
            mURI = Uri.parse(Constants.TMDBLogoUrl);
            try {
                url = new URL(mURI.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return url;
    }

    /*

    I will implement this later when I have time to fix it...

    public static void picassoGet(String imageUrl, View view){
        if (imageUrl == null) imageUrl = Constants.TMDBLogoUrl;

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .into((Target) view);
    }*/

}
