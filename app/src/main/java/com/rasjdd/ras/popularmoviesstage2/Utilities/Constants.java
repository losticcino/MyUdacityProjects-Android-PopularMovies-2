package com.rasjdd.ras.popularmoviesstage2.Utilities;

import java.security.PublicKey;

public class Constants {
    public static final String TMDBAPIServer = "api.themoviedb.org";
    public static final String TMDBImageServer = "image.tmdb.org";
    // sample TMDB image url https://image.tmdb.org/t/p/w185/3IGbjc5ZC5yxim5W0sFING2kdcz.jpg
    public static final String TMDBLogoUrl = "https://www.themoviedb.org/assets/1/v4/logos/" +
            "293x302-powered-by-square-green-3ee4814bb59d8260d51efdd7c124383540fc04ca27d23eaea3a8c87bfa0f388d.png";

    public static final String TMDBAPIVer = "3";
    public static final String TMDBAPImode = "discover";
    public static final String TMDBAPIQueryKeySortBy = "sort_by";
    public static final String TMDBAPIQueryKeyPage = "page";
    public static final String TMDBAPIQueryKeyApi = "api_key";
    public static final String TMDBAPIQueryKeyGetRating = "top_rated";
    public static final String TMDBAPIQueryKeyGetPopular = "popular";
    public static final String TMDBAPIQueryKeyGetNowPlaying = "now_playing";
    public static final String TMDBAPIQueryAppendToResponse = "append_to_response";

    public static final String TMDBPosterWidthSml = "w92";
    public static final String TMDBPosterWidthMed = "w185";
    public static final String TMDBPosterWidthBig = "w500";
    public static final String TMDBBackdropWidthSml = "w300";
    public static final String TMDBBackdropWidthMed = "w780";
    public static final String TMDBBackdropWidthBig = "w1280";
    public static final String TMDBWidthOrg = "original";
    public static final String TMDBImageServerPathAppend1 = "t";
    public static final String TMDBImageServerPathAppend2 = "p";

    public static final String TMDBMovieType = "movie";
    public static final String TMDBShowType = "show";

    public static final String sortByPopularity = "popularity";
    public static final String sortByRating = "vote_average";
    public static final String sortByTitle = "title";

    public static final String sortAscending = ".asc";
    public static final String sortDescending = ".desc";

    public static final String TMDBDetailVideos = "videos";
    public static final String TMDBDetailImages = "images";
    public static final String TMDBDetailReviews = "reviews";

    //Youtube API constants
    public static final String YouTubeWatchServer = "m.youtube.com";
    public static final String YouTubeThumbnailServer = "img.youtube.com";
    public static final String YouTubeThumbnailLink1 = "vi";
    public static final String YouTubeThumbnailResource = "0.jpg";
    public static final String YouTubeRickRoll = "dQw4w9WgXcQ";
    public static final String YouTubeWatchLink1 = "watch";
    public static final String YouTubeWatchQueryKey = "v";
}
