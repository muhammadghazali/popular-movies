package com.example.popularmovies.popularmovies;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmovies.popularmovies.models.Movie;
import com.example.popularmovies.popularmovies.models.MovieList;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MoviePosterAdapterViewHolder> {
    private MovieList mMovieList = null;

    private Context mParentContext;

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

    @Override
    public void onBindViewHolder(MoviePosterAdapterViewHolder holder, int position) {
        Movie movie = mMovieList.results.get(position);

        Picasso
                .with(holder.mMoviePosterImageView.getContext())
                .load(movie.getPosterPath())
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
        if (mMovieList == null) {
            return 0;
        }

        return mMovieList.results.size();
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
            Movie movie = mMovieList.results.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }

    /**
     * This method is used to set the weather movie on a {@link MoviePosterAdapter} if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new MoviePosterAdapter to display it.
     *
     * @param movieData The new movie data to be displayed.
     */
    public void setMovieData(MovieList movieData) {
        mMovieList = movieData;
        notifyDataSetChanged();
    }
}
