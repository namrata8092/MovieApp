package com.nds.nanodegree.movieapp.services;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.nds.nanodegree.movieapp.Converter.MovieTrailerConverter;
import com.nds.nanodegree.movieapp.common.Util;
import com.nds.nanodegree.movieapp.model.TrailerSearchResultModel;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Namrata Shah on 3/29/2017.
 */

public class FetchMovieTrailers extends AsyncTask<Object, Object, String> {

    private URL url;
    private ProgressBar mMovieDataFetchProgressBar;
    private MovieTrailerResponse trailerResponse;

    public interface MovieTrailerResponse{
        void getMovieTrailerResults(TrailerSearchResultModel result);
    }

    public FetchMovieTrailers(ProgressBar progressBar, URL url, MovieTrailerResponse response){
        this.url = url;
        this.trailerResponse = response;
        this.mMovieDataFetchProgressBar = progressBar;
    }

    @Override
    protected String doInBackground(Object... voids) {
        try{
            String trailerResult = Util.getResponseFromHttpURL(url);
            return trailerResult;
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
        TrailerSearchResultModel searchResult = MovieTrailerConverter.getMovieTrailerModel(result);

        if(trailerResponse != null){
            trailerResponse.getMovieTrailerResults(searchResult);
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