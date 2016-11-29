package com.udacity.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sagar on 21/11/16.
 */

public class Movie implements Parcelable {

    private int id;
    private String title;
    private String moviePoster;
    private String movieThumbNail;
    private String overview;
    private Double rating;
    private String releaseDate;


    private Boolean adult;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movie(int id, String original_title, String overview, String poster_path, Double rating, String release_date) {
        this.moviePoster = poster_path;
        this.overview = overview;
        this.id = id;
        this.title = original_title;
        this.rating = rating;
        this.releaseDate = release_date;
    }

    public Movie() {

    }




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMovieThumbNail() {
        return movieThumbNail;
    }

    public void setMovieThumbNail(String movieThumbNail) {
        this.movieThumbNail = movieThumbNail;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }



    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    Movie(Parcel in){

        id = in.readInt();
        title = in.readString();
        moviePoster = in.readString();
        movieThumbNail = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        rating=in.readDouble();

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(moviePoster);
        parcel.writeString(movieThumbNail);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeDouble(rating);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
