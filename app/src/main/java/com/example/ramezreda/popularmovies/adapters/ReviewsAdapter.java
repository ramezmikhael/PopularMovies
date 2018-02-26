package com.example.ramezreda.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ramezreda.popularmovies.R;
import com.example.ramezreda.popularmovies.models.Review;

import java.util.List;

/**
 * Created by Ramez on 2/25/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private final List<Review> mReviewsList;

    public ReviewsAdapter(Context mContext, List<Review> mTrailersList) {
        this.mContext = mContext;
        this.mReviewsList = mTrailersList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ReviewViewHolder)holder).tvAuthor.setText(mReviewsList.get(position).getAuthor());
        ((ReviewViewHolder)holder).tvContent.setText(mReviewsList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewsList.size();
    }

    private class ReviewViewHolder extends RecyclerView.ViewHolder  {
        final TextView tvAuthor;
        final TextView tvContent;

        public ReviewViewHolder(View view) {
            super(view);
            tvAuthor = view.findViewById(R.id.author);
            tvContent = view.findViewById(R.id.content);
        }
    }
}
