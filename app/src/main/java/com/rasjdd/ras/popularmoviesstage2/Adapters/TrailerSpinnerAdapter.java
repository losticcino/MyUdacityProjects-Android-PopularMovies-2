package com.rasjdd.ras.popularmoviesstage2.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.Video;
import com.rasjdd.ras.popularmoviesstage2.R;
import com.rasjdd.ras.popularmoviesstage2.Utilities.Constants;
import com.rasjdd.ras.popularmoviesstage2.Utilities.NetUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class TrailerSpinnerAdapter extends RecyclerView.Adapter<TrailerSpinnerAdapter.TrailerSpinnerViewHolder> {

    private ArrayList<Video> mVideoList;

    private final TrailerOnClickHandler mClickHandler;

    public interface TrailerOnClickHandler {
        void onTrailerDetailsClick(Video result);
    }

    public TrailerSpinnerAdapter(TrailerOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class TrailerSpinnerViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private final ImageView trailerImageView;

        public TrailerSpinnerViewHolder(View itemView) {
            super(itemView);
            trailerImageView = itemView.findViewById(R.id.imageTrailerPoster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Video mResult = mVideoList.get(getAdapterPosition());
            mClickHandler.onTrailerDetailsClick(mResult);
        }
    }

    @NonNull
    @Override
    public TrailerSpinnerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int galleryItemID = R.layout.detail_trailer;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(galleryItemID, viewGroup, false);

        return new TrailerSpinnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerSpinnerViewHolder trailerSpinnerViewHolder, int i) {
        String videoIdentifier = mVideoList.get(i).getKey();
        String urlString = Constants.TMDBLogoUrl;
        if (videoIdentifier != null) {
            URL imagePath = NetUtils.buildYouTubeThumbnailURL(videoIdentifier);
            urlString = imagePath.toString();
        }

        Picasso.get()
                .load(urlString)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(trailerSpinnerViewHolder.trailerImageView);

    }

    public int getItemCount() {
        if (mVideoList == null) return 0;
        return mVideoList.size();
    }

    public void setTrailerList(ArrayList<Video> results) {
        mVideoList = results;
        notifyDataSetChanged();
    }

}
