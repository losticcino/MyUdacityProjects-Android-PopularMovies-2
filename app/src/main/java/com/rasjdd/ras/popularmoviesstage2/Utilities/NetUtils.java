package com.rasjdd.ras.popularmoviesstage2.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

import static com.rasjdd.ras.popularmoviesstage2.Utilities.Constants.RandomYoutubeIdentifier;

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

    public static URL buildAPIGetURL(String mediaType, Integer pageNum, String sortType, String sortOrder){
        //Set Defaults in case they're forgotten
        if (mediaType == null) mediaType = Constants.TMDBMovieType;
        if (sortType == null) sortType = Constants.TMDBAPIQueryKeyGetPopular;
        if (sortOrder == null) sortOrder = Constants.sortDescending;
        if (pageNum <= 1) pageNum = 1;

        Uri.Builder formedURI = new Uri.Builder();
        formedURI.scheme("https")
                .authority(Constants.TMDBAPIServer)
                .appendPath(Constants.TMDBAPIVer)
                .appendPath(mediaType)
                .appendPath(sortType)
                .appendQueryParameter(Constants.TMDBAPIQueryKeyPage, String.valueOf(pageNum))
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

    public static URL buildMediaDetailUrl(String mediaType,String sID) {
        // Set defaults in the case of forgotten inputs
        if (mediaType == null) mediaType = Constants.TMDBMovieType;
        // In case the ID is forgotten, return a default movie to prevent bad URLS
        // TODO pick a random movie from a list of more than just one...
        if (sID == null) sID = "9255"; // For now, just return Hot Shots! Part Deux for its cover image.

        String appends = Constants.TMDBDetailVideos + ","
                + Constants.TMDBDetailReviews;

        Uri.Builder formedURI = new Uri.Builder();

        formedURI.scheme("https")
                .authority(Constants.TMDBAPIServer)
                .appendPath(Constants.TMDBAPIVer)
                .appendPath(mediaType)
                .appendPath(sID)
                .appendQueryParameter(Constants.TMDBAPIQueryKeyApi, APIKeys.TheMovieDbAPIKey)
                .appendQueryParameter(Constants.TMDBAPIQueryAppendToResponse, appends)
                .build();

        URL url = null;
        try {
            url = new URL(formedURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildYouTubeThumbnailURL(String videoIdentifier){
        if (null == videoIdentifier) videoIdentifier = RandomYoutubeIdentifier();

        Uri.Builder formedURI = new Uri.Builder();
        formedURI.scheme("https")
                .authority(Constants.YouTubeThumbnailServer)
                .appendPath(Constants.YouTubeThumbnailLink1)
                .appendPath(videoIdentifier)
                .appendPath(Constants.YouTubeThumbnailResource)
                .build();

        URL url = null;
        try {
            url = new URL(formedURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static Uri buildYouTubeWatchURI(String videoIdentifier){
        if (null == videoIdentifier) videoIdentifier = RandomYoutubeIdentifier();

        Uri.Builder formedURI = new Uri.Builder();
        formedURI.scheme("https")
                .authority(Constants.YouTubeWatchServer)
                .appendPath(Constants.YouTubeWatchLink1)
                .appendPath(videoIdentifier)
                .appendPath(Constants.YouTubeThumbnailResource)
                .appendQueryParameter(Constants.YouTubeWatchQueryKey, videoIdentifier);

        return formedURI.build();
    }

    public static boolean testConnectivityBasic(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
