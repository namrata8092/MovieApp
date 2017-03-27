package com.nds.nanodegree.movieapp.dbo;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Namrata Shah on 3/26/2017.
 * MovieContract defines table name, table structure to store movie details.
 */

public final class MovieContract {
    private MovieContract() {
    }

    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.nds.nanodegree.movieapp";
    public static final Uri BASE_URI = Uri.parse(SCHEME + AUTHORITY);
    public static final String DETAIL_PATH="movieDetail";

    public static class MovieEntry implements BaseColumns {

        public static final Uri DETAIL_URI = BASE_URI.buildUpon().appendPath(DETAIL_PATH).build();

        public static final String TABLE_NAME = "movieDetail";

        public static final String COLUMN_NAME_MOVIE_TITLE = "title";
        public static final String COLUMN_NAME_MOVIE_SUBTITLE = "subTitle";
        public static final String COLUMN_NAME_MOVIE_POSTER_URL = "posterUrl";
        public static final String COLUMN_NAME_MOVIE_ID = "movieId";
        public static final String COLUMN_NAME_MOVIE_FAVORITE = "favorite";
    }
}
