package com.nds.nanodegree.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/** Movie review model.
 * Created by Namrata Shah on 3/27/2017.
 */

public class ReviewModel implements Parcelable{

    private final String author;
    private final String content;

    public ReviewModel(String auth, String cnt){
        this.author = auth;
        this.content = cnt;
    }

    protected ReviewModel(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<ReviewModel> CREATOR = new Creator<ReviewModel>() {
        @Override
        public ReviewModel createFromParcel(Parcel in) {
            return new ReviewModel(in);
        }

        @Override
        public ReviewModel[] newArray(int size) {
            return new ReviewModel[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
    }
}
