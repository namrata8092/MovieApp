package com.nds.nanodegree.movieapp.dbo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Namrata Shah on 3/26/2017.
 * MovieContentProvider is content provider that helps to insert / update / delete / query movie database.
 */

public class MovieContentProvider extends ContentProvider {

    private MovieDBHelper mMovieDBHelper;
    private static final int FAV_MOVIE_TASK = 100;
    private static final int FAV_SELECTED_MOVIE_TASK = 101;
    private static UriMatcher sUriMatcher = buildUriMatcher();

    /**
     * URI matcher added all possible URIs
     * @return
     */
    private static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.FAVORITE_PATH, FAV_MOVIE_TASK);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.FAVORITE_PATH+"/#", FAV_SELECTED_MOVIE_TASK);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDBHelper = new MovieDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase movieDb = mMovieDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor queriedCursor;
        switch (match){
            case FAV_MOVIE_TASK:
                queriedCursor = movieDb.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new SQLiteException("Query failed "+uri);
        }
        queriedCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return queriedCursor;
    }

    /**
     * returns best matching uri
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        final int uriMatching = sUriMatcher.match(uri);
        switch (uriMatching){
            case FAV_MOVIE_TASK:
                return MovieContract.MovieEntry.CONTENT_TYPE_URI;
            case FAV_SELECTED_MOVIE_TASK:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE_URI;
        }
        throw new UnsupportedOperationException("Unknown uri "+uri);
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase movieDB = mMovieDBHelper.getWritableDatabase();
        final int uriMatch = sUriMatcher.match(uri);
        Uri returnUri;
        switch (uriMatch){
            case FAV_MOVIE_TASK:
                long insertionId = movieDB.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if(insertionId > 0){
                    returnUri = MovieContract.MovieEntry.buildSelectedMovieUri(insertionId);
                }else{
                    throw new SQLiteException("Insertion failed "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI "+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase movieDB = mMovieDBHelper.getWritableDatabase();
        final int uriMatch = sUriMatcher.match(uri);
        int rowDeleted = 0;
        switch (uriMatch) {
            case FAV_MOVIE_TASK:
                rowDeleted = movieDB.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
        if (rowDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase movieDB = mMovieDBHelper.getWritableDatabase();
        final int uriMatch = sUriMatcher.match(uri);
        int rowUpdated = 0;
        switch (uriMatch) {
            case FAV_MOVIE_TASK:
                rowUpdated = movieDB.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
        if (rowUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowUpdated;
    }
}
