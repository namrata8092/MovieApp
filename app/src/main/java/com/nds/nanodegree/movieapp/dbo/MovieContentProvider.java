package com.nds.nanodegree.movieapp.dbo;

import android.content.ContentProvider;
import android.content.ContentUris;
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
    private static final int MOVIE_DETAIL_TASK = 100;
    private static final int SELECTED_MOVIE_DETAIL_TASK = 101;
    private static UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.DETAIL_PATH, MOVIE_DETAIL_TASK);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.DETAIL_PATH+"/#", SELECTED_MOVIE_DETAIL_TASK);
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
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase movieDB = mMovieDBHelper.getWritableDatabase();
        int uriMatch = sUriMatcher.match(uri);
        Uri returnUri;
        switch (uriMatch){
            case MOVIE_DETAIL_TASK :
                long insertionId = movieDB.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if(insertionId > 0){
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.DETAIL_URI,insertionId);
                    return returnUri;
                }else{
                    throw new SQLiteException("Insertion failed "+uri);
                }
            default:
                throw new UnsupportedOperationException("Unknown URI "+uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
