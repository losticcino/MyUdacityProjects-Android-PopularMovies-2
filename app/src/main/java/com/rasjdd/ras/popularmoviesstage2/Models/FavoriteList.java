package com.rasjdd.ras.popularmoviesstage2.Models;

import com.google.gson.annotations.SerializedName;
import com.rasjdd.ras.popularmoviesstage2.DatabaseFunctions.FavoriteMovieDetails;
import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.MovieListDetailResponse;

import java.util.ArrayList;

public class FavoriteList {
    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int total_results;
    @SerializedName("total_pages")
    private int total_pages;
    @SerializedName("results")
    private ArrayList<FavoriteMovieDetails> results;

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

    public ArrayList<FavoriteMovieDetails> getResults() {
        return results;
    }

    public void setResults(ArrayList<FavoriteMovieDetails> results) {
        this.results = results;
    }
}
