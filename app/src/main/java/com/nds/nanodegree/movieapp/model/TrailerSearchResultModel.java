package com.nds.nanodegree.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Namrata Shah on 3/29/2017.
 */

public class TrailerSearchResultModel implements Parcelable {

    private List<TrailerModel> trailers;

    public TrailerSearchResultModel(List<TrailerModel> trailers){
        this.trailers = trailers;
    }

    protected TrailerSearchResultModel(Parcel in) {
        trailers = in.createTypedArrayList(TrailerModel.CREATOR);
    }

    public static final Creator<TrailerSearchResultModel> CREATOR = new Creator<TrailerSearchResultModel>() {
        @Override
        public TrailerSearchResultModel createFromParcel(Parcel in) {
            return new TrailerSearchResultModel(in);
        }

        @Override
        public TrailerSearchResultModel[] newArray(int size) {
            return new TrailerSearchResultModel[size];
        }
    };

    public List<TrailerModel> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<TrailerModel> trailers) {
        this.trailers = trailers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(trailers);
    }
}
