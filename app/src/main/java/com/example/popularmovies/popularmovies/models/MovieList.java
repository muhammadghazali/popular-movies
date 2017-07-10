package com.example.popularmovies.popularmovies.models;

import java.util.ArrayList;
import java.util.List;

public class MovieList {
    int page;
    int total_results;
    int total_pages;

    public final List<Movie> results = new ArrayList<Movie>();
}
