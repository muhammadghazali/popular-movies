package com.example.popularmovies.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.popularmovies.popularmovies.models.MovieList;
import com.example.popularmovies.popularmovies.utilities.NetworkUtils;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieList>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int FORECAST_LOADER_ID = 0;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mMoviesRecyclerView;
    private MoviePosterAdapter mMoviePosterAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);

        mMoviePosterAdapter = new MoviePosterAdapter();
        mMoviesRecyclerView.setAdapter(mMoviePosterAdapter);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        int loaderId = FORECAST_LOADER_ID;

        LoaderManager.LoaderCallbacks<MovieList> callback = MainActivity.this;

        Bundle bundleForLoader = null;

        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);

        /*
         * Register MainActivity as an OnPreferenceChangedListener to receive a callback when a
         * SharedPreference has changed. Please note that we must unregister MainActivity as an
         * OnSharedPreferenceChanged listener in onDestroy to avoid any memory leaks.
         */
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    public void goToMovieDetailActivity(View view) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        startActivity(intent);
    }

    private void showErrorMessage() {
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * OnStart is called when the Activity is coming into view. This happens when the Activity is
     * first created, but also happens when the Activity is returned to from another Activity. We
     * are going to use the fact that onStart is called when the user returns to this Activity to
     * check if the location setting or the preferred units setting has changed. If it has changed,
     * we are going to perform a new query.
     */
    @Override
    protected void onStart() {
        super.onStart();

        /*
         * If the preferences for location or units have changed since the user was last in
         * MainActivity, perform another query and set the flag to false.
         *
         * This isn't the ideal solution because there really isn't a need to perform another
         * GET request just to change the units, but this is the simplest solution that gets the
         * job done for now. Later in this course, we are going to show you more elegant ways to
         * handle converting the units from celsius to fahrenheit and back without hitting the
         * network again by keeping a copy of the data in a manageable format.
         */
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            Log.d(TAG, "onStart: preferences were updated");
            getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /* Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks. */
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id         The ID whose loader is to be created.
     * @param loaderArgs Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<MovieList> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<MovieList>(this) {

            /* This String array will hold and help cache our weather data */
            MovieList movieList = null;

            // COMPLETED (3) Cache the weather data in a member variable and deliver it in onStartLoading.

            /**
             * Subclasses of AsyncTaskLoader must implement this to take care of loading their data.
             */
            @Override
            protected void onStartLoading() {
                if (movieList != null) {
                    deliverResult(movieList);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            /**
             * This is the method of the AsyncTaskLoader that will load and parse the JSON data
             * from OpenWeatherMap in the background.
             *
             * @return Weather data from OpenWeatherMap as an array of Strings.
             *         null if an error occurs
             */
            @Override
            public MovieList loadInBackground() {
                URL requestUrl = NetworkUtils
                        .buildUrl(getContext().getString(R.string.THE_MOVIE_DB_API_TOKEN));

                try {
                    String jsonResponse = NetworkUtils
                            .getResponseFromHttpUrl(requestUrl);

                    Moshi moshi = new Moshi.Builder().build();
                    JsonAdapter<MovieList> jsonAdapter = moshi.adapter(MovieList.class);

                    movieList = jsonAdapter.fromJson(jsonResponse);

                    return movieList;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            /**
             * Sends the result of the load to the registered listener.
             *
             * @param data The result of the load
             */
            public void deliverResult(MovieList data) {
                movieList = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieList> loader, MovieList data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mMoviePosterAdapter.setMovieData(data);
        if (null == data) {
            showErrorMessage();
        } else {
            showMovieDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieList> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        /*
         * Set this flag to true so that when control returns to MainActivity, it can refresh the
         * data.
         *
         * This isn't the ideal solution because there really isn't a need to perform another
         * GET request just to change the units, but this is the simplest solution that gets the
         * job done for now. Later in this course, we are going to show you more elegant ways to
         * handle converting the units from celsius to fahrenheit and back without hitting the
         * network again by keeping a copy of the data in a manageable format.
         */
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }
}
