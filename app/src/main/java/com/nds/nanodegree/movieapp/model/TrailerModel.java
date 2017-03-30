package com.nds.nanodegree.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Namrata Shah on 3/27/2017.
 */

public class TrailerModel implements Parcelable{

    private final String trailerName;
    private final String trailerKey;


    public TrailerModel(String name, String key){
        this.trailerName = name;
        this.trailerKey = key;
    }

    protected TrailerModel(Parcel in) {
        trailerName = in.readString();
        trailerKey = in.readString();
    }

    public static final Creator<TrailerModel> CREATOR = new Creator<TrailerModel>() {
        @Override
        public TrailerModel createFromParcel(Parcel in) {
            return new TrailerModel(in);
        }

        @Override
        public TrailerModel[] newArray(int size) {
            return new TrailerModel[size];
        }
    };

    public String getTrailerName() {
        return trailerName;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailerName);
        dest.writeString(trailerKey);
    }
}
