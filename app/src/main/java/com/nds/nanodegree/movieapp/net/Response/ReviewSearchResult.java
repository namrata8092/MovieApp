package com.nds.nanodegree.movieapp.net.Response;

import com.google.gson.annotations.SerializedName;
import com.nds.nanodegree.movieapp.net.tos.MovieReviews;

/**
 * Created by Namrata Shah on 3/27/2017.
 */

public class ReviewSearchResult {

    @SerializedName("reviews")
    private MovieReviews reviews;

    public MovieReviews getReviews() {
        return reviews;
    }

    @Override
    public String toString() {
        return "ReviewSearchResult{" +
                "reviews=" + reviews +
                '}';
    }
}
