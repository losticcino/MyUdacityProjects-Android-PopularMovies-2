package com.rasjdd.ras.popularmoviesstage2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.MovieListDetailResponse;
import com.rasjdd.ras.popularmoviesstage2.R;
import com.rasjdd.ras.popularmoviesstage2.Utilities.Constants;
import com.rasjdd.ras.popularmoviesstage2.Utilities.NetUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.MainViewAdapterViewHolder> {

    private ArrayList<MovieListDetailResponse> mMovieList;

    private final MainAdapterOnClickHandler mClickHandler;

    public interface MainAdapterOnClickHandler {
        void onMovieDetailsClick(MovieListDetailResponse result);
    }

    public MainViewAdapter(MainAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MainViewAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
         final ImageView mainViewImageView;

        MainViewAdapterViewHolder(View itemView) {
            super(itemView);
            mainViewImageView = itemView.findViewById(R.id.imageGalleryPoster);
//            int position = getAdapterPosition();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MovieListDetailResponse mResult = mMovieList.get(getAdapterPosition());
            mClickHandler.onMovieDetailsClick(mResult);
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
        String imageAddress = mMovieList.get(i).getPoster_path();
        String urlString = Constants.TMDBLogoUrl;
        if (imageAddress != null) {
            URL imagePath = NetUtils.buildImageURL(Constants.TMDBPosterWidthBig,imageAddress.substring(1));
            urlString = imagePath.toString();
        }

        Picasso.get()
                .load(urlString)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(mainViewAdapterViewHolder.mainViewImageView);

    }

    public int getItemCount() {
        if (mMovieList == null) return 0;
        return mMovieList.size();
    }

    public void setMovieList(ArrayList<MovieListDetailResponse> results) {
        mMovieList = results;
        notifyDataSetChanged();
    }

    public ArrayList<MovieListDetailResponse> getMovieList() {
        return mMovieList;
    }

    public void appendMovieList(ArrayList<MovieListDetailResponse> appends) {
        for (MovieListDetailResponse append : appends) {
            mMovieList.add(append);
        }
        notifyDataSetChanged();
    }
}
