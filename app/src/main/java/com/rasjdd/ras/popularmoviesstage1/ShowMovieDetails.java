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
            String isAdult = "";
            MovieDetails mSelectedMovie = parentIntent.getParcelableExtra(MovieDetails.MyParcelName);
            if (mSelectedMovie.isAdult() == false) isAdult += " | (ADULT)";

            mTitle.setText(mSelectedMovie.getTitle());
            mRating.setText(String.valueOf("Rating: " + mSelectedMovie.getVote_average()));
            mPopularity.setText(String.valueOf("Popularity: " + mSelectedMovie.getPopularity()));
            mMoreInfo.setText(new StringBuilder(
                    "Released: " + mSelectedMovie.getRelease_date()
                            + " | " + mSelectedMovie.getOriginal_language() + isAdult));
            mSynopsis.setText(mSelectedMovie.getOverview());

            URL url;
            String imageUrl;
            if (mSelectedMovie.getBackdrop_path() != null) {
                String s = mSelectedMovie.getBackdrop_path();
                url = NetUtils.buildImageURL(Constants.TMDBWidthBig,s.substring(1));
                imageUrl = url.toString();
            } else imageUrl = Constants.TMDBLogoUrl;

            // TODO Convert Picasso to a macro...
            //NetUtils.picassoGet(urlString, mainViewAdapterViewHolder.mainViewImageView);

            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(mBackdrop);

            if (mSelectedMovie.getPoster_path() != null) {
                String s = mSelectedMovie.getPoster_path();
                url = NetUtils.buildImageURL(Constants.TMDBWidthBig,s.substring(1));
                imageUrl = url.toString();
            } else imageUrl = Constants.TMDBLogoUrl;

            //NetUtils.picassoGet(urlString, mainViewAdapterViewHolder.mainViewImageView);

            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(mPoster);


        }
    }
}
