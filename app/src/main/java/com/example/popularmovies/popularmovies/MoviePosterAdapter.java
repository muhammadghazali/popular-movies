package com.example.popularmovies.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmovies.popularmovies.data.MovieContract;
import com.example.popularmovies.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MoviePosterAdapterViewHolder> {
    private Context mParentContext;
    private Cursor mCursor;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    final private MoviePosterAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MoviePosterAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    /**
     * Creates a ForecastAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public MoviePosterAdapter(MoviePosterAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public MoviePosterAdapter(MoviePosterAdapterOnClickHandler mClickHandler, Cursor mCursor) {
        this.mCursor = mCursor;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public void onBindViewHolder(MoviePosterAdapterViewHolder holder, int position) {
        int idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry._ID);
        int posterPathIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER);

        mCursor.moveToPosition(position); // get to the right location in the cursor

        final int id = mCursor.getInt(idIndex);
        String posterPath = mCursor.getString(posterPathIndex);

        holder.itemView.setTag(id);

        Picasso
                .with(holder.mMoviePosterImageView.getContext())
                .load(posterPath)
                .into(holder.mMoviePosterImageView);
    }

    @Override
    public MoviePosterAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParentContext = parent.getContext();
        int itemLayoutId = R.layout.movie_poster_item;
        LayoutInflater inflater = LayoutInflater.from(mParentContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(itemLayoutId, parent, shouldAttachToParentImmediately);
        MoviePosterAdapterViewHolder viewHolder = new MoviePosterAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }

        return mCursor.getCount();
    }

    public class MoviePosterAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        @Nullable
        @BindView(R.id.movie_poster_img)
        ImageView mMoviePosterImageView;

        public MoviePosterAdapterViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            int originalTitleIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE);
            int movieIdIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            int posterPathIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER);
            int userRatingIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_USER_RATING);
            int overviewIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
            int releaseDateIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);

            mCursor.moveToPosition(adapterPosition); // get to the right location in the cursor

            String originalTitle = mCursor.getString(originalTitleIndex);
            String posterPath = mCursor.getString(posterPathIndex);
            int movieId = mCursor.getInt(movieIdIndex);
            String userRating = mCursor.getString(userRatingIndex);
            String overview = mCursor.getString(overviewIndex);
            String releaseDate = mCursor.getString(releaseDateIndex);

            Movie movie = new Movie();
            movie.setOriginalTitle(originalTitle);
            movie.setMovieId(movieId);
            movie.setPosterPath(posterPath);
            movie.setOverview(overview);
            movie.setVoteAverage(Double.valueOf(userRating));
            movie.setReleaseDate(releaseDate);

            mClickHandler.onClick(movie);
        }
    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }
}
