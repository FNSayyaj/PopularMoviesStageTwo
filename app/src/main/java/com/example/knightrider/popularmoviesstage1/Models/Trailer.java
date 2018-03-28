package com.example.knightrider.popularmoviesstage1.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KNIGHT RIDER on 3/18/2018.
 */

public class Trailer implements Parcelable {

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    private String name;
    private String key;

    public Trailer(String name, String key) {

        this.name = name;
        this.key = key;
    }

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getKey() {
        return key;
    }
    public void setKey(String key){this.key = key;}

    private Trailer(Parcel in) {
        this.name = in.readString();
        this.key = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(key);
    }
}

