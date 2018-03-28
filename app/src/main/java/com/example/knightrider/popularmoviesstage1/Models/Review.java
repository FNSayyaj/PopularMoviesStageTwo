package com.example.knightrider.popularmoviesstage1.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KNIGHT RIDER on 3/18/2018.
 */

public class Review implements Parcelable {

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    private String author;
    private String content;


    public Review(String author, String content) {

        this.author = author;
        this.content = content;
    }

    public String getAuthor(){return author;}
    public void setAuthor(String author){this.author = author;}

    public String getContent() {
        return content;
    }
    public void setContent(String content){this.content = content;}

    private Review(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
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