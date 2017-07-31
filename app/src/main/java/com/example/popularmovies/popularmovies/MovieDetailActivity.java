package com.example.popularmovies.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codewaves.youtubethumbnailview.ImageLoader;
import com.codewaves.youtubethumbnailview.ThumbnailLoader;
import com.codewaves.youtubethumbnailview.ThumbnailView;
import com.example.popularmovies.popularmovies.data.MovieContract;
import com.example.popularmovies.popularmovies.models.Movie;
import com.example.popularmovies.popularmovies.models.Review;
import com.example.popularmovies.popularmovies.models.Trailer;
import com.example.popularmovies.popularmovies.models.TrailerList;
import com.example.popularmovies.popularmovies.models.UserReviewList;
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
    private static final int MOVIE_USER_REVIEWS_LOADER_ID = 2;
    private Movie mMovie;

    TrailerList mTrailerList = null;
    UserReviewList mUserReviewList = null;

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

    @BindView(R.id.trailers_list_section)
    LinearLayout trailersListSection;

    @BindView(R.id.user_reviews_list_section)
    LinearLayout userReviewsListSection;

    @BindView(R.id.tv_no_user_reviews_yet)
    TextView noUserReviewsTextView;

    private LoaderManager.LoaderCallbacks<TrailerList> trailerListLoaderCallback;
    private LoaderManager.LoaderCallbacks<UserReviewList> userReviewListLoaderCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ThumbnailLoader.initialize(getApplicationContext());

        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                String movieInJson = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);

                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<Movie> jsonAdapter = moshi.adapter(Movie.class);

                try {
                    mMovie = jsonAdapter.fromJson(movieInJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mOriginalTitle.setText(mMovie.getOriginalTitle());

                Picasso
                        .with(this)
                        .load(mMovie.getPosterPath())
                        .into(mMoviePosterImageView);

                mOverview.setText(mMovie.getOverview());
                mAvarageRate.setText(mMovie.getVoteAverage());
                mReleaseDate.setText(mMovie.getReleaseDate());

                int trailersLoaderId = MOVIE_TRAILERS_LOADER_ID;
                int userReviewsLoaderId = MOVIE_USER_REVIEWS_LOADER_ID;

                trailerListLoaderCallback = new TrailerListLoaderCallbacks(mMovie.getId());
                userReviewListLoaderCallback = new UserReviewsListLoaderCallbacks(mMovie.getId());

                Bundle bundleForLoader = null;

                getSupportLoaderManager().initLoader(trailersLoaderId, bundleForLoader, trailerListLoaderCallback);
                getSupportLoaderManager().initLoader(userReviewsLoaderId, bundleForLoader, userReviewListLoaderCallback);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO conditionally display the favorite icon
        // display ic_star if the movie is in the favorite list
        // display ic_star_border if the movie is not in the favorite list
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.details, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, mMovie.getPosterPath());
            contentValues.put(MovieContract.MovieEntry.COLUMN_SYSNOPSIS, mMovie.getOverview());
            contentValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING, mMovie.getVoteAverage());
            contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
            contentValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE, 1);

            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

            if (uri != null) {
                Toast.makeText(getBaseContext(), "Movie added to favorites", Toast.LENGTH_LONG).show();
            }

            return true;
        }
        // TODO handle the unfavorite action
        return super.onOptionsItemSelected(item);
    }

    public void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private class UserReviewsListLoaderCallbacks implements LoaderManager.LoaderCallbacks<UserReviewList> {
        int mMovieId;

        public UserReviewsListLoaderCallbacks(int movieId) {
            mMovieId = movieId;
        }

        @Override
        public Loader<UserReviewList> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<UserReviewList>(MovieDetailActivity.this) {
                @Override
                protected void onStartLoading() {
                    if (mUserReviewList != null) {
                        deliverResult(mUserReviewList);
                    }

//                        TODO display the loading indicator
                    forceLoad();
                }

                @Override
                public UserReviewList loadInBackground() {
                    URL requestUrl = NetworkUtils
                            .buildUserReviewsUrl(getContext().getString(R.string.THE_MOVIE_DB_API_TOKEN), mMovieId);

                    try {
                        String jsonResponse = NetworkUtils
                                .getResponseFromHttpUrl(requestUrl);

                        Moshi moshi = new Moshi.Builder().build();
                        JsonAdapter<UserReviewList> jsonAdapter = moshi.adapter(UserReviewList.class);

                        mUserReviewList = jsonAdapter.fromJson(jsonResponse);

                        return mUserReviewList;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void deliverResult(UserReviewList data) {
                    mUserReviewList = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<UserReviewList> loader, UserReviewList data) {
            for (Review review : data.results) {
                TextView authorTextView = new TextView(MovieDetailActivity.this);
                authorTextView.setText(review.getAuthor());
                authorTextView.setTypeface(null, Typeface.BOLD);

                TextView contentTextView = new TextView(MovieDetailActivity.this);
                contentTextView.setText(review.getContent());

                userReviewsListSection.addView(authorTextView);
                userReviewsListSection.addView(contentTextView);
            }

            if (data.results.size() == 0) {
                noUserReviewsTextView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onLoaderReset(Loader<UserReviewList> loader) {

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
            for (Trailer trailer : mTrailerList.results) {
                ThumbnailView thumbnailView = new ThumbnailView(getApplicationContext());
                thumbnailView.setTag(String.valueOf(trailer.getKey()));

                ThumbnailView.LayoutParams layoutParams = new ThumbnailView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        640);
                thumbnailView.setLayoutParams(layoutParams);

                thumbnailView.loadThumbnail("https://www.youtube.com/watch?v=" + trailer.getKey(), new ImageLoader() {
                    @Override
                    public Bitmap load(String url) throws IOException {
                        return Picasso.with(MovieDetailActivity.this).load(url).get();
                    }
                });
                thumbnailView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        watchYoutubeVideo(view.getTag().toString());
                    }
                });

                trailersListSection.addView(thumbnailView);
            }
        }

        @Override
        public void onLoaderReset(Loader<TrailerList> loader) {

        }
    }
}
