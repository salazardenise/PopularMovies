package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.GridLayoutManager;

import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MovieAdapter mMovieAdapter;
    private static final String option_popular = "popular";
    private static final String option_top_rated = "top_rated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up recycler view
        RecyclerView mMoviesGrid = (RecyclerView) findViewById(R.id.rv_movies);

        // set up grid layout manager
        int numOfColumns;
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            numOfColumns = 3; // Portrait Mode
        } else {
            numOfColumns = 4; // Landscape Mode
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numOfColumns);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMoviesGrid.setLayoutManager(gridLayoutManager);

        // set up adapter
        mMovieAdapter = new MovieAdapter(this);
        mMoviesGrid.setAdapter(mMovieAdapter);

        /* Once all of our views are setup, we can load the movie data. */
        loadMovieData(option_popular);
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
        new FetchMovieData().execute(option);
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
            if (movieInfoData != null) {
                mMovieAdapter.setMovieData(movieInfoData);
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
            Log.i(TAG, "Most Popular selected");
            loadMovieData(option_popular);
            return true;
        } else if (menuItemThatWasSelected == R.id.option_top_rated) {
            Log.i(TAG, "Top Rated selected");
            loadMovieData(option_top_rated);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
