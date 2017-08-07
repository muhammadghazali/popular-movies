package com.example.popularmovies.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movieDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 9;


    // Constructor
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL UNIQUE, " +
                MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " STRING NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " STRING NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + "  TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_SYNOPSIS + "  TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_FAVORITE + "  INTEGER NOT NULL DEFAULT 0, " +
                MovieContract.MovieEntry.COLUMN_POPULAR + "  INTEGER NOT NULL DEFAULT 0, " +
                MovieContract.MovieEntry.COLUMN_TOP_RATED + "  INTEGER NOT NULL DEFAULT 0, " +
                MovieContract.MovieEntry.COLUMN_USER_RATING + " STRING NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }


    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}