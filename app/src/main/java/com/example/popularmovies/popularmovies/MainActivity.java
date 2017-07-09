package com.example.popularmovies.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.popularmovies.popularmovies.utilities.NetworkUtils;
import com.example.popularmovies.popularmovies.utilities.OpenWeatherJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]> {

    private static final int FORECAST_LOADER_ID = 0;
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

        LoaderManager.LoaderCallbacks<String[]> callback = MainActivity.this;

        Bundle bundleForLoader = null;

        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
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
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id         The ID whose loader is to be created.
     * @param loaderArgs Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<String[]> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<String[]>(this) {

            /* This String array will hold and help cache our weather data */
            String[] mMoviesData = null;

            // COMPLETED (3) Cache the weather data in a member variable and deliver it in onStartLoading.

            /**
             * Subclasses of AsyncTaskLoader must implement this to take care of loading their data.
             */
            @Override
            protected void onStartLoading() {
                if (mMoviesData != null) {
                    deliverResult(mMoviesData);
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
            public String[] loadInBackground() {
                // TODO load the api key from the res/values.xml
                URL requestUrl = NetworkUtils.buildUrl("e9fc82cee13f0ec75672adb52a8ec7e8");

                try {
                    String jsonResponse = NetworkUtils
                            .getResponseFromHttpUrl(requestUrl);

                    String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                            .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);

//                    TODO populate the json response
                    String[] test = {"hello", "world"};

                    return test;
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
            public void deliverResult(String[] data) {
                mMoviesData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mMoviePosterAdapter.setMovieData(data);
        if (null == data) {
            showErrorMessage();
        } else {
            showMovieDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }
}
