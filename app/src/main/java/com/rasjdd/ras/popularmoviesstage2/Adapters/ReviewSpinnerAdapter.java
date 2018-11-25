package com.rasjdd.ras.popularmoviesstage2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rasjdd.ras.popularmoviesstage2.Models.DetailModels.Review;
import com.rasjdd.ras.popularmoviesstage2.R;

import java.util.ArrayList;

public class ReviewSpinnerAdapter extends RecyclerView.Adapter<ReviewSpinnerAdapter.ReviewViewAdapterViewHolder> {

    private ArrayList<Review> mReviewList;

    private final ReviewOnClickHandler mClickHandler;

    public interface ReviewOnClickHandler {
        void onReviewClick(Review review);
    }

    public ReviewSpinnerAdapter(ReviewOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewAdapterViewHolder reviewViewAdapterViewHolder, int i) {
        String authorString = "- \"" + mReviewList.get(i).getAuthor() + "\"";
        reviewViewAdapterViewHolder.authorView.setText(authorString);
        if (mReviewList.get(i).getContent().length() <= 450)
            reviewViewAdapterViewHolder.moreInfoView.setVisibility(View.GONE);
        reviewViewAdapterViewHolder.contentView.setText(mReviewList.get(i).getContent());
    }

    @NonNull
    @Override
    public ReviewViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int reviewItemID = R.layout.detail_review;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(reviewItemID, viewGroup, false);

        return new ReviewViewAdapterViewHolder(view);
    }

    public class ReviewViewAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private final TextView authorView;
        private final TextView contentView;
        private final TextView moreInfoView;

        public ReviewViewAdapterViewHolder(View itemView) {
            super(itemView);
            moreInfoView = itemView.findViewById(R.id.lblMoreReview);
            authorView = itemView.findViewById(R.id.contentAuthor);
            contentView = itemView.findViewById(R.id.contentReview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Review mResult = mReviewList.get(getAdapterPosition());
            mClickHandler.onReviewClick(mResult);
        }
    }

    public int getItemCount() {
        if (mReviewList == null) return 0;
        return mReviewList.size();
    }

    public void setReviewList(ArrayList<Review> results) {
        mReviewList = results;
        notifyDataSetChanged();
    }

}
