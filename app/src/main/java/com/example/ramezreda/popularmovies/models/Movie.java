package com.example.ramezreda.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RamezReda on 2/17/2018.
 */

public class Movie implements Parcelable{
    private final int id;
    private final String title;
    private final String posterPath;
    private final String overview;
    private final double voteAverage;
    private final String releaseDate;

    public Movie(int id, String originalTitle, String imageURL, String overview, double userRating, String releaseDate) {
        this.id = id;
        this.title = originalTitle;
        this.posterPath = imageURL;
        this.overview = overview;
        this.voteAverage = userRating;
        this.releaseDate = releaseDate;
    }

    private Movie(Parcel parcel) {
        id = parcel.readInt();
        title = parcel.readString();
        posterPath = parcel.readString();
        overview = parcel.readString();
        voteAverage = parcel.readDouble();
        releaseDate = parcel.readString();
    }

    public int getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeDouble(voteAverage);
        parcel.writeString(releaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new ClassLoaderCreator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return null;
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

        @Override
        public Movie createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return new Movie(parcel);
        }
    };
}
