package com.example.android.popularmovies.utilities;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.android.popularmovies.MovieInfo;

public class MovieJsonUtils {

    private static final String TAG = MovieJsonUtils.class.getSimpleName();
    private static final String TMDB_STATUS_CODE = "status_code";
    private static final String TMDB_RESULTS = "results";
    private static final String TMDB_ID = "id";
    private static final String TMDB_TITLE = "title";
    private static final String TMDB_POSTER_PATH = "poster_path";
    private static final String TMDB_BACKDROP_PATH = "backdrop_path";
    private static final String TMDB_PLOT_SYNOPSIS = "overview";
    private static final String TMDB_USER_RATING = "vote_average";
    private static final String TMDB_RELEASE_DATE = "release_date";


    public static int[] getMovieIdsFromJson(String moviesJsonStr) throws JSONException {

        /* int array to hold each movie id */
        int[] parsedMovieData;
        JSONObject movieJson = new JSONObject(moviesJsonStr);

        /* Is there an error? */
        if (checkForStatusCode(movieJson)) return null;

        JSONArray results = movieJson.getJSONArray(TMDB_RESULTS);
        parsedMovieData = new int[results.length()];
        for (int i = 0; i < results.length(); i++) {
            int id;

            /* Get the JSON object representing the movie */
            JSONObject movieObject = results.getJSONObject(i);
            id = movieObject.getInt(TMDB_ID);
            parsedMovieData[i] = id;
        }
        return parsedMovieData;
    }

    public static MovieInfo[] getMoviesFromJson(String moviesJsonStr) throws JSONException {
        MovieInfo[] parsedMovieInfoData;
        JSONObject movieJson = new JSONObject(moviesJsonStr);

        /* Is there an error? */
        if (checkForStatusCode(movieJson)) return null;

        JSONArray results = movieJson.getJSONArray(TMDB_RESULTS);
        parsedMovieInfoData = new MovieInfo[results.length()];

        for (int i = 0; i < results.length(); i++) {
            int id;
            String title;
            String posterPath;
            String backdropPath;
            String plotSynopsis;
            int userRating;
            String releaseDate;

            /* Get the JSON object representing the movieInfo */
            JSONObject movieObject = results.getJSONObject(i);
            id = movieObject.getInt(TMDB_ID);
            title = movieObject.getString(TMDB_TITLE);
            posterPath = movieObject.getString(TMDB_POSTER_PATH);
            backdropPath = movieObject.getString(TMDB_BACKDROP_PATH);
            plotSynopsis = movieObject.getString(TMDB_PLOT_SYNOPSIS);
            userRating = movieObject.getInt(TMDB_USER_RATING);
            releaseDate = movieObject.getString(TMDB_RELEASE_DATE);
            MovieInfo movieInfo = new MovieInfo(id, title, posterPath, backdropPath, plotSynopsis, userRating, releaseDate);
            parsedMovieInfoData[i] = movieInfo;
            //Log.i(TAG, movieInfo.toString());

        }
        return parsedMovieInfoData;
    }

    private static boolean checkForStatusCode(JSONObject movieJson) {
        if (movieJson.has(TMDB_STATUS_CODE)) {
            Log.v(TAG, "Error retrieving data occurred");
            return true;
        }
        return false;
    }


}
