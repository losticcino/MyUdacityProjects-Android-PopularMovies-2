package com.rasjdd.ras.popularmoviesstage2.DatabaseFunctions;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.Genre;
import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.ProductionCompany;
import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.ProductionCountry;
import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.ReviewList;
import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.SpokenLanguage;
import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.VideoList;

import java.util.ArrayList;

@Entity(tableName = "FavMovie")
public class FavoriteMovieDetails {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "adult")
    private boolean adult;

    @ColumnInfo(name = "release_date")
    private String releaseDate;

    private String backdropPath;
    private Object belongsToCollection;
    private int budget;
    private String homepage;
    private String imdbId;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private float popularity;
    private String posterPath;
    private int revenue;
    private int runtime;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private float voteAverage;
    private int voteCount;
}
