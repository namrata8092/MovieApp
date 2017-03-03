package com.nds.nanodegree.movieapp.services;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.nds.nanodegree.movieapp.Converter.MovieSearchConverter;
import com.nds.nanodegree.movieapp.common.Util;
import com.nds.nanodegree.movieapp.model.MovieSearchResult;

import java.io.IOException;
import java.net.URL;

/** FetchMovieData async task will make server call to TMDB to retrieve movie details based on query / URL.
* MovieResponse interface is to pass movie data as a result of async task.
* */

/**
 * Created by Namrata Shah on 2/26/2017.
 */
public class FetchMovieData extends AsyncTask<Object, Object, String> {

    private URL url;
    private ProgressBar mMovieDataFetchProgressBar;
    private MovieResponse movieResponse;

    public interface MovieResponse{
        void getMovieSearchResults(MovieSearchResult result);
    }

    public FetchMovieData(ProgressBar progressBar, URL url, MovieResponse response){
        this.url = url;
        this.movieResponse = response;
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
        MovieSearchResult searchResult = MovieSearchConverter.getMovieSearchModel(result);

        if(movieResponse != null){
            movieResponse.getMovieSearchResults(searchResult);
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
