package com.example.popularmovies.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codewaves.youtubethumbnailview.ImageLoader;
import com.codewaves.youtubethumbnailview.ThumbnailLoader;
import com.codewaves.youtubethumbnailview.ThumbnailView;
import com.example.popularmovies.popularmovies.models.Movie;
import com.example.popularmovies.popularmovies.models.TrailerList;
import com.example.popularmovies.popularmovies.utilities.NetworkUtils;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    private static final int MOVIE_TRAILERS_LOADER_ID = 1;
    private String mForecast;

    TrailerList mTrailerList = null;

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

    private LoaderManager.LoaderCallbacks<TrailerList> trailerListLoaderCallback;

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

                int loaderId = MOVIE_TRAILERS_LOADER_ID;

                trailerListLoaderCallback = new TrailerListLoaderCallbacks(movie.getId());

                Bundle bundleForLoader = null;

                getSupportLoaderManager().initLoader(loaderId, bundleForLoader, trailerListLoaderCallback);

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

    private class TrailerListLoaderCallbacks implements LoaderManager.LoaderCallbacks<TrailerList> {
        private int mMovieId;

        public TrailerListLoaderCallbacks(int movieId) {
            mMovieId = movieId;
        }

        @Override
        public Loader<TrailerList> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<TrailerList>(MovieDetailActivity.this) {


                @Override
                protected void onStartLoading() {
                    if (mTrailerList != null) {
                        deliverResult(mTrailerList);
                    }

//                        TODO display the loading indicator
                    forceLoad();
                }

                @Override
                public TrailerList loadInBackground() {
                    URL requestUrl = NetworkUtils
                            .buildTrailersUrl(getContext().getString(R.string.THE_MOVIE_DB_API_TOKEN), mMovieId);

                    try {
                        String jsonResponse = NetworkUtils
                                .getResponseFromHttpUrl(requestUrl);

                        Moshi moshi = new Moshi.Builder().build();
                        JsonAdapter<TrailerList> jsonAdapter = moshi.adapter(TrailerList.class);

                        mTrailerList = jsonAdapter.fromJson(jsonResponse);

                        return mTrailerList;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void deliverResult(TrailerList data) {
                    mTrailerList = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<TrailerList> loader, TrailerList data) {
//                TODO hide the progress indicator
//                TODO list the trailers on the view
        }

        @Override
        public void onLoaderReset(Loader<TrailerList> loader) {

        }
    }
}
