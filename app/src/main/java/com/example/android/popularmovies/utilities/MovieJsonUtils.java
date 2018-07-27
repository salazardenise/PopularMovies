package com.example.android.popularmovies.utilities;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.android.popularmovies.MovieInfo;
import com.example.android.popularmovies.Review;
import com.example.android.popularmovies.Video;

public class MovieJsonUtils {

    private static final String TAG = MovieJsonUtils.class.getSimpleName();

    public static int[] getMovieIdsFromJson(String moviesJsonStr) throws JSONException {

        /* int array to hold each movie id */
        int[] parsedMovieData;
        JSONObject movieJson = new JSONObject(moviesJsonStr);

        /* Is there an error? */
        if (checkForStatusCode(movieJson)) return null;

        JSONArray results = movieJson.getJSONArray(Constants.TMDB_RESULTS);
        parsedMovieData = new int[results.length()];
        for (int i = 0; i < results.length(); i++) {
            int id;

            /* Get the JSON object representing the movie */
            JSONObject movieObject = results.getJSONObject(i);
            id = movieObject.getInt(Constants.TMDB_ID);
            parsedMovieData[i] = id;
        }
        return parsedMovieData;
    }

    public static MovieInfo[] getMoviesFromJson(String moviesJsonStr) throws JSONException {
        MovieInfo[] parsedMovieInfoData;
        JSONObject movieJson = new JSONObject(moviesJsonStr);

        /* Is there an error? */
        if (checkForStatusCode(movieJson)) return null;

        JSONArray results = movieJson.getJSONArray(Constants.TMDB_RESULTS);
        parsedMovieInfoData = new MovieInfo[results.length()];

        for (int i = 0; i < results.length(); i++) {
            int id;
            String title;
            String posterPathEnd;
            String posterPath;
            String backdropPathEnd;
            String backdropPath;
            String plotSynopsis;
            int userRating;
            String releaseDate;

            /* Get the JSON object representing the movieInfo */
            JSONObject movieObject = results.getJSONObject(i);
            id = movieObject.getInt(Constants.TMDB_ID);
            title = movieObject.getString(Constants.TMDB_TITLE);
            posterPathEnd = movieObject.getString(Constants.TMDB_POSTER_PATH);
            posterPath = Constants.POSTER_PATH_BASE_URL + Constants.POSTER_SIZE + posterPathEnd;
            backdropPathEnd = movieObject.getString(Constants.TMDB_BACKDROP_PATH);
            backdropPath = Constants.POSTER_PATH_BASE_URL + Constants.BACKDROP_SIZE + backdropPathEnd;
            plotSynopsis = movieObject.getString(Constants.TMDB_PLOT_SYNOPSIS);
            userRating = movieObject.getInt(Constants.TMDB_USER_RATING);
            releaseDate = movieObject.getString(Constants.TMDB_RELEASE_DATE);
            MovieInfo movieInfo = new MovieInfo(id, title, posterPath, backdropPath, plotSynopsis, userRating, releaseDate);
            parsedMovieInfoData[i] = movieInfo;

        }
        return parsedMovieInfoData;
    }

    public static Review[] getReviewsFromJson(String reviewsJsonStr) throws JSONException {
        Review[] parsedReviews;
        JSONObject reviewsJson = new JSONObject(reviewsJsonStr);

        /* Is there an error? */
        if (checkForStatusCode(reviewsJson)) return null;

        JSONArray results = reviewsJson.getJSONArray(Constants.TMDB_RESULTS);
        parsedReviews = new Review[results.length()];

        for (int i = 0; i < results.length(); i++) {
            String id;
            String author;
            String content;
            String url;

            /* Get the JSON object representing the review */
            JSONObject reviewObject = results.getJSONObject(i);
            id = reviewObject.getString(Constants.TMDB_ID);
            author = reviewObject.getString(Constants.TMDB_AUTHOR);
            content = reviewObject.getString(Constants.TMDB_CONTENT);
            url = reviewObject.getString(Constants.TMDB_URL);
            Review review = new Review(id, author, content, url);
            parsedReviews[i] = review;
        }
        return parsedReviews;
    }

    public static Video[] getVideosFromJson(String trailersJsonStr) throws JSONException {
        Video[] videos;
        JSONObject videosJson = new JSONObject(trailersJsonStr);

        /* Is there an error? */
        if (checkForStatusCode(videosJson)) return null;

        JSONArray results = videosJson.getJSONArray(Constants.TMDB_RESULTS);
        videos = new Video[results.length()];

        for (int i = 0; i < results.length(); i++) {
            /* Get the JSON object representing the video */
            JSONObject videoObject = results.getJSONObject(i);
            String id = videoObject.getString(Constants.TMDB_ID);
            String key = videoObject.getString(Constants.TMDB_KEY);
            String name = videoObject.getString(Constants.TMDB_NAME);
            String site = videoObject.getString(Constants.TMDB_SITE);
            int size = videoObject.getInt(Constants.TMDB_SIZE);
            String type = videoObject.getString(Constants.TMDB_TYPE);
            videos[i] = new Video(id, key, name, site, size, type);
        }
        return videos;
    }

    private static boolean checkForStatusCode(JSONObject movieJson) {
        if (movieJson.has(Constants.TMDB_STATUS_CODE)) {
            Log.v(TAG, "Error retrieving data occurred");
            return true;
        }
        return false;
    }

}
