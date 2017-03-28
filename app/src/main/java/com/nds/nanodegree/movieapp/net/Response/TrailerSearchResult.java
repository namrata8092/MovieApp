package com.nds.nanodegree.movieapp.net.Response;

import com.google.gson.annotations.SerializedName;
import com.nds.nanodegree.movieapp.net.tos.MovieTrailerResult;

/**
 * Created by Namrata Shah on 3/27/2017.
 */

public class TrailerSearchResult {

    @SerializedName("videos")
    private MovieTrailerResult trailerResult;

    public MovieTrailerResult getTrailerResult() {
        return trailerResult;
    }

    @Override
    public String toString() {
        return "TrailerSearchResult{" +
                "trailerResult=" + trailerResult +
                '}';
    }

}
