package com.nds.nanodegree.movieapp.Converter;

import com.nds.nanodegree.movieapp.common.JSONSerializeHelper;
import com.nds.nanodegree.movieapp.model.MovieModel;
import com.nds.nanodegree.movieapp.model.MovieSearchResult;
import com.nds.nanodegree.movieapp.net.Response.SearchResult;
import com.nds.nanodegree.movieapp.net.tos.Movie;

import java.util.ArrayList;
import java.util.List;

/*MovieSearchConverter to convert transfer object to model.
* */
/**
 * Created by Namrata Shah on 2/26/2017.
 */
public final class MovieSearchConverter {

    public MovieSearchConverter(){

    }

    public static MovieSearchResult getMovieSearchModel(String movieSearchResult){
        SearchResult result = JSONSerializeHelper.deserializeObject(SearchResult.class, movieSearchResult);
        MovieSearchResult model = new MovieSearchResult(toModel(result.getMovieResults()));
        return model;
    }

    private static List<MovieModel> toModel(List<Movie> movieResults) {
        if(movieResults == null || movieResults.size() == 0 || movieResults.isEmpty()){
            return null;
        }else{
            List<MovieModel> moviesModel = new ArrayList<>();
            for(Movie movie : movieResults){
                MovieModel movieModel = new MovieModel(Integer.toString(movie.getId()), movie.getTitle(), movie.getPosterPath());
                movieModel.setBackDropURL(movie.getBackdropPath());
                movieModel.setOriginalTitle(movie.getOriginalTitle());
                movieModel.setOverview(movie.getOverview());
                movieModel.setReleaseDate(movie.getReleaseDate());
                movieModel.setVoteAverage(Double.toString(movie.getVoteAverage()));
                moviesModel.add(movieModel);
            }
            return moviesModel;
        }
    }
}
