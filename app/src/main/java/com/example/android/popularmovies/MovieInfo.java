package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieInfo implements Parcelable {

    private static final String POSTER_PATH_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185/";
    private static final String BACKDROP_SIZE = "w342/";
    private int id;
    private String title;
    private String posterPath;
    private String backdropPath;
    private String plotSynopsis;
    private int userRating;
    private String releaseDate;

    public MovieInfo(int id, String title, String posterPathEnd, String backdropPathEnd, String plotSynopsis, int userRating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterPath = createPosterPath(posterPathEnd);
        this.backdropPath = createBackdropPath(backdropPathEnd);
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    private String createPosterPath(String posterPathEnd) {
        return POSTER_PATH_BASE_URL + POSTER_SIZE + posterPathEnd;
    }

    private String createBackdropPath(String backdropPathEnd) {
        return POSTER_PATH_BASE_URL + BACKDROP_SIZE + backdropPathEnd;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public int getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String toString() {
        return title + ": " + id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
        dest.writeString(this.plotSynopsis);
        dest.writeInt(this.userRating);
        dest.writeString(this.releaseDate);
    }

    protected MovieInfo(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.plotSynopsis = in.readString();
        this.userRating = in.readInt();
        this.releaseDate = in.readString();
    }

    public static final Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel source) {
            return new MovieInfo(source);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
}
