package com.nds.nanodegree.movieapp.common;

import com.nds.nanodegree.movieapp.BuildConfig;

/**
 * Created by Namrata Shah on 2/26/2017.
 */
public final class Constants {

    public Constants(){
    }

    public static final String API_KEY = BuildConfig.API_KEY;
    public static final String MOVIE_SEARCH_BY_POPULAR = "http://api.themoviedb.org/3/movie/popular?";
    public static final String MOVIE_SEARCH_BY_TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated?";
    public static final String MOVIE_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String QUERY_PARAM_API_KEY="api_key";
    public static final String DEVICE_SIZE="w185";
    public static final String MOVIE_LIST_BUNDLE_KEY="movieListBundle";
    public static final String SELECTED_MOVIE_DETAIL_BUNDLE_KEY="selectedMovieDetail";
    public static final int SEARCH_BY_POPULARITY=0;
    public static final int SEARCH_BY_RATING=1;
    public static final int SEARCH_BY_FAVORITE=2;
}

