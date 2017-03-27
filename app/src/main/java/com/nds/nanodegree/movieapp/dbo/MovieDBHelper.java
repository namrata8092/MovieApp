package com.nds.nanodegree.movieapp.dbo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Namrata Shah on 3/26/2017.
 * MovieDBHelper creates SQL database for movie & update database.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";

    public static final int DATABASE_VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE= "CREATE TABLE "+ MovieContract.MovieEntry.TABLE_NAME+
                " ("+ MovieContract.MovieEntry._ID+"INTEGER PRIMARY KEY AS INTEGER,"
            + MovieContract.MovieEntry.COLUMN_NAME_MOVIE_TITLE+" TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_NAME_MOVIE_SUBTITLE+" TEXT NOT NULL,"
            + MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID+" INTEGER NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_NAME_MOVIE_POSTER_URL+" TEXT NOT NULL, "
            + MovieContract.MovieEntry.COLUMN_NAME_MOVIE_FAVORITE+" BOOLEAN "+");";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
