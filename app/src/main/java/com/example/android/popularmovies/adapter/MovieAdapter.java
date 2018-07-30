package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.MovieInfo;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private static final boolean shouldAttachToParentImmediately = false;
    private MovieInfo[] mMovieInfoData;
    public interface MovieAdapterOnClickHandler {
        void onListItemClick(MovieInfo movieInfo);
    }
    private MovieAdapterOnClickHandler mClickHandler;

    // data is passed into the constructor
    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }

    @Override
    public MovieAdapter.MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieAdapterViewHolder holder, int position) {
        MovieInfo movieInfo = mMovieInfoData[position];
        String imageUrl = movieInfo.getPosterPath();
        Picasso.with(holder.mImageView.getContext()).load(imageUrl).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovieInfoData == null) return 0;
        return mMovieInfoData.length;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_movie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onListItemClick(onItemClick(v, getAdapterPosition()));
        }

        // Convenience methods for getting data at click position
        public String getItemTitle(int id) {
            return mMovieInfoData[id].getTitle();
        }
        public MovieInfo getItem(int id) {
            return mMovieInfoData[id];
        }

        // Method that executes your code for the action received
        public MovieInfo onItemClick(View view, int position) {
            //String movieInfoToastTest = "You clicked movie " + getItemTitle(position) + ", which is at cell position " + position;
            return getItem(position);
        }

    }

    /**
     * This method is used to set the movie info on a MovieAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new ForecastAdapter to display it.
     *
     * @param movieInfoData The new movie info data to be displayed.
     */
    public void setMovieData(MovieInfo[] movieInfoData) {
        mMovieInfoData = movieInfoData;
        notifyDataSetChanged();
    }

    /**
     * When data changes and a re-query occurs, this function sets mMovieInfoData
     * This is only called for the favorites view
     */
    public void setMovieDataFromCursor(Cursor cursor) {
        if(null != cursor) {
            int count = cursor.getCount();

            if (count > 0) {
                // Indices
                int idIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry._ID);
                int movieIdIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID);
                int titleIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE);
                int posterPathIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH);
                int backdropPathIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH);
                int plotSynopsisIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_PLOT_SYNOPSIS);
                int userRatingIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_USER_RATING);
                int releaseDateIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE);

                mMovieInfoData = new MovieInfo[count];
                int i = 0;
                while (cursor.moveToNext()) {
                    // Determine the values of the wanted data
                    int id = cursor.getInt(idIndex);
                    String movieIdStr = cursor.getString(movieIdIndex);
                    int movieId = Integer.parseInt(movieIdStr);
                    String title = cursor.getString(titleIndex);
                    String posterPath = cursor.getString(posterPathIndex);
                    String backdropPath = cursor.getString(backdropPathIndex);
                    String plotSynopsis = cursor.getString(plotSynopsisIndex);
                    String userRatingStr = cursor.getString(userRatingIndex);
                    int userRating = Integer.parseInt(userRatingStr);
                    String releaseDate = cursor.getString(releaseDateIndex);

                    //Set values
                    MovieInfo movieInfo = new MovieInfo(movieId, title, posterPath, backdropPath, plotSynopsis, userRating, releaseDate);
                    mMovieInfoData[i] = movieInfo;
                    i++;
                }
                notifyDataSetChanged();
            }
            cursor.close();
        }
    }
}
