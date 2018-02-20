package com.example.ramezreda.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ramezreda.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DetailsActivity";
    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent i = getIntent();
        Movie movie = i.getParcelableExtra(MainActivity.EXTRA_PARCELABLE_NAME);

        Log.d(TAG,  movie.getTitle());

        TextView title = findViewById(R.id.title);
        TextView releaseDate = findViewById(R.id.release_date);
        TextView plot = findViewById(R.id.plot);
        TextView voteAvg = findViewById(R.id.vote_avg);
        ImageView poster = findViewById(R.id.poster);

        title.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());
        plot.setText(movie.getOverview());
        voteAvg.setText(String.valueOf(movie.getVoteAverage()));

        Picasso.with(this).load(IMAGE_BASE_URL + movie.getPosterPath()).into(poster);
    }
}
