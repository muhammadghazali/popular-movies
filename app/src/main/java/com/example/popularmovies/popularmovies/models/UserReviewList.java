package com.example.popularmovies.popularmovies.models;

import java.util.ArrayList;
import java.util.List;

public class UserReviewList {
    int id;
    int page;
    int total_pages;
    int total_results;

    public final List<Review> results = new ArrayList<Review>();

    public int getId() {
        return id;
    }

    public int getPage() {
        return page;
    }


    public int getTotalPages() {
        return total_pages;
    }

    public int getTotalResults() {
        return total_results;
    }


    public List<Review> getResults() {
        return results;
    }
}
