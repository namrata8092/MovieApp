package com.nds.nanodegree.movieapp.Converter;

import com.nds.nanodegree.movieapp.common.JSONSerializeHelper;
import com.nds.nanodegree.movieapp.model.ReviewModel;
import com.nds.nanodegree.movieapp.model.ReviewSearchResultModel;
import com.nds.nanodegree.movieapp.net.Response.ReviewSearchResult;
import com.nds.nanodegree.movieapp.net.tos.MovieReviewResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Namrata Shah on 3/29/2017.
 */

public final class MovieReviewConverter {

    public MovieReviewConverter(){}

    public static ReviewSearchResultModel getMovieReviewModel(String reviewResult){
        ReviewSearchResult result = JSONSerializeHelper.deserializeObject(ReviewSearchResult.class, reviewResult);
        if(result.getReviews() != null){
            ReviewSearchResultModel searchResult = new ReviewSearchResultModel(toModel(result.getReviews().getResults()));
            return searchResult;
        }else
            return null;

    }

    private static List<ReviewModel> toModel(List<MovieReviewResult> results) {
        if(results == null || results.isEmpty() || results.size() == 0)
            return null;
        List<ReviewModel> models = new ArrayList<>();
        for(MovieReviewResult review : results){
            ReviewModel model = new ReviewModel(review.getAuthor(), review.getContent());
            models.add(model);
        }
        return models;
    }

}
