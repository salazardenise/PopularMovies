package com.example.android.popularmovies.utilities;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    /**
     * Builds the URL used to talk to the movies db using the popular or top rated options.
     *
     * @param option options will be either popular or top_rated
     * @return The URL to use to query the movies
     */
    public static URL buildUrl(String option) {
        String movies_url = Constants.BASE_URL + option;
        Uri builtUri = Uri.parse(movies_url).buildUpon()
                            .appendQueryParameter(Constants.API_KEY, Constants.API_KEY_VALUE)
                            .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Log.v(TAG, "Built URI " + url);
        return url;
    }

    public static URL buildReviewsURL(int id) {
        String reviews_url = Constants.BASE_URL + id + Constants.REVIEWS;
        Uri builtUri = Uri.parse(reviews_url).buildUpon()
                            .appendQueryParameter(Constants.API_KEY, Constants.API_KEY_VALUE)
                            .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Log.v(TAG, "Built URI " + url);
        return url;
    }

    public static URL buildTrailersURL(int id) {
        String reviews_url = Constants.BASE_URL + id + Constants.VIDEOS;
        Uri builtUri = Uri.parse(reviews_url).buildUpon()
                .appendQueryParameter(Constants.API_KEY, Constants.API_KEY_VALUE)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Log.v(TAG, "Built URI " + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter(Constants.DELIMITTER);

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
