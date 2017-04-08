package com.nds.nanodegree.movieapp.services;

import android.os.AsyncTask;

import com.nds.nanodegree.movieapp.Converter.MovieTrailerConverter;
import com.nds.nanodegree.movieapp.common.Util;
import com.nds.nanodegree.movieapp.model.TrailerModel;
import com.nds.nanodegree.movieapp.model.TrailerSearchResultModel;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by Namrata Shah on 3/29/2017.
 */

public class FetchMovieTrailers extends AsyncTask<Object, Object, String> {

    private URL url;
    private MovieTrailerResponse trailerResponse;

    public interface MovieTrailerResponse{
        void getMovieTrailerResults(List<TrailerModel> result);
    }

    public FetchMovieTrailers( URL url, MovieTrailerResponse response){
        this.url = url;
        this.trailerResponse = response;
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
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        TrailerSearchResultModel searchResult = MovieTrailerConverter.getMovieTrailerModel(result);

        if (trailerResponse != null && searchResult != null) {
            trailerResponse.getMovieTrailerResults(searchResult.getTrailers());
        }
    }

}