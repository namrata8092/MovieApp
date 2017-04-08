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
    public static final String MOVIE_SORT_CHOICE_BUNDLE_KEY ="movieSortChoiceBundle";
    public static final String MOVIE_LIST_BUNDLE_KEY="movieListBundle";
    public static final String SELECTED_MOVIE_DETAIL_BUNDLE_KEY="selectedMovieDetail";
    public static final int SEARCH_BY_POPULARITY=0;
    public static final int SEARCH_BY_RATING=1;
    public static final int SEARCH_BY_FAVORITE=2;

    public static final int TRAILER_URL=3;
    public static final int REVIEW_URL=4;

    public static final String MOVIE_POSTER_FRAGMENT_TAG="movie_poster";
    public static final String MOVIE_DETAIL_FRAGMENT_TAG="movie_detail";

    public static final int REMOVED_FROM_FAVORITE = 0;
    public static final int ADDED_TO_FAVORITE = 1;
    public static final String YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=";

    public static final String MOVIE_BASE_URL="https://api.themoviedb.org/3";
    public static final String MOVIE_KEY="movie";
    public static final String REVIEW_KEY="reviews";
    public static final String VIDEO_KEY="videos";


}



