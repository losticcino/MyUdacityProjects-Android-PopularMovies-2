package com.rasjdd.ras.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.rasjdd.ras.popularmoviesstage1.Models.MovieDetails;
import com.rasjdd.ras.popularmoviesstage1.Utilities.Constants;
import com.rasjdd.ras.popularmoviesstage1.Utilities.NetUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class ShowMovieDetails extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.movie_details);
        // TODO Figure out why I can't get rid of the fecking title.

        ImageView mBackdrop = findViewById(R.id.iv_details_backdrop);
        ImageView mPoster = findViewById(R.id.iv_details_poster);
        TextView mTitle = findViewById(R.id.tv_details_title);
        TextView mRating = findViewById(R.id.tv_details_rating);
        TextView mPopularity = findViewById(R.id.tv_details_popularity);
        TextView mMoreInfo = findViewById(R.id.tv_details_moreInfo);
        TextView mSynopsis = findViewById(R.id.tv_details_synopsis);

        Intent parentIntent = getIntent();

        if (parentIntent != null) {
            URL url;
            String isAdult = "";
            MovieDetails mSelectedMovie = parentIntent.getParcelableExtra(MovieDetails.MyParcelName);
            if (mSelectedMovie.isAdult()) isAdult += " | " + getString(R.string.details_movie_is_adult);

            mTitle.setText(mSelectedMovie.getTitle());

            //Set Voter Rating
            mRating.setText(String.valueOf(mSelectedMovie.getVote_average()));

            //Set Popularity
            mPopularity.setText(String.valueOf(mSelectedMovie.getPopularity()));

            //Set Extra info ~~ date | lang | ?adult
            mMoreInfo.setText(new StringBuilder(getString(R.string.details_heading_release)
                    + ": " + mSelectedMovie.getRelease_date()
                    + " | " + mSelectedMovie.getOriginal_language() + isAdult));

            //Set Synopsis info
            mSynopsis.setText(mSelectedMovie.getOverview());

            //Build Backdrop URL
            url = NetUtils.buildImageURL(Constants.TMDBBackdropWidthBig,mSelectedMovie.getBackdrop_path());

            // TODO Convert Picasso to a macro...
            //NetUtils.picassoGet(urlString, mainViewAdapterViewHolder.mainViewImageView);

            Picasso.get()
                    .load(url.toString())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(mBackdrop);

            //Build Poster URL
            url = NetUtils.buildImageURL(Constants.TMDBPosterWidthBig,mSelectedMovie.getPoster_path());

            //NetUtils.picassoGet(urlString, mainViewAdapterViewHolder.mainViewImageView);

            Picasso.get()
                    .load(url.toString())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(mPoster);


        }
    }
}
