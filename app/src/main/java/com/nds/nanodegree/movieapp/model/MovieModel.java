package com.nds.nanodegree.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/*Movie model class
* */
/**
 * Created by Namrata Shah on 2/26/2017.
 */
public class MovieModel implements Parcelable{

    private final String ID;
    private final String title;
    private final String posterURL;
    private String backDropURL;
    private String overview;
    private String releaseDate;
    private String originalTitle;
    private String voteAverage;

    public MovieModel(String id, String title, String posterURL){
        this.ID = id;
        this.title = title;
        this.posterURL = posterURL;
    }

    public String getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public String getBackDropURL() {
        return backDropURL;
    }

    public void setBackDropURL(String backDropURL) {
        this.backDropURL = backDropURL;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    protected MovieModel(Parcel in) {
        ID = in.readString();
        title = in.readString();
        posterURL = in.readString();
        backDropURL = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        originalTitle = in.readString();
        voteAverage = in.readString();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ID);
        parcel.writeString(title);
        parcel.writeString(posterURL);
        parcel.writeString(backDropURL);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(originalTitle);
        parcel.writeString(voteAverage);
    }
}
