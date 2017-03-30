package com.nds.nanodegree.movieapp.Converter;

import com.nds.nanodegree.movieapp.common.JSONSerializeHelper;
import com.nds.nanodegree.movieapp.model.TrailerModel;
import com.nds.nanodegree.movieapp.model.TrailerSearchResultModel;
import com.nds.nanodegree.movieapp.net.Response.TrailerSearchResult;
import com.nds.nanodegree.movieapp.net.tos.MovieTrailers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Namrata Shah on 3/29/2017.
 */

public final class MovieTrailerConverter {
    public MovieTrailerConverter() {
    }

    public static TrailerSearchResultModel getMovieTrailerModel(String trailerResult) {
        TrailerSearchResult result = JSONSerializeHelper.deserializeObject(TrailerSearchResult.class, trailerResult);
        if (result.getTrailerResult() != null) {
            TrailerSearchResultModel searchResult = new TrailerSearchResultModel(toModel(result.getTrailerResult().getResults()));
            return searchResult;
        } else
            return null;

    }

    private static List<TrailerModel> toModel(List<MovieTrailers> results) {
        if (results == null || results.isEmpty() || results.size() == 0)
            return null;
        List<TrailerModel> trailers = new ArrayList<>();
        for (MovieTrailers trailer : results) {
            if(trailer.getType().equalsIgnoreCase("trailer")){
                TrailerModel model = new TrailerModel(trailer.getName(), trailer.getKey());
                trailers.add(model);
            }
        }
        return trailers;
    }
}
