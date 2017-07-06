package com.example.popularmovies.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MoviePosterAdapterViewHolder> {
    private String[] mMovieList = {
            "https://image.tmdb.org/t/p/w640/9OjFAb9r21TNJIbbMVAZtbj3I60.jpg",
            "https://image.tmdb.org/t/p/w640/4WinsdHQBdh5aTt7Bd7T7dbUXbb.jpg",
            "https://image.tmdb.org/t/p/w640/jnllnSq8u4d1oQPU7PsoAHD6bLU.jpg",
            "https://image.tmdb.org/t/p/w640/7SSm7BfzFoVzmd6fCDccj7qRxc8.jpg",
            "https://image.tmdb.org/t/p/w640/iTST6DcLhfufWYUKCOskkusaYUq.jpg",
            "https://image.tmdb.org/t/p/w640/9EXnebqbb7dOhONLPV9Tg2oh2KD.jpg",
            "https://image.tmdb.org/t/p/w640/4PiiNGXj1KENTmCBHeN6Mskj2Fq.jpg",
            "https://image.tmdb.org/t/p/w640/hZrkA4aFcCCzatBJ2kqrLaCHd5t.jpg",
            "https://image.tmdb.org/t/p/w640/wHJKSDX1ZZqL5pvewihMgSDTQyf.jpg",
            "https://image.tmdb.org/t/p/w640/7k6nlkYkSs8zCTv0RHwaxehJG3u.jpg",
            "https://image.tmdb.org/t/p/w640/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "https://image.tmdb.org/t/p/w640/f8Ng1Sgb3VLiSwAvrfKeQPzvlfr.jpg"
    };

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
}
