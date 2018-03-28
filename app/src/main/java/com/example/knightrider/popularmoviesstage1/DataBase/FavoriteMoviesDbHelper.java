package com.example.knightrider.popularmoviesstage1.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by KNIGHT RIDER on 3/23/2018.
 */

public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "favoritemovies.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 6;

    // Constructor
    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a table to hold waitlist data
        final String SQL_CREATE_FAVORITEMOVIES_TABLE =
                " CREATE TABLE " +
                        FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME + "(" +
                        FavoriteMoviesContract.FavoriteMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL UNIQUE, " +
                        FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                        FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_AVERAGE_RATE + " DOUBLE NOT NULL, " +
                        FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                        FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL" +
                        " );";


        db.execSQL(SQL_CREATE_FAVORITEMOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME);
        onCreate(db);

    }
}
