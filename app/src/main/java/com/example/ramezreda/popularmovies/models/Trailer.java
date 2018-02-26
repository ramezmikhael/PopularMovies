package com.example.ramezreda.popularmovies.models;

/**
 * Created by Ramez on 2/25/2018.
 */

public class Trailer {
    private final String name;
    private final String key;

    public Trailer(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
