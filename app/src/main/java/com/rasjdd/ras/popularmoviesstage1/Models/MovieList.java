package com.rasjdd.ras.popularmoviesstage1.Models;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieList {
    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int total_results;
    @SerializedName("total_pages")
    private int total_pages;
    @SerializedName("results")
    private ArrayList<ResultList> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public ArrayList<ResultList> getResults() {
        return results;
    }

    public void setResults(ArrayList<ResultList> results) {
        this.results = results;
    }


    public class ResultList {
        @SerializedName("id")
        private int id;
        @SerializedName("title")
        private String title;
        @SerializedName("release_date")
        private String release_date;
        @SerializedName("overview")
        private String overview;
        @SerializedName("popularity")
        private float popularity;
        @SerializedName("vote_average")
        private float vote_average;
        @SerializedName("vote_count")
        private int vote_count;
        @SerializedName("original_title")
        private String original_title;
        @SerializedName("original_language")
        private String original_language;
        @SerializedName("video")
        private boolean video;
        @SerializedName("adult")
        private boolean adult;
        @SerializedName("poster_path")
        private String poster_path;
        @SerializedName("backdrop_path")
        private String backdrop_path;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRelease_date() {
            return release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public float getPopularity() {
            return popularity;
        }

        public void setPopularity(float popularity) {
            this.popularity = popularity;
        }

        public float getVote_average() {
            return vote_average;
        }

        public void setVote_average(float vote_average) {
            this.vote_average = vote_average;
        }

        public int getVote_count() {
            return vote_count;
        }

        public void setVote_count(int vote_count) {
            this.vote_count = vote_count;
        }

        public String getOriginal_title() {
            return original_title;
        }

        public void setOriginal_title(String original_title) {
            this.original_title = original_title;
        }

        public String getOriginal_language() {
            return original_language;
        }

        public void setOriginal_language(String original_language) {
            this.original_language = original_language;
        }

        public boolean isVideo() {
            return video;
        }

        public void setVideo(boolean video) {
            this.video = video;
        }

        public boolean isAdult() {
            return adult;
        }

        public void setAdult(boolean adult) {
            this.adult = adult;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public void setBackdrop_path(String backdrop_path) {
            this.backdrop_path = backdrop_path;
        }
    }


}
