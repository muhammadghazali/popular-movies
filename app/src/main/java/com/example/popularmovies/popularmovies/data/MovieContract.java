package com.example.popularmovies.popularmovies.data;

import android.provider.BaseColumns;

public class MovieContract {
    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_SYSNOPSIS = "sysnopsis";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
    }
}
