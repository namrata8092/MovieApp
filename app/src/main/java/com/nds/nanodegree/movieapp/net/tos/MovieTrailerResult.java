package com.nds.nanodegree.movieapp.net.tos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Namrata Shah on 3/27/2017.
 */

public class MovieTrailerResult {

    @SerializedName("results")
    private List<MovieTrailers> results;

    public List<MovieTrailers> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "MovieTrailerResult{" +
                "results=" + results +
                '}';
    }
}
