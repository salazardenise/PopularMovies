package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.data.MoviesContract.MoviesEntry;

public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    public static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE =
                "CREATE TABLE " + MoviesEntry.TABLE_NAME + "(" +
                        MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MoviesEntry.COLUMN_MOVIE_ID + " INTEGER NON NULL, " +
                        MoviesEntry.COLUMN_TITLE + " TEXT NON NULL, " +
                        MoviesEntry.COLUMN_POSTER_PATH + " TEXT NON NULL, " +
                        MoviesEntry.COLUMN_BACKDROP_PATH + " TEXT NON NULL, " +
                        MoviesEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NON NULL, " +
                        MoviesEntry.COLUMN_USER_RATING + " INTEGER NON NULL, " +
                        MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NON NULL)";
        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DROP_TABLE =
                "DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME;
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }
}
