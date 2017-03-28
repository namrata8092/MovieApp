package com.nds.nanodegree.movieapp.net.tos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Namrata Shah on 3/27/2017.
 */

public class MovieReviews {

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int total_pages;

    @SerializedName("total_results")
    private int total_results;

    @SerializedName("results")
    private List<MovieReviewResult> results;

    public int getPage() {
        return page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public List<MovieReviewResult> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "MovieReviews{" +
                "page=" + page +
                ", total_pages=" + total_pages +
                ", total_results=" + total_results +
                ", results=" + results +
                '}';
    }
}
