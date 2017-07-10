package com.example.popularmovies.popularmovies.models;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.util.Date;

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

        final DateTimeFormatter displayFormat = DateTimeFormat.shortDate();
        final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        final LocalDate dt = dtf.parseLocalDate(release_date);

        return dt.toString(displayFormat);
    }
}
