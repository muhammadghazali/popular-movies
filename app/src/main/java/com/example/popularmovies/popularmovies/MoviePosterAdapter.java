package com.example.popularmovies.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MoviePosterAdapterViewHolder> {
    private String[] mMovieList = {};

    private Context mParentContext;

    @Override
    public void onBindViewHolder(MoviePosterAdapterViewHolder holder, int position) {
        Picasso
                .with(holder.mMoviePosterImageView.getContext())
                .load(mMovieList[position])
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

        return mMovieList.length;
    }

    public class MoviePosterAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mMoviePosterImageView;

        public MoviePosterAdapterViewHolder(View itemView) {
            super(itemView);

            mMoviePosterImageView = (ImageView) itemView.findViewById(R.id.movie_poster_img);
        }
    }

    /**
     * This method is used to set the weather forecast on a ForecastAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new ForecastAdapter to display it.
     *
     * @param movieData The new weather data to be displayed.
     */
    public void setMovieData(String[] movieData) {
        mMovieList = movieData;
        notifyDataSetChanged();
    }
}
