package com.example.ramezreda.popularmovies;

import android.app.Application;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

/**
 * Created by Ramez on 2/26/2018.
 */

public class Global extends Application {

    /*
    This class is a solution for caching images with picasso for offline use
    Got it from Stackoverflow:
    https://stackoverflow.com/a/30686992
     */

    @Override
    public void onCreate() {
        super.onCreate();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

    }
}
