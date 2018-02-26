package com.example.ramezreda.popularmovies.models;

/**
 * Created by Ramez on 2/25/2018.
 */

public class Review {
    private final String author;
    private final String content;

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
