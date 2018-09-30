package com.rasjdd.ras.popularmoviesstage1.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MovieDetails {
    @SerializedName("id")
    private int ID;

    @SerializedName("title")
    private String MovieTitle;

    @SerializedName("release_date")
    private Date ReleaseDate;

    @SerializedName("overview")
    private String Overview;

    @SerializedName("popularity")
    private float Popularity;

    @SerializedName("vote_average")
    private float AverageVote;

    @SerializedName("vote_count")
    private int Votes;

    @SerializedName("original_title")
    private String OriginalTitle;

    @SerializedName("original_language")
    private String OrginalLanguage;

    @SerializedName("video")
    private boolean Video;

    @SerializedName("adult")
    private boolean IsAdult;

    @SerializedName("poster_path")
    private String PosterPath;

    @SerializedName("backdrop_path")
    private String BackdropPath;

    // Getters

    public int getID() {
        return ID;
    }

    public String getMovieTitle() {
        return MovieTitle;
    }

    public Date getReleaseDate() {
        return ReleaseDate;
    }

    public String getOverview() {
        return Overview;
    }

    public float getPopularity() {
        return Popularity;
    }

    public float getAverageVote() {
        return AverageVote;
    }

    public int getVotes() {
        return Votes;
    }

    public String getOriginalTitle() {
        return OriginalTitle;
    }

    public String getOrginalLanguage() {
        return OrginalLanguage;
    }

    public boolean isVideo() {
        return Video;
    }

    public boolean isAdult() {
        return IsAdult;
    }

    public String getPosterPath() {
        return PosterPath;
    }

    public String getBackdropPath() {
        return BackdropPath;
    }

    // End Getters
    // Begin Setters

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setMovieTitle(String title) {
        MovieTitle = title;
    }

    public void setReleaseDate(Date releaseDate) {
        ReleaseDate = releaseDate;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public void setPopularity(float popularity) {
        Popularity = popularity;
    }

    public void setAverageVote(float averageVote) {
        AverageVote = averageVote;
    }

    public void setVotes(int votes) {
        Votes = votes;
    }

    public void setOriginalTitle(String originalTitle) {
        OriginalTitle = originalTitle;
    }

    public void setOrginalLanguage(String orginalLanguage) {
        OrginalLanguage = orginalLanguage;
    }

    public void setVideo(boolean video) {
        Video = video;
    }

    public void setAdult(boolean adult) {
        IsAdult = adult;
    }

    public void setPosterPath(String posterPath) {
        PosterPath = posterPath;
    }

    public void setBackdropPath(String backdropPath) {
        BackdropPath = backdropPath;
    }

    // End Setters


}
