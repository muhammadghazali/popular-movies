package com.example.popularmovies.popularmovies.models;

public class Movie {
    int vote_count;
    int id;
    int movieId;
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

    public Movie() {
    }

    public void setVoteCount(int vote_count) {
        this.vote_count = vote_count;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public void setVoteAverage(double vote_average) {
        this.vote_average = vote_average;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public void setPosterPath(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setOriginalLanguage(String original_language) {
        this.original_language = original_language;
    }

    public void setOriginalTitle(String original_title) {
        this.original_title = original_title;
    }

    public void setGenreIds(double[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    public void setBackdropPath(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String release_date) {
        this.release_date = release_date;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getPosterPath() {
        String basePath = "https://image.tmdb.org/t/p/w185/";
        return basePath.toString() + poster_path;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return String.valueOf(vote_average);
    }

    public String getReleaseDate() {
        return release_date;
    }

    public int getId() {
        return id;
    }
}
