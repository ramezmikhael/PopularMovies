package com.example.ramezreda.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ramezreda.popularmovies.data.FavoritesContract.FavoritesEntry;

/**
 * Created by Ramez on 2/26/2018.
 */

class MoviesDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "movies.db";
    private static final int DB_VERSION = 1;

    public MoviesDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + FavoritesEntry.TABLE_NAME + " (" +
                FavoritesEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                FavoritesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                FavoritesEntry.COLUMN_RELEASE_DATE    + " TEXT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
