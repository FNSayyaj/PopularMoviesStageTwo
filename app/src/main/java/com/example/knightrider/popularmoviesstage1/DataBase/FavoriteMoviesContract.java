package com.example.knightrider.popularmoviesstage1.DataBase;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by KNIGHT RIDER on 3/23/2018.
 */

public class FavoriteMoviesContract{

    public static final String AUTHORITY = "com.example.knightrider.popularmoviesstage1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITE_MOVIES = "favoritemovies";

    public static final class FavoriteMoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String TABLE_NAME = "favoritemovies";
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_AVERAGE_RATE = "averageRate";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_OVERVIEW = "overView";
    }
}
