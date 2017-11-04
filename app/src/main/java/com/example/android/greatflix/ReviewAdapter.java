package com.example.android.greatflix;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.greatflix.objects.Reviews;

import java.util.List;

/**
 * Created by TRAVIS on 2017-11-02.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {


    private List<Reviews> mReviewData;

    public ReviewAdapter(){

    }
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View reviewView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_recycler_view_layout,parent,false);

        return new ReviewAdapterViewHolder(reviewView);
    }
    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mAuthorTextView;
        public final TextView mReviewTextView;


        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.tv_author_holder);
            mReviewTextView = (TextView) itemView.findViewById(R.id.tv_review_holder);

        }
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        holder.mAuthorTextView.setText(mReviewData.get(position).getAuthor());
        holder.mReviewTextView.setText(mReviewData.get(position).getContents());

    }

    @Override
    public int getItemCount() {
        if (null == mReviewData)return 0;
        return mReviewData.size();
    }

    public void setReviewData(List<Reviews> reviewData) {
        mReviewData = reviewData;
        notifyDataSetChanged();


    }
}
