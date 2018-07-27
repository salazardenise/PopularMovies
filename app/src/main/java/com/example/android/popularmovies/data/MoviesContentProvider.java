package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MoviesContentProvider extends ContentProvider {

    private MoviesDbHelper mMoviesDbHelper;
    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;
    public static final UriMatcher sUriMatcher = buildUriMatcher();
    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMoviesDbHelper = new MoviesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mMoviesDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch(match) {
            case MOVIES:
                cursor = db.query(MoviesContract.MoviesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MOVIE_WITH_ID:
                //Uri: content://<authority>/movies/#
                // index 0 -> movies portion of path
                // index 1 -> # portion of path
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};
                cursor = db.query(MoviesContract.MoviesEntry.TABLE_NAME, projection, mSelection, mSelectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    // getType() returns mime type (as a String) of the data being accessed
    // MIME type is a standard way to define a data type
    // 2 types of data: 1) single directory, 2) a single row of data
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch(match) {
            case MOVIES:
                return "vnd.android.cursor.dir" + "/" +
                        MoviesContract.AUTHORITY + "/" +
                        MoviesContract.PATH_MOVIES;
            case MOVIE_WITH_ID:
                return "vnd.android.cursor.item" + "/" +
                        MoviesContract.AUTHORITY + "/" +
                        MoviesContract.PATH_MOVIES;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri = null;
        switch(match) {
            // handle different integer matches
            case MOVIES:
                long id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
                if (id > 0) { // success
                    returnUri = ContentUris.withAppendedId(MoviesContract.MoviesEntry.CONTENT_URI, id);
                } else { // fail
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int nRows = 0;
        switch(match) {
            case MOVIES:
                nRows = db.delete(MoviesContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_WITH_ID:
                //Uri: content://<authority>/movies/#
                // index 0 -> movies portion of path
                // index 1 -> # portion of path
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id?";
                String[] mSelectionArgs = new String[]{id};
                nRows = db.delete(MoviesContract.MoviesEntry.TABLE_NAME, mSelection, mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        if (nRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return nRows;
    }

    // update not really needed unless plan to provide app's data to other apps
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        int movieUpdated;
        int match = sUriMatcher.match(uri);
        switch(match) {
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id?";
                String[] mSelectionArgs = new String[]{id};
                movieUpdated = db.update(MoviesContract.MoviesEntry.TABLE_NAME, values, mSelection, mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (movieUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return movieUpdated;
    }
}
