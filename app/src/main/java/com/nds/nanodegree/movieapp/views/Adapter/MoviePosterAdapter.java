package com.nds.nanodegree.movieapp.views.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nds.nanodegree.movieapp.R;
import com.nds.nanodegree.movieapp.common.Util;
import com.nds.nanodegree.movieapp.model.MovieModel;

import java.util.List;

/*MoviePosterAdapter is custom adadpter to display movie poster and movie name.
* */

/**
 * Created by Namrata Shah on 2/26/2017.
 */
public class MoviePosterAdapter extends ArrayAdapter<MovieModel> {
    List<MovieModel> movieList;

    public MoviePosterAdapter(Context context, int resource, List<MovieModel> objects) {
        super(context, 0, objects);
        this.movieList = objects;
    }

    @Override
    public int getCount() {
        if(movieList!= null)
            return movieList.size();
        else return 0;
    }

    @Nullable
    @Override
    public MovieModel getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public int getPosition(MovieModel item) {
        return super.getPosition(item);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieModel model = getItem(position);
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.grid_cell_layout, parent, false);
        }
        ImageView mPosterIV = (ImageView) convertView.findViewById(R.id.moviePoster);
        TextView mMovieTitleTV = (TextView)convertView.findViewById(R.id.movieTitle);
        if(model != null){
            mMovieTitleTV.setText(model.getTitle());
            Glide.with(mPosterIV.getContext()).load(Util.createImageURI(model.getPosterURL())).into(mPosterIV);
        }
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
