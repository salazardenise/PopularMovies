package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CURRENT_OPTION_KEY = "option_key";
    private MovieAdapter mMovieAdapter;
    private RecyclerView mMoviesGrid;
    private static final String option_popular = "popular";
    private static final String option_top_rated = "top_rated";
    private static final String option_favorites = "favorites";
    private static final int PORTRAIT_NUM_COLS = 3;
    private static final int LANDSCAPE_NUM_COLS = 4;
    private String current_option;
    private ProgressBar loadingIndicator;
    private TextView errorMessage;
    private TextView noFavoritesMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up recycler view
        mMoviesGrid = (RecyclerView) findViewById(R.id.rv_movies);

        // set up grid layout manager
        int numOfColumns;
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            numOfColumns = PORTRAIT_NUM_COLS; // Portrait Mode
        } else {
            numOfColumns = LANDSCAPE_NUM_COLS; // Landscape Mode
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numOfColumns);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMoviesGrid.setLayoutManager(gridLayoutManager);

        // set up adapter
        mMovieAdapter = new MovieAdapter(this);
        mMoviesGrid.setAdapter(mMovieAdapter);

        // loading indicator and error message
        loadingIndicator = findViewById(R.id.loading_indicator);
        errorMessage = findViewById(R.id.error_message);
        noFavoritesMessage = findViewById(R.id.no_favorites_message);

        /* Once all of our views are setup, we can load the movie data. */
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CURRENT_OPTION_KEY)) {
                current_option = savedInstanceState.getString(CURRENT_OPTION_KEY);
            }
        } else {
            current_option = option_popular;
        }
        loadMoviesAndSetTitle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMoviesAndSetTitle();
    }

    private void loadMoviesAndSetTitle() {
        if (current_option.equals(option_favorites)) {
            loadFavorites();
            setTitle(R.string.option_favorites);
        } else {
            if (current_option.equals(option_popular)) setTitle(R.string.option_popular);
            else setTitle(R.string.option_top_rated);
            loadMovieData(current_option);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_OPTION_KEY, current_option);
    }

    @Override
    public void onListItemClick(MovieInfo movieInfo) {
        //Toast.makeText(this, "You clicked movie " + movieInfo.getTitle(), Toast.LENGTH_LONG).show();
        // create and start MovieDetail activity
        Context context = MainActivity.this;
        Class destinationActivity = MovieDetailActivity.class;
        Intent startMovieDetailActivityIntent = new Intent(context, destinationActivity);
        startMovieDetailActivityIntent.putExtra(MovieDetailActivity.MOVIE_INFO, movieInfo);
        startActivity(startMovieDetailActivityIntent);
    }

    private void loadMovieData(String option) {
        mMoviesGrid.setVisibility(View.INVISIBLE);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {
            errorMessage.setVisibility(View.INVISIBLE);
            loadingIndicator.setVisibility(View.VISIBLE);
            noFavoritesMessage.setVisibility(View.INVISIBLE);
            new FetchMovieData().execute(option);
        } else {
            errorMessage.setVisibility(View.VISIBLE);
            loadingIndicator.setVisibility(View.INVISIBLE);
            noFavoritesMessage.setVisibility(View.INVISIBLE);
        }
    }

    public class FetchMovieData extends AsyncTask<String, Void, MovieInfo[]> {

        @Override
        protected MovieInfo[] doInBackground(String... params) {

            /* If there's no option, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }
            String option = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(option);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);

                return MovieJsonUtils
                        .getMoviesFromJson(jsonMovieResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieInfo[] movieInfoData) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            noFavoritesMessage.setVisibility(View.INVISIBLE);
            if (movieInfoData != null) {
                errorMessage.setVisibility(View.INVISIBLE);
                mMovieAdapter.setMovieData(movieInfoData);
                mMoviesGrid.setVisibility(View.VISIBLE);
            } else {
                errorMessage.setVisibility(View.VISIBLE);
                mMoviesGrid.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int menuItemThatWasSelected = menuItem.getItemId();
        if (menuItemThatWasSelected == R.id.option_popular) {
            //Log.i(TAG, "Most Popular selected");
            current_option = option_popular;
            loadMovieData(current_option);
            setTitle(R.string.option_popular);
            return true;
        } else if (menuItemThatWasSelected == R.id.option_top_rated) {
            //Log.i(TAG, "Top Rated selected");
            current_option = option_top_rated;
            loadMovieData(current_option);
            setTitle(R.string.option_top_rated);
            return true;
        } else if (menuItemThatWasSelected == R.id.option_favorites) {
            //Log.i(TAG, "Favorites selected");
            current_option = option_favorites;
            loadFavorites();
            setTitle(R.string.option_favorites);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void loadFavorites() {
        loadingIndicator.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
        mMoviesGrid.setVisibility(View.INVISIBLE);
        noFavoritesMessage.setVisibility(View.INVISIBLE);
        new FetchFavoritesMovieData().execute();
    }

    public class FetchFavoritesMovieData extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            try {
                return getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI, null, null, null, null);
            } catch (Exception e) {
                Log.e(TAG, getResources().getString(R.string.failed_to_load_favorites));
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            errorMessage.setVisibility(View.INVISIBLE);
            if (cursor != null && cursor.getCount() > 0) {
                noFavoritesMessage.setVisibility(View.INVISIBLE);
                mMovieAdapter.setMovieDataFromCursor(cursor);
                mMoviesGrid.setVisibility(View.VISIBLE);
            } else {
                noFavoritesMessage.setVisibility(View.VISIBLE);
                mMoviesGrid.setVisibility(View.INVISIBLE);
            }
        }
    }

}
