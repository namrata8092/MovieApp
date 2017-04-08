package com.nds.nanodegree.movieapp.services;

import android.os.AsyncTask;

import com.nds.nanodegree.movieapp.Converter.MovieReviewConverter;
import com.nds.nanodegree.movieapp.common.Util;
import com.nds.nanodegree.movieapp.model.ReviewModel;
import com.nds.nanodegree.movieapp.model.ReviewSearchResultModel;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by Namrata Shah on 3/29/2017.
 */

public class FetchMovieReviews  extends AsyncTask<Object, Object, String> {

    private URL url;
    private MovieReviewResponse reviewResponse;

    public interface MovieReviewResponse{
        void getMovieReviewResults(List<ReviewModel> reviewModels);
    }

    public FetchMovieReviews(URL url, MovieReviewResponse response){
        this.url = url;
        this.reviewResponse = response;
    }

    @Override
    protected String doInBackground(Object... voids) {
        try{
            String movieSearchResult = Util.getResponseFromHttpURL(url);
            return movieSearchResult;
        }catch(IOException e){
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        ReviewSearchResultModel searchResult = MovieReviewConverter.getMovieReviewModel(result);

        if(reviewResponse != null){
            reviewResponse.getMovieReviewResults(searchResult.getReviews());
        }
    }
}
