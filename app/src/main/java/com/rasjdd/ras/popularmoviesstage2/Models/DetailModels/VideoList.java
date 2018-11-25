package com.rasjdd.ras.popularmoviesstage2.Models.DetailModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VideoList {

    @SerializedName("results")
    @Expose
    private ArrayList<Video> videos;

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> results) {
        this.videos = results;
    }

}