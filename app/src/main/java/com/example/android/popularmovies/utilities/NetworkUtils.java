package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "api_key";
    private static final String API_KEY_VALUE = "[INSERT_API_KEY]";

    /**
     * Builds the URL used to talk to the movies db using the popular or top rated options.
     *
     * @param option options will be either popular or top_rated
     * @return The URL to use to query the movies
     */
    public static URL buildUrl(String option) {
        String movies_url = BASE_URL + option;
        Uri builtUri = Uri.parse(movies_url).buildUpon()
                            .appendQueryParameter(API_KEY, API_KEY_VALUE)
                            .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built URI " + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

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
