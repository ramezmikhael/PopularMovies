package com.example.ramezreda.popularmovies;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ramezreda.popularmovies.loaders.MoviesListLoader;
import com.example.ramezreda.popularmovies.models.Movie;
import com.example.ramezreda.popularmovies.utils.JsonUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,
        MoviesAdapter.ItemClickListener,
        AdapterView.OnItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final java.lang.String EXTRA_URL = "url";
    private final int MOVIES_LIST_LOADER_ID = 1;
    private final String URL = "http://api.themoviedb.org/3/movie/";
    private final String SORT_POPULAR = "popular";
    private final String SORT_TOP_RATED = "top_rated";
    private final String API_KEY = "?api_key=" + BuildConfig.API_KEY;
    private final int GRID_COLUMN_COUNT = 2;
    public static final String EXTRA_PARCELABLE_NAME = "movie";

    private List<Movie> mMovies;
    private Spinner spinnerSort;
    private RecyclerView rvMoviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerSort = findViewById(R.id.spinner_sort);
        rvMoviesList = findViewById(R.id.movies_list);

        spinnerSort.setOnItemSelectedListener(this);
        rvMoviesList.setLayoutManager(new GridLayoutManager(this, GRID_COLUMN_COUNT));

    }

    @Override
    protected void onStart() {

        super.onStart();

        startLoader(0);
    }

    private void startLoader(int sortIndex) {
        /*
        I had a nightmare making this works...
        I used the following code without using forceLoad(), it didn't work,
        After I googled the problem found this stackoverflow answer

        https://stackoverflow.com/a/10537392
         */

        String url = null;

        if(sortIndex == 0)
            url = URL + SORT_POPULAR + API_KEY;
        else if(sortIndex == 1)
            url = URL + SORT_TOP_RATED + API_KEY;

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, url);
        getSupportLoaderManager().restartLoader(MOVIES_LIST_LOADER_ID, bundle, this).forceLoad();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        String url = args.getString(EXTRA_URL);
        return new MoviesListLoader<>(MainActivity.this, url);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, data);
        if(data.equals("")) {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }

        mMovies = JsonUtils.parseMoviesJson(data);

        MoviesAdapter moviesAdapter = new MoviesAdapter(this, mMovies);
        moviesAdapter.setOnItemClickListener(this);
        rvMoviesList.setAdapter(moviesAdapter);
//        moviesAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onItemClicked(View view, int pos) {
        Intent i = new Intent(this, DetailsActivity.class);
        i.putExtra(EXTRA_PARCELABLE_NAME, mMovies.get(pos));
        startActivity(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        startLoader(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}