package com.example.ramezreda.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ramezreda.popularmovies.R;
import com.example.ramezreda.popularmovies.SuperGlobals;
import com.example.ramezreda.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by RamezReda on 2/17/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    private final List<Movie> mMoviesList;

    private ItemClickListener mItemClickListener;

    public MoviesAdapter(Context context, List<Movie> moviesList) {
        mContext = context;
        mMoviesList = moviesList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MovieViewHolder movieViewHolder = (MovieViewHolder) holder;

        Picasso.with(mContext)
                .load(SuperGlobals.IMAGE_BASE_URL + mMoviesList.get(position).getPosterPath())
                .into(movieViewHolder.img);
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView img;

        public MovieViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mItemClickListener == null)
                return;
            mItemClickListener.onItemClicked(view, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClicked(View view, int pos);
    }
}
