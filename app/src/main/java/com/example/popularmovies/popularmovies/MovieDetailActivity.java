package com.example.popularmovies.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codewaves.youtubethumbnailview.ImageLoader;
import com.codewaves.youtubethumbnailview.ThumbnailLoader;
import com.codewaves.youtubethumbnailview.ThumbnailView;
import com.example.popularmovies.popularmovies.models.Movie;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    private String mForecast;

    @BindView(R.id.tv_original_title)
    TextView mOriginalTitle;

    @BindView(R.id.tv_overview)
    TextView mOverview;

    @BindView(R.id.tv_avarage_rate)
    TextView mAvarageRate;

    @BindView(R.id.tv_release_date)
    TextView mReleaseDate;

    @BindView(R.id.movie_poster_img)
    ImageView mMoviePosterImageView;

    @BindView(R.id.trailer_1)
    ThumbnailView mTrailer1;

    @BindView(R.id.trailer_2)
    ThumbnailView mTrailer2;

    @BindView(R.id.trailer_3)
    ThumbnailView mTrailer3;

    @BindView(R.id.trailers_list_section)
    LinearLayout trailersListSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ThumbnailLoader.initialize(getApplicationContext());

        ButterKnife.bind(this);

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

                mTrailer1.loadThumbnail("https://www.youtube.com/watch?v=iCkYw3cRwLo", new ImageLoader() {
                    @Override
                    public Bitmap load(String url) throws IOException {
                        return Picasso.with(MovieDetailActivity.this).load(url).get();
                    }
                });

                mTrailer1.loadThumbnail("https://www.youtube.com/watch?v=Ow78zp30ioE", new ImageLoader() {
                    @Override
                    public Bitmap load(String url) throws IOException {
                        return Picasso.with(MovieDetailActivity.this).load(url).get();
                    }
                });

                mTrailer2.loadThumbnail("https://www.youtube.com/watch?v=Sq8vjBg7EWE", new ImageLoader() {
                    @Override
                    public Bitmap load(String url) throws IOException {
                        return Picasso.with(MovieDetailActivity.this).load(url).get();
                    }
                });

                mTrailer3.loadThumbnail("https://www.youtube.com/watch?v=MKyp9Tx-NPY", new ImageLoader() {
                    @Override
                    public Bitmap load(String url) throws IOException {
                        return Picasso.with(MovieDetailActivity.this).load(url).get();
                    }
                });

            }
        }
    }
}
