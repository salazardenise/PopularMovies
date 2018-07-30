package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.adapter.ReviewAdapter;
import com.example.android.popularmovies.adapter.VideoAdapter;
import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity implements VideoAdapter.VideoAdapterOnClickHandler {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String MOVIE_INFO = "MovieInfo"; // key
    private MovieInfo mSelectedMovie;
    private ImageView mBackdropImage;
    private ImageView mMovieImage;
    private TextView mTitleText;
    private TextView mReleaseDateText;
    private TextView mUserRatingText;
    private TextView mPlotSynopsisText;

    private ReviewAdapter mReviewAdapter;
    private RecyclerView mReviewsList;
    private TextView noReviews;

    private VideoAdapter mVideoAdapter;
    private RecyclerView mVideosList;
    private TextView noVideos;

    private Video firstVideo;

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
            mSelectedMovie = getIntent().getParcelableExtra(MOVIE_INFO); // Parcelable
            setUp();
        }

        // SET UP REVIEWS
        noReviews = (TextView) findViewById(R.id.tv_no_reviews);
        noReviews.setVisibility(View.INVISIBLE);
        // set up recycler view
        mReviewsList = (RecyclerView) findViewById(R.id.rv_reviews);
        // set up linear layout manager
        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this);
        mReviewsList.setLayoutManager(reviewsLayoutManager);
        // set up adapter
        mReviewAdapter = new ReviewAdapter();
        mReviewsList.setAdapter(mReviewAdapter);
        /* Once all of our views are setup, we can load the reviews data. */
        loadReviews(mSelectedMovie.getId());


        // SET UP VIDEOS
        noVideos = (TextView) findViewById(R.id.tv_no_videos);
        noVideos.setVisibility(View.INVISIBLE);
        // set up recycler view
        mVideosList = (RecyclerView) findViewById(R.id.rv_videos);
        // set up linear layout manager
        LinearLayoutManager videosLayoutManager = new LinearLayoutManager(this);
        mVideosList.setLayoutManager(videosLayoutManager);
        // set up adapter
        mVideoAdapter = new VideoAdapter(this);
        mVideosList.setAdapter(mVideoAdapter);
        /* Once all of our views are setup, we can load the reviews data. */
        loadVideos(mSelectedMovie.getId());
    }

    private void setUp() {
        setTitle(mSelectedMovie.getTitle());
        Context context = MovieDetailActivity.this;
        Picasso.with(context).load(mSelectedMovie.getBackdropPath()).into(mBackdropImage);
        Picasso.with(context).load(mSelectedMovie.getPosterPath()).into(mMovieImage);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mBackdropImage.setVisibility(View.VISIBLE); // Portrait Mode
        } else {
            mBackdropImage.setVisibility(View.GONE); // Landscape Mode
        }
        mTitleText.setText(mSelectedMovie.getTitle());
        mReleaseDateText.setText(mSelectedMovie.getReleaseDate());
        String userRating = String.valueOf(mSelectedMovie.getUserRating()) + "/10";
        mUserRatingText.setText(userRating);
        mPlotSynopsisText.setText(mSelectedMovie.getPlotSynopsis());
    }

    private void loadReviews(int id) {
        new FetchReviews().execute(id);
    }

    private void loadVideos(int id) { new FetchVideos().execute(id); }

    @Override
    public void onListItemClick(Video video) {
        Context context = MovieDetailActivity.this;
        Uri webpage = Uri.parse(video.getLink());
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public class FetchReviews extends AsyncTask<Integer, Void, Review[]> {

        @Override
        protected Review[] doInBackground(Integer... integers) {
            /* If there's no option, there's nothing to look up. */
            if (integers.length == 0) {
                return null;
            }
            int id = integers[0];
            URL reviewsRequestUrl = NetworkUtils.buildReviewsURL(id);

            try {
                String jsonReviewsResponse = NetworkUtils
                        .getResponseFromHttpUrl(reviewsRequestUrl);

                return MovieJsonUtils
                        .getReviewsFromJson(jsonReviewsResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Review[] reviews) {
            if (reviews == null || reviews.length == 0) {
                noReviews.setVisibility(View.VISIBLE);
                mReviewsList.setVisibility(View.GONE);
            } else {
                mReviewAdapter.setReviewData(reviews);
                noReviews.setVisibility(View.GONE);
                mReviewsList.setVisibility(View.VISIBLE);
            }
        }
    }

    public class FetchVideos extends AsyncTask<Integer, Void, Video[]> {

        @Override
        protected Video[] doInBackground(Integer... integers) {
             /* If there's no option, there's nothing to look up. */
            if (integers.length == 0) {
                return null;
            }
            int id = integers[0];
            URL trailersRequestUrl = NetworkUtils.buildTrailersURL(id);

            try {
                String jsonTrailersResponse = NetworkUtils
                        .getResponseFromHttpUrl(trailersRequestUrl);

                return MovieJsonUtils
                        .getVideosFromJson(jsonTrailersResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Video[] videos) {
            if (videos == null || videos.length == 0) {
                noVideos.setVisibility(View.VISIBLE);
                mVideosList.setVisibility(View.GONE);
            } else {
                mVideoAdapter.setReviewData(videos);
                noVideos.setVisibility(View.GONE);
                mVideosList.setVisibility(View.VISIBLE);
                firstVideo = videos[0];
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        if (menuItemThatWasSelected == R.id.detail_option_share) {
            shareTrailer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareTrailer() {
        String mimeType ="text/plain";
        String title = "Sharing an Awesome Movie";
        if (firstVideo != null) {
            // share name of movie and first trailer
            ShareCompat.IntentBuilder
                    .from(this)
                    .setType(mimeType)
                    .setChooserTitle(title)
                    .setText(firstVideo.getLink())
                    .startChooser();
        } else {
            // share name of movie only
            String textToShare = mSelectedMovie.getTitle() + " is an awesome movie. Check it out!";
            ShareCompat.IntentBuilder
                    .from(this)
                    .setType(mimeType)
                    .setChooserTitle(title)
                    .setText(textToShare)
                    .startChooser();
        }
    }

    private boolean isFavorite() {
        if (mSelectedMovie == null) return false;
        Uri itemUri = MoviesContract.MoviesEntry.CONTENT_URI;
        String mSelection = "movie_id=?";
        int movieId = mSelectedMovie.getId();
        String movieIdStr = String.valueOf(movieId);
        String[] mSelectionArgs = new String[]{movieIdStr};

        Cursor cursor = getContentResolver().query(itemUri, null, mSelection, mSelectionArgs, null);
        boolean isNull = (cursor != null && cursor.getCount() > 0);
        cursor.close();
        return isNull;
    }

    public void onClickAddFavorite(View view) {
        if (mSelectedMovie == null) return;
        if (isFavorite()) {
            // unfavorite it
            Uri itemUri = MoviesContract.MoviesEntry.CONTENT_URI;
            String mSelection = MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?";
            String movieIdStr = String.valueOf(mSelectedMovie.getId());
            String[] mSelectionArgs = new String[]{movieIdStr};
            int deleted = getContentResolver().delete(itemUri, mSelection, mSelectionArgs);
        } else {
            // favorite it
            ContentValues cv = new ContentValues();
            cv.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, mSelectedMovie.getId());
            cv.put(MoviesContract.MoviesEntry.COLUMN_TITLE, mSelectedMovie.getTitle());
            cv.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, mSelectedMovie.getPosterPath());
            cv.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH, mSelectedMovie.getBackdropPath());
            cv.put(MoviesContract.MoviesEntry.COLUMN_PLOT_SYNOPSIS, mSelectedMovie.getPlotSynopsis());
            cv.put(MoviesContract.MoviesEntry.COLUMN_USER_RATING, mSelectedMovie.getUserRating());
            cv.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, mSelectedMovie.getReleaseDate());
            Uri insertUri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, cv);
        }
        finish(); // this tells us this Activity is over and to return to MainActivity
    }
}
