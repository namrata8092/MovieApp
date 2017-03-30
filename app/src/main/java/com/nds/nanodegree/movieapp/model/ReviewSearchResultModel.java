package com.nds.nanodegree.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Namrata Shah  on 3/29/2017.
 */

public class ReviewSearchResultModel implements Parcelable{

    private List<ReviewModel> reviews;

    public ReviewSearchResultModel(List<ReviewModel> reviews){
        this.reviews = reviews;
    }

    protected ReviewSearchResultModel(Parcel in) {
        reviews = in.createTypedArrayList(ReviewModel.CREATOR);
    }

    public static final Creator<ReviewSearchResultModel> CREATOR = new Creator<ReviewSearchResultModel>() {
        @Override
        public ReviewSearchResultModel createFromParcel(Parcel in) {
            return new ReviewSearchResultModel(in);
        }

        @Override
        public ReviewSearchResultModel[] newArray(int size) {
            return new ReviewSearchResultModel[size];
        }
    };

    public List<ReviewModel> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewModel> reviews) {
        this.reviews = reviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(reviews);
    }
}
