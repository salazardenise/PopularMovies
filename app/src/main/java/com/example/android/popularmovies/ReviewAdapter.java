package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private static final boolean shouldAttachToParentImmediately = false;
    private Review[] mReviews;

    @NonNull
    @Override
    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.review_item, parent, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewAdapterViewHolder holder, int position) {
        Review review = mReviews[position];
        String author = review.getAuthor();
        String content = review.getContent();
        String url = review.getUrl();

        holder.author.setText(author);
        holder.content.setText(content);
        holder.url.setText(url);
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) {
            return 0;
        }
        return mReviews.length;
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView author;
        public final TextView content;
        public final TextView url;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.review_author);
            content = (TextView) itemView.findViewById(R.id.review_content);
            url = (TextView) itemView.findViewById(R.id.reivew_url);
        }
    }

    /**
    * This method is used to set the reviews on a ReviewAdapter if we've already
    * created one. This is handy when we get new data from the web but don't want to create a
    * new ReviewAdapter to display it.
    *
    * @param reviews The new reviews data to be displayed.
    */
    public void setReviewData(Review[] reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }
}
