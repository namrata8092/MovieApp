package com.nds.nanodegree.movieapp.Converter;

import com.nds.nanodegree.movieapp.common.JSONSerializeHelper;
import com.nds.nanodegree.movieapp.model.ReviewModel;
import com.nds.nanodegree.movieapp.model.ReviewSearchResultModel;
import com.nds.nanodegree.movieapp.net.Response.MovieReviews;
import com.nds.nanodegree.movieapp.net.tos.MovieReviewResult;

import java.util.ArrayList;
import java.util.List;
/**
 * It converts Movie review transfer object to model.
 */

/**
 * Created by Namrata Shah on 3/29/2017.
 */

public final class MovieReviewConverter {

    public MovieReviewConverter(){}

    public static ReviewSearchResultModel getMovieReviewModel(String reviewResult){
        MovieReviews result = JSONSerializeHelper.deserializeObject(MovieReviews.class, reviewResult);
        if(result.getResults() != null){
            ReviewSearchResultModel searchResult = new ReviewSearchResultModel(toModel(result.getResults()));
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
