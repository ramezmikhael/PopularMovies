package com.example.ramezreda.popularmovies.data;

import android.net.Uri;

/**
 * Created by Ramez on 2/26/2018.
 */

public class FavoritesContract {
    public static final String CONTENT_AUTHORITY = "com.example.ramezreda.popularmovies";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoritesEntry {
        // Content Uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();
        // table name
        public static final String TABLE_NAME = "favorites";
        // columns
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_RELEASE_DATE = "release_date";
    }
}
