package com.rasjdd.ras.popularmoviesstage1.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.rasjdd.ras.popularmoviesstage1.Models.MovieDetails;
import com.rasjdd.ras.popularmoviesstage1.Models.MovieList;
import com.rasjdd.ras.popularmoviesstage1.R;
import com.rasjdd.ras.popularmoviesstage1.Utilities.Constants;
import com.rasjdd.ras.popularmoviesstage1.Utilities.NetUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.MainViewAdapterViewHolder> {

    private ArrayList<MovieList.ResultList> mMovieList;

    private final MainAdapterOnClickHandler mClickHandler;

    private interface MainAdapterOnClickHandler {
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
            MovieList.ResultList thisResult = mMovieList.get(getAdapterPosition());
            mClickHandler.onClick(thisResult);
        }
    }

    @Override
    public MainViewAdapterViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int galleryItemID = R.layout.gallery_layout;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(galleryItemID, viewGroup, true);

        return new MainViewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewAdapterViewHolder mainViewAdapterViewHolder, int i) {
        String imageAddr = mMovieList.get(i).getPoster_path();
        URL imagePath = NetUtils.buildImageURL(Constants.TMDBWidthBig,imageAddr);
        Uri uri = Uri.parse(imagePath.toString());

        mainViewAdapterViewHolder.mainViewImageView.setImageURI(uri);
    }

    public int getItemCount() {
        if (mMovieList == null) return 0;
        return mMovieList.size();
    }

    public void setmMovieList(ArrayList<MovieList.ResultList> mMovieList) {
        this.mMovieList = mMovieList;
        notifyDataSetChanged();
    }
}
