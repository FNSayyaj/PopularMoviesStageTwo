package com.example.knightrider.popularmoviesstage1.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KNIGHT RIDER on 3/10/2018.
 *
 * postponed Parcelable to the next project
 */

public class Movie implements Parcelable{

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

    private String id;
    private String title;
    private String release_date;
    private double vote_average;
    private String poster_path;
    private String overView;

    public Movie(String id, String title, String release_date, double vote_average, String poster_path, String overview) {

        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
        this.overView = overview;
    }


    public String getId(){return id;}
    public void setId(String id){this.id = id;}

    public String getTitle() {
        return title;
    }
    public void setTitle(String title){this.title = title;}

    public String getReleaseDate() {
        return release_date;
    }
    public void setReleaseDate(String release_date){this.release_date = release_date;}

    public String getPosterPath() {
        return poster_path;
    }
    public void setPosterPath(String poster_path){this.poster_path = poster_path;}

    public double getVote() {
        return vote_average;
    }
    public void setVote(double vote_average){this.vote_average = vote_average;}

    public String getOverview() {
        return overView;
    }
    public void setOverView(String overView){this.overView = overView;}

    private Movie(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.release_date = in.readString();
        this.vote_average = in.readDouble();
        this.poster_path = in.readString();
        this.overView = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(release_date);
        dest.writeDouble(vote_average);
        dest.writeString(poster_path);
        dest.writeString(overView);
    }
}
