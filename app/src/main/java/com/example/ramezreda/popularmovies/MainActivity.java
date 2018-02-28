package com.example.ramezreda.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ramezreda.popularmovies.adapters.MoviesAdapter;
import com.example.ramezreda.popularmovies.data.FavoritesContract.FavoritesEntry;
import com.example.ramezreda.popularmovies.loaders.MoviesListLoader;
import com.example.ramezreda.popularmovies.models.Movie;
import com.example.ramezreda.popularmovies.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,
        MoviesAdapter.ItemClickListener {

    private static final String TAG = "MainActivity";
    private static final java.lang.String EXTRA_URL = "url";
    private static final String EXTRA_SS_SORT_ID = "sort_id";

    private final int MOVIES_LIST_LOADER_ID = 1;
    private int mSelectedSortMenuItem = 0;

    private final String SORT_POPULAR = "popular";
    private final String SORT_TOP_RATED = "top_rated";
    private final String API_KEY = "?api_key=" + BuildConfig.API_KEY;
    private int GRID_COLUMN_COUNT;
    public static final String EXTRA_PARCELABLE_NAME = "movie";

    private List<Movie> mMovies;
    private RecyclerView rvMoviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            GRID_COLUMN_COUNT = 2;
        } else {
            GRID_COLUMN_COUNT = 4;
        }

        rvMoviesList = findViewById(R.id.movies_list);

        rvMoviesList.setLayoutManager(new GridLayoutManager(this, GRID_COLUMN_COUNT));

    }

    @Override
    protected void onResume() {

        super.onResume();
        getMovies();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_SS_SORT_ID, mSelectedSortMenuItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mSelectedSortMenuItem = savedInstanceState.getInt(EXTRA_SS_SORT_ID);
        getMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mSelectedSortMenuItem = item.getItemId();
        return getMovies();
    }

    private boolean getMovies() {
        if(mSelectedSortMenuItem == R.id.sort_popular) {
            startLoader(SORT_POPULAR);
            return true;
        } else if(mSelectedSortMenuItem == R.id.sort_top_rated) {
            startLoader(SORT_TOP_RATED);
            return true;
        } else if(mSelectedSortMenuItem == R.id.sort_favorites) {
            queryFavorites();
            return true;
        } else {
            startLoader(SORT_POPULAR);
            return true;
        }
    }

    private void startLoader(String sort) {
        /*
        I had a nightmare making this works...
        I used the following code without using forceLoad(), it didn't work,
        After I googled the problem found this stackoverflow answer

        https://stackoverflow.com/a/10537392
         */

        String url = SuperGlobals.API_URL + sort + API_KEY;

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

    private void queryFavorites() {
        Uri uri = FavoritesEntry.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        mMovies = new ArrayList<>();
        while (cursor.moveToNext()) {
            Movie movie = new Movie(
                    cursor.getInt(cursor.getColumnIndex(FavoritesEntry.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_POSTER_PATH)),
                    cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_OVERVIEW)),
                    cursor.getDouble(cursor.getColumnIndex(FavoritesEntry.COLUMN_VOTE_AVERAGE)),
                    cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_RELEASE_DATE))
                    );
            mMovies.add(movie);
        }

        MoviesAdapter moviesAdapter = new MoviesAdapter(this, mMovies);
        moviesAdapter.setOnItemClickListener(this);
        rvMoviesList.setAdapter(moviesAdapter);
        cursor.close();
    }
}
