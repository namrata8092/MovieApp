package com.nds.nanodegree.movieapp.views.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nds.nanodegree.movieapp.R;
import com.nds.nanodegree.movieapp.model.ReviewModel;

import java.util.List;

/**
 * Created by Namrata Shah on 3/29/2017.
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder> {
    private LayoutInflater layoutInflater;
    private List<ReviewModel> reviews;

    public MovieReviewAdapter(Context context, List<ReviewModel> reviewModels){
        this.layoutInflater = LayoutInflater.from(context);
        this.reviews = reviewModels;
    }

    public class MovieReviewViewHolder extends RecyclerView.ViewHolder {
        TextView mAuthorTextView;
        TextView mContentTextView;

        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = (TextView)itemView.findViewById(R.id.reviewAuthorTextView);
            mContentTextView = (TextView)itemView.findViewById(R.id.reviewContentTextView);
        }
    }

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.movie_review_row, parent, false);
        return new MovieReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        ReviewModel model = reviews.get(position);
        holder.mAuthorTextView.setText(model.getAuthor().trim());
        holder.mContentTextView.setText(model.getContent().trim());
    }

    @Override
    public int getItemCount() {
        if(reviews != null)
            return reviews.size();
        return 0;
    }
}
