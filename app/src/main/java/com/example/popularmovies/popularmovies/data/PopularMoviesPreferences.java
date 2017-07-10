package com.example.popularmovies.popularmovies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.example.popularmovies.popularmovies.R;

public class PopularMoviesPreferences {
    /**
     * Returns true if the user has selected metric temperature display.
     *
     * @param context Context used to get the SharedPreferences
     * @return true If metric display should be used
     */
    public static String getSortOption(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForSort = context.getString(R.string.pref_sort_key);
        String defaultSortOption = context.getString(R.string.pref_sort_popular);
        String preferredSortOption = prefs.getString(keyForSort, defaultSortOption);
//        String sortOptions = context.getString(R.string.pref_sort_key);

        return preferredSortOption;
    }
}
