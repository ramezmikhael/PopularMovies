package com.example.ramezreda.popularmovies.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.ramezreda.popularmovies.utils.NetworkUtils;

/**
 * Created by RamezReda on 2/17/2018.
 */

public class MoviesListLoader<T> extends AsyncTaskLoader<java.lang.String> {

    private final String mUrl;

    public MoviesListLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public String loadInBackground() {
        return new NetworkUtils().sendHTTPRequest(mUrl);
    }

    @Override
    public void deliverResult(String data) {
        super.deliverResult(data);
    }
}
