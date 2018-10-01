package com.rasjdd.ras.popularmoviesstage1.Adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.rasjdd.ras.popularmoviesstage1.MainActivity;
import com.rasjdd.ras.popularmoviesstage1.Models.MovieDetails;
import com.rasjdd.ras.popularmoviesstage1.Models.MovieList;
import com.rasjdd.ras.popularmoviesstage1.R;
import com.rasjdd.ras.popularmoviesstage1.Utilities.Constants;
import com.rasjdd.ras.popularmoviesstage1.Utilities.NetUtils;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.MainViewAdapterViewHolder> {

    private ArrayList<MovieList.ResultList> mMovieList;

    private final MainAdapterOnClickHandler mClickHandler;

    public interface MainAdapterOnClickHandler {
        void onClick(MovieList.ResultList result);
    }

    public MainViewAdapter(MainAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MainViewAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public final ImageView mainViewImageView;

        public MainViewAdapterViewHolder(View itemView) {
            super(itemView);
            mainViewImageView = itemView.findViewById(R.id.iv_gallery_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MovieList.ResultList mResult = mMovieList.get(getAdapterPosition());
            mClickHandler.onClick(mResult);
        }
    }

    @NonNull
    @Override
    public MainViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int galleryItemID = R.layout.gallery_layout;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(galleryItemID, viewGroup, false);

        return new MainViewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewAdapterViewHolder mainViewAdapterViewHolder, int i) {
        String imageAddr = mMovieList.get(i).getPoster_path();
        URL imagePath = NetUtils.buildImageURL(Constants.TMDBWidthBig,imageAddr.substring(1));

        Picasso.get()
                .load(imagePath.toString())
                .placeholder(R.drawable.ic_image_placeholder)
                .into(mainViewAdapterViewHolder.mainViewImageView);

    }

    public int getItemCount() {
        if (mMovieList == null) return 0;
        return mMovieList.size();
    }

    public void setMovieList(ArrayList<MovieList.ResultList> results) {
        mMovieList = results;
        notifyDataSetChanged();
    }
}
