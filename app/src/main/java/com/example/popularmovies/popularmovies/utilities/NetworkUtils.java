package com.example.popularmovies.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String POPULAR_MOVIE_URL =
            "https://api.themoviedb.org/3/movie/popular";

    private static final String TOP_RATED_MOVIE_URL =
            "https://api.themoviedb.org/3/movie/top_rated";

    private static final String BASE_URL =
            "https://api.themoviedb.org/3";


    final static String API_KEY_PARAM = "api_key";

    /**
     * Builds the URL used to talk to the moviedb API.
     *
     * @param apiKey The location that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String apiKey, String sortOptions) {
        String apiUrl = (sortOptions.equals("popular")) ?
                POPULAR_MOVIE_URL : TOP_RATED_MOVIE_URL;

        Uri builtUri = Uri.parse(apiUrl).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Build the URL that match the following form movie/{movie_id}/videos
     *
     * @param apiKey
     * @return
     */
    public static URL buildTrailersUrl(String apiKey, int movieId) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath("movie")
                .appendPath(String.valueOf(movieId))
                .appendPath("videos")
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Build the URL that match the following form movie/{movie_id}/reviews
     *
     * @param apiKey
     * @return
     */
    public static URL buildUserReviewsUrl(String apiKey, int movieId) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath("movie")
                .appendPath(String.valueOf(movieId))
                .appendPath("reviews")
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}