package com.nds.nanodegree.movieapp.views.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.nds.nanodegree.movieapp.R;
import com.nds.nanodegree.movieapp.model.TrailerModel;

import java.util.List;

/**
 * Created by Namrata Shah on 3/29/2017.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.TrailerViewHolder>{

    private LayoutInflater inflater;
    private List<TrailerModel> trailers;
    private AdapterView.OnItemClickListener clickListener;

    public MovieTrailerAdapter(Context context, List<TrailerModel> models, AdapterView.OnItemClickListener listener){
        this.inflater = LayoutInflater.from(context);
        this.trailers = models;
        this.clickListener = listener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.trailer_list_row, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.trailerTextView.setText(trailers.get(position).getTrailerName());
    }

    @Override
    public int getItemCount() {
        if(trailers != null)
            return trailers.size();
        return 0;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemClickListener{

        TextView trailerTextView;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            trailerTextView = (TextView)itemView.findViewById(R.id.trailerTextView);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            clickListener.onItemClick(parent,view, position, id);
        }
    }

}
