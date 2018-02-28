package com.example.ramezreda.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramezreda.popularmovies.adapters.ReviewsAdapter;
import com.example.ramezreda.popularmovies.adapters.TrailersAdapter;
import com.example.ramezreda.popularmovies.data.FavoritesContract.FavoritesEntry;
import com.example.ramezreda.popularmovies.loaders.MoviesListLoader;
import com.example.ramezreda.popularmovies.models.Movie;
import com.example.ramezreda.popularmovies.models.Review;
import com.example.ramezreda.popularmovies.models.Trailer;
import com.example.ramezreda.popularmovies.utils.JsonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>,
        TrailersAdapter.TrailerClickListener {

    private static final String TAG = "DetailsActivity";
    private final int TRAILER_LIST_LOADER_ID = 2;
    private final int REVIEWS_LIST_LOADER_ID = 3;

    private final String API_KEY = "?api_key=" + BuildConfig.API_KEY;
    private final String EXTRA_URL = "url";
    private List<Trailer> mTrailers;
    private Movie mMovie;

    private ImageButton btnFavorite;
    private RecyclerView rvTrailers;
    private RecyclerView rvReviews;

    private MenuItem menuShare;

    private boolean mIsFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent i = getIntent();

        mMovie = i.getParcelableExtra(MainActivity.EXTRA_PARCELABLE_NAME);

        Log.d(TAG,  mMovie.getTitle());

        btnFavorite = findViewById(R.id.favorite_button);
        rvTrailers = findViewById(R.id.trailers_recycler_view);
        rvTrailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        rvReviews = findViewById(R.id.reviews_recycler_view);
        rvReviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        /* For smooth scrolling according to this SoF answer
         *  https://stackoverflow.com/a/33143512
         */
        rvTrailers.setNestedScrollingEnabled(false);
        rvReviews.setNestedScrollingEnabled(false);

        TextView title = findViewById(R.id.title);
        TextView releaseDate = findViewById(R.id.release_date);
        TextView plot = findViewById(R.id.plot);
        TextView voteAvg = findViewById(R.id.vote_avg);
        ImageView poster = findViewById(R.id.poster_image);

        title.setText(mMovie.getTitle());
        releaseDate.setText(mMovie.getReleaseDate());
        plot.setText(mMovie.getOverview());
        voteAvg.setText(String.valueOf(mMovie.getVoteAverage()));

        checkFavorite();

        Picasso.with(this).load(SuperGlobals.IMAGE_BASE_URL + mMovie.getPosterPath()).into(poster);

        startTrailerLoader();
        startReviewsLoader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater infalter = new MenuInflater(this);
        infalter.inflate(R.menu.detail_menu, menu);

        menuShare = menu.findItem(R.id.menu_share);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.menu_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, SuperGlobals.YOUTUBE_BASE_URL + mTrailers.get(0).getKey());
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_movie_trailer)));

            return true;
        }

        return false;
    }

    private void checkFavorite() {
        Uri uri = FavoritesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(mMovie.getId())).build();

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        mIsFavorite = cursor.getCount() > 0;
        toggleFavoriteImage(mIsFavorite);
        cursor.close();
    }

    private void startReviewsLoader() {
        String url = String.format(SuperGlobals.REVIEWS_URL, mMovie.getId()) + API_KEY;
        Log.d(TAG, "url:" + url );

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, url);
        getSupportLoaderManager().restartLoader(REVIEWS_LIST_LOADER_ID, bundle, this).forceLoad();
    }

    private void startTrailerLoader() {
        String url = String.format(SuperGlobals.TRAILERS_URL, mMovie.getId()) + API_KEY;
        Log.d(TAG, "url:" + url );

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, url);
        getSupportLoaderManager().restartLoader(TRAILER_LIST_LOADER_ID, bundle, this).forceLoad();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        String url = args.getString(EXTRA_URL);
        return new MoviesListLoader<>(this, url);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, data);
        if(data.equals("")) {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }

        if(loader.getId() == TRAILER_LIST_LOADER_ID) {
            mTrailers = JsonUtils.parseTrailersJson(data);
            TrailersAdapter trailersAdapter = new TrailersAdapter(this, mTrailers);
            trailersAdapter.setOnTrailerClickListener(this);
            rvTrailers.setAdapter(trailersAdapter);

            // If there are no trailers, hide the share menu item
            if(mTrailers.size() == 0)
                menuShare.setVisible(false);
            else
                menuShare.setVisible(true);

        } else if(loader.getId() == REVIEWS_LIST_LOADER_ID) {
            List<Review> reviews = JsonUtils.parseReviewsJson(data);
            ReviewsAdapter reviewsAdapter = new ReviewsAdapter(this, reviews);
            rvReviews.setAdapter(reviewsAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onTrailerItemClicked(View view, int pos) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(SuperGlobals.YOUTUBE_BASE_URL + mTrailers.get(pos).getKey()));
        startActivity(i);
    }

    private void toggleFavoriteImage(boolean on) {
        if(on)
            btnFavorite.setImageResource(android.R.drawable.star_big_on);
        else
            btnFavorite.setImageResource(android.R.drawable.star_big_off);
    }

    public void favoriteClicked(View view) {
        if(mIsFavorite) {
            Uri uri = FavoritesEntry.CONTENT_URI;
            String id = String.valueOf(mMovie.getId());
            uri = uri.buildUpon().appendPath(id).build();
            int count = getContentResolver().delete(uri, null, null);
            if(count > 0) {
                Toast.makeText(this, R.string.success_removed_from_favorites, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.error_remove_from_favorites, Toast.LENGTH_SHORT).show();
                return;
            }
        } else {

            ContentValues values = new ContentValues();
            values.put(FavoritesEntry.COLUMN_ID, mMovie.getId());
            values.put(FavoritesEntry.COLUMN_TITLE, mMovie.getTitle());
            values.put(FavoritesEntry.COLUMN_OVERVIEW, mMovie.getOverview());
            values.put(FavoritesEntry.COLUMN_POSTER_PATH, mMovie.getPosterPath());
            values.put(FavoritesEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
            values.put(FavoritesEntry.COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());

            Uri uri = getContentResolver().insert(FavoritesEntry.CONTENT_URI, values);
            if(uri != null) {
                Toast.makeText(this, R.string.success_added_to_favorites, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.error_adding_to_favorites, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        mIsFavorite = !mIsFavorite;
        toggleFavoriteImage(mIsFavorite);
    }
}
