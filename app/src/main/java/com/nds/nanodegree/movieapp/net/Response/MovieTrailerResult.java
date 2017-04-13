package com.nds.nanodegree.movieapp.net.Response;

import com.google.gson.annotations.SerializedName;
import com.nds.nanodegree.movieapp.net.tos.MovieTrailers;

import java.util.List;

/**Movie trailer response transfer object
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
