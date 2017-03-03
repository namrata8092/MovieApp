package com.nds.nanodegree.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/*Movie search result model class.
* */
/**
 * Created by Namrata Shah on 2/26/2017.
 */
public class MovieSearchResult implements Parcelable{

    private final List<MovieModel> movieList;

    public MovieSearchResult(List<MovieModel> movies){
        this.movieList = movies;
    }

    public List<MovieModel> getMovieList() {
        return movieList;
    }

    protected MovieSearchResult(Parcel in) {
        movieList = in.createTypedArrayList(MovieModel.CREATOR);
    }

    public static final Creator<MovieSearchResult> CREATOR = new Creator<MovieSearchResult>() {
        @Override
        public MovieSearchResult createFromParcel(Parcel in) {
            return new MovieSearchResult(in);
        }

        @Override
        public MovieSearchResult[] newArray(int size) {
            return new MovieSearchResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(movieList);
    }
}
