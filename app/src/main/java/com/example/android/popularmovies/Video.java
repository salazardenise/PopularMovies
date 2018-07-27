package com.example.android.popularmovies;


import android.net.Uri;

import com.example.android.popularmovies.utilities.Constants;

import java.net.MalformedURLException;
import java.net.URL;

public class Video {
    private String id;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;
    private String link;

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public String getLink() { return link;}

    public Video (String id, String key, String name, String site, int size, String type){
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;

        Uri builtUri = Uri.parse(Constants.BASE_URL_YOUTUBE).buildUpon()
                .appendQueryParameter(Constants.QUERY_PARAM_V, key)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.link = url.toString();
    }

    public String toString() {
        return this.name + ": " + this.key;
    }

}
