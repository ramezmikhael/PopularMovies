package com.example.ramezreda.popularmovies.utils;

import com.example.ramezreda.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String JSON_RESULTS = "results";
    private static final String JSON_TITLE = "title";
    private static final String JSON_POSTER_PATH = "poster_path";
    private static final String JSON_OVERVIEW = "overview" ;
    private static final String JSON_VOTE_AVERAGE = "vote_average";
    private static final String JSON_RELEASE_DATE = "release_date";

    public static List<Movie> parseMoviesJson(String json) {

        try {
            List<Movie> moviesList = new ArrayList<>();

            JSONObject parentJson = new JSONObject(json);
            JSONArray moviesJson = parentJson.getJSONArray(JSON_RESULTS);

            for(int i = 0; i < moviesJson.length(); ++i) {
                JSONObject movieJson = moviesJson.getJSONObject(i);

                String title = movieJson.getString(JSON_TITLE);
                String posterPath = movieJson.getString(JSON_POSTER_PATH);
                String overview = movieJson.getString(JSON_OVERVIEW);
                double voteAverage = movieJson.getDouble(JSON_VOTE_AVERAGE);
                String releaseDate = movieJson.getString(JSON_RELEASE_DATE);

                moviesList.add(new Movie(title, posterPath, overview, voteAverage, releaseDate));
            }

            return moviesList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
