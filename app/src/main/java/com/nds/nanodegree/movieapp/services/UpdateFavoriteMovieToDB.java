package com.nds.nanodegree.movieapp.services;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.nds.nanodegree.movieapp.common.Constants;
import com.nds.nanodegree.movieapp.dbo.MovieContract;
import com.nds.nanodegree.movieapp.model.FavoriteMovieUpdateListener;
import com.nds.nanodegree.movieapp.model.MovieModel;

/**UpdateFavoriteMovieToDB will update favorite movie to database.
 * If movie is not saved, it will be added to DB.
 * If movie already exist, it will be deleted from DB.
 * Created by Namrata Shah on 4/4/2017.
 */

public class UpdateFavoriteMovieToDB extends AsyncTask {

    private Context mContext;
    private MovieModel movieModel;
    private FavoriteMovieUpdateListener updateListener;

    public UpdateFavoriteMovieToDB(Context cnt, MovieModel model, FavoriteMovieUpdateListener listener){
        this.mContext = cnt;
        this.movieModel = model;
        this.updateListener = listener;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        AddOrRemoveFavoriteMovie();
        return null;
    }

    private void AddOrRemoveFavoriteMovie() {
        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID},
                MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?",
                new String[]{String.valueOf(movieModel.getID())},
                null);

        if (movieCursor.moveToFirst()) {
            int rowDeleted = mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?",
                    new String[]{String.valueOf(movieModel.getID())});

            if (rowDeleted > 0) {
                updateListener.onSuccess(Constants.REMOVED_FROM_FAVORITE);
            } else {
                updateListener.onFailure();
            }

        } else {
            ContentValues values = new ContentValues();

            values.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID, movieModel.getID());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_TITLE, movieModel.getTitle());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ORIGINAL_TITLE, movieModel.getOriginalTitle());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_POSTER_URL, movieModel.getPosterURL());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_BACKDROP_URL, movieModel.getBackDropURL());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_OVERVIEW, movieModel.getOverview());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_VOTE_AVERAGE, movieModel.getVoteAverage());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_RELEASE_DATE, movieModel.getReleaseDate());

            Uri insertedUri = mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,values);

            long movieRowId = ContentUris.parseId(insertedUri);

            if (movieRowId > 0) {
                updateListener.onSuccess(Constants.ADDED_TO_FAVORITE);
            } else {
                updateListener.onFailure();
            }
        }
        movieCursor.close();
    }
}
