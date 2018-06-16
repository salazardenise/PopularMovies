package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String MOVIE_INFO = "MovieInfo"; // key
    private MovieInfo mSelectedMovie;
    private ImageView mBackdropImage;
    private ImageView mMovieImage;
    private TextView mTitleText;
    private TextView mReleaseDateText;
    private TextView mUserRatingText;
    private TextView mPlotSynopsisText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mBackdropImage = (ImageView) findViewById(R.id.iv_movie_backdrop);
        mMovieImage = (ImageView) findViewById(R.id.iv_movie_detail);
        mTitleText = (TextView) findViewById(R.id.tv_title);
        mReleaseDateText = (TextView) findViewById(R.id.tv_release_date);
        mUserRatingText = (TextView) findViewById(R.id.tv_user_rating);
        mPlotSynopsisText = (TextView) findViewById(R.id.tv_plot_synopsis);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(MOVIE_INFO)) {
            Log.i(TAG, "Detail Acitivty has Extra");
            mSelectedMovie = getIntent().getParcelableExtra(MOVIE_INFO); // Parcelable
            setUp();
        }
    }

    private void setUp() {
        Context context = MovieDetailActivity.this;
        Picasso.with(context).load(mSelectedMovie.getBackdropPath()).into(mBackdropImage);
        Picasso.with(context).load(mSelectedMovie.getPosterPath()).into(mMovieImage);
        mTitleText.setText(mSelectedMovie.getTitle());
        mReleaseDateText.setText(mSelectedMovie.getReleaseDate());
        String userRating = String.valueOf(mSelectedMovie.getUserRating()) + "/10";
        mUserRatingText.setText(userRating);
        mPlotSynopsisText.setText(mSelectedMovie.getPlotSynopsis());
    }
}
