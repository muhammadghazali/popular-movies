package com.example.popularmovies.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.popularmovies.models.Movie;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MovieDetailActivity extends AppCompatActivity {

    private String mForecast;
    private TextView mOriginalTitle;
    private TextView mOverview;
    private TextView mAvarageRate;
    private TextView mReleaseDate;
    private ImageView mMoviePosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mOriginalTitle = (TextView) findViewById(R.id.tv_original_title);
        mMoviePosterImageView = (ImageView) findViewById(R.id.movie_poster_img);
        mOverview = (TextView) findViewById(R.id.tv_overview);
        mAvarageRate = (TextView) findViewById(R.id.tv_avarage_rate);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mForecast = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);

                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<Movie> jsonAdapter = moshi.adapter(Movie.class);
                Movie movie = null;

                try {
                    movie = jsonAdapter.fromJson(mForecast);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mOriginalTitle.setText(movie.getOriginalTitle());

                Picasso
                        .with(this)
                        .load(movie.getPosterPath())
                        .into(mMoviePosterImageView);

                mOverview.setText(movie.getOverview());
                mAvarageRate.setText(movie.getVoteAverage());
                mReleaseDate.setText(movie.getReleaseDate());
            }
        }
    }
}
