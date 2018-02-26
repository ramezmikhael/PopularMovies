package com.example.ramezreda.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.ramezreda.popularmovies.R;

import static com.example.ramezreda.popularmovies.data.FavoritesContract.*;

/**
 * Created by Ramez on 2/26/2018.
 */

public class FavoritesContentProvider extends ContentProvider {

    private static final int FAVORITES = 100;
    private static final int FAVORITE_WITH_ID = 101;

    private MoviesDBHelper mMoviesDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(CONTENT_AUTHORITY, PATH_FAVORITES, FAVORITES);
        matcher.addURI(CONTENT_AUTHORITY, PATH_FAVORITES + "/#", FAVORITE_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mMoviesDbHelper = new MoviesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor cursor;

        switch (match) {
            case FAVORITES:
                cursor = db.query(FavoritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                cursor = db.query(FavoritesEntry.TABLE_NAME,
                        null,
                        FavoritesEntry.COLUMN_ID + "=?",
                        new String[] {id},
                        null,
                        null,
                        null);
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.error_unkown_uri) + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVORITES:
                long id = db.insert(FavoritesEntry.TABLE_NAME, null, contentValues);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(FavoritesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException(getContext().getString(R.string.error_insert_failed_into) + uri);
                }
                break;
                default:
                    throw new UnsupportedOperationException(getContext().getString(R.string.error_unkown_uri) + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int deletedRows = 0;

        switch (match) {
            case FAVORITE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                deletedRows = db.delete(FavoritesEntry.TABLE_NAME,
                        FavoritesEntry.COLUMN_ID + "=?",
                        new String[] {id});
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.error_unkown_uri) + uri);
        }

        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
