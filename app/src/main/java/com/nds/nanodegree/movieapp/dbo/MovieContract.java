package com.nds.nanodegree.movieapp.dbo;

import android.content.ContentResolver;
import android.content.ContentUris;
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
    public static final String FAVORITE_PATH="favoriteMovie";

    public static class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(FAVORITE_PATH).build();

        public static final String CONTENT_TYPE_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + FAVORITE_PATH;

        public static final String CONTENT_ITEM_TYPE_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + FAVORITE_PATH;

        public static final String TABLE_NAME = "favoriteMovie";

        public static final String COLUMN_NAME_MOVIE_ID = "movieId";
        public static final String COLUMN_NAME_MOVIE_TITLE = "title";
        public static final String COLUMN_NAME_MOVIE_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_NAME_MOVIE_POSTER_URL = "posterUrl";
        public static final String COLUMN_NAME_MOVIE_BACKDROP_URL = "backDropURL";
        public static final String COLUMN_NAME_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_NAME_MOVIE_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_NAME_MOVIE_RELEASE_DATE = "releaseDate";

        public static Uri buildSelectedMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
