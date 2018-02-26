package com.example.ramezreda.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ramezreda.popularmovies.R;
import com.example.ramezreda.popularmovies.models.Trailer;

import java.util.List;

/**
 * Created by Ramez on 2/25/2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    private final List<Trailer> mTrailersList;
    private TrailerClickListener mTrailerClickListener;

    public TrailersAdapter(Context context, List<Trailer> videosList) {
        mContext = context;
        mTrailersList = videosList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TrailerViewHolder) holder).tvTrailerName.setText(mTrailersList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mTrailersList.size();
    }

    public void setOnTrailerClickListener(TrailerClickListener itemClickListener) {
        this.mTrailerClickListener = itemClickListener;
    }

    private class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        final TextView tvTrailerName;

        public TrailerViewHolder(View view) {
            super(view);
            tvTrailerName = view.findViewById(R.id.trailer_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mTrailerClickListener == null)
                return;
            mTrailerClickListener.onTrailerItemClicked(view, getAdapterPosition());
        }
    }

    public interface TrailerClickListener {
        void onTrailerItemClicked(View view, int pos);
    }
}
