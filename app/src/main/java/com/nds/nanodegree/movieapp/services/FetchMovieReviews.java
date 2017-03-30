package com.nds.nanodegree.movieapp.services;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.nds.nanodegree.movieapp.Converter.MovieReviewConverter;
import com.nds.nanodegree.movieapp.common.Util;
import com.nds.nanodegree.movieapp.model.ReviewSearchResultModel;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Namrata Shah on 3/29/2017.
 */

public class FetchMovieReviews  extends AsyncTask<Object, Object, String> {

    private URL url;
    private ProgressBar mMovieDataFetchProgressBar;
    private MovieReviewResponse reviewResponse;

    public interface MovieReviewResponse{
        void getMovieReviewResults(ReviewSearchResultModel result);
    }

    public FetchMovieReviews(ProgressBar progressBar, URL url, MovieReviewResponse response){
        this.url = url;
        this.reviewResponse = response;
        this.mMovieDataFetchProgressBar = progressBar;
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
    protected void onPreExecute() {
        super.onPreExecute();
        displayProgressBar();
    }

    /**
     * display progressbar Whenever there is server call.
     */
    private void displayProgressBar() {
        mMovieDataFetchProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        ReviewSearchResultModel searchResult = MovieReviewConverter.getMovieReviewModel(result);

        if(reviewResponse != null){
            reviewResponse.getMovieReviewResults(searchResult);
        }

        hideProgressBar();
    }

    /**
     * hide progressbar after getting response from server
     * and data is parsed to model object.
     */
    private void hideProgressBar() {
        mMovieDataFetchProgressBar.setVisibility(View.GONE);
    }
}
