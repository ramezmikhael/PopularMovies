package com.example.ramezreda.popularmovies.utils;

import com.example.ramezreda.popularmovies.models.Movie;
import com.example.ramezreda.popularmovies.models.Review;
import com.example.ramezreda.popularmovies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    // Movie JSON objects' names
    private static final String JSON_RESULTS = "results";
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_POSTER_PATH = "poster_path";
    private static final String JSON_OVERVIEW = "overview" ;
    private static final String JSON_VOTE_AVERAGE = "vote_average";
    private static final String JSON_RELEASE_DATE = "release_date";

    // Trailers JSON objects' names
    private static final String JSON_TRAILER_NAME = "name";
    private static final String JSON_TRAILER_KEY = "key";

    // Reviews JSON objects' names
    private static final String JSON_REVIEW_AUTHOR = "author";
    private static final String JSON_REVIEW_CONTENT = "content";

    public static List<Movie> parseMoviesJson(String json) {

        try {
            List<Movie> moviesList = new ArrayList<>();

            JSONObject parentJson = new JSONObject(json);
            JSONArray moviesJson = parentJson.getJSONArray(JSON_RESULTS);

            for(int i = 0; i < moviesJson.length(); ++i) {
                JSONObject movieJson = moviesJson.getJSONObject(i);

                int id = movieJson.getInt(JSON_ID);
                String title = movieJson.getString(JSON_TITLE);
                String posterPath = movieJson.getString(JSON_POSTER_PATH);
                String overview = movieJson.getString(JSON_OVERVIEW);
                double voteAverage = movieJson.getDouble(JSON_VOTE_AVERAGE);
                String releaseDate = movieJson.getString(JSON_RELEASE_DATE);

                moviesList.add(new Movie(id, title, posterPath, overview, voteAverage, releaseDate));
            }

            return moviesList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Trailer> parseTrailersJson(String json) {
        try {
            List<Trailer> trailersList = new ArrayList<>();

            JSONObject parentJson = new JSONObject(json);
            JSONArray trailersJson = parentJson.getJSONArray(JSON_RESULTS);

            for(int i = 0; i < trailersJson.length(); ++i) {
                JSONObject trailerJson = trailersJson.getJSONObject(i);

                String name = trailerJson.getString(JSON_TRAILER_NAME);
                String key = trailerJson.getString(JSON_TRAILER_KEY);

                trailersList.add(new Trailer(name, key));
            }
            return trailersList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Review> parseReviewsJson(String json) {
        try {
            List<Review> reviewList = new ArrayList<>();

            JSONObject parentJson = new JSONObject(json);
            JSONArray trailersJson = parentJson.getJSONArray(JSON_RESULTS);

            for(int i = 0; i < trailersJson.length(); ++i) {
                JSONObject trailerJson = trailersJson.getJSONObject(i);

                String author = trailerJson.optString(JSON_REVIEW_AUTHOR);
                String content = trailerJson.optString(JSON_REVIEW_CONTENT);

                reviewList.add(new Review(author, content));
            }
            return reviewList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
