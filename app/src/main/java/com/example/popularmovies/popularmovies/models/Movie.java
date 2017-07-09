package com.example.popularmovies.popularmovies.models;

public class Movie {
    int vote_count;
    int id;
    boolean video;
    double vote_average;
    String title;
    double popularity;
    String poster_path;
    String original_language;
    String original_title;
    double[] genre_ids;
    String backdrop_path;
    boolean adult;
    String overview;
    String release_date;

    public String getPosterPath() {
        String basePath = "https://image.tmdb.org/t/p/w185/";
        return basePath.toString() + poster_path;
    }
}
