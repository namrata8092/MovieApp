package com.nds.nanodegree.movieapp.views.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nds.nanodegree.movieapp.R;
import com.nds.nanodegree.movieapp.model.TrailerModel;

import java.util.List;

/**MovieTrailerAdapter is custom adapter to display trailers.
 * Created by Namrata Shah on 3/29/2017.
 */

public class MovieTrailerAdapter extends ArrayAdapter {

    private LayoutInflater inflater;
    private List<TrailerModel> trailers;


    public MovieTrailerAdapter(Context context, int resource, List<TrailerModel> trailers) {
        super(context, resource, trailers);
        this.trailers = trailers;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(trailers!=null)
            return trailers.size();
        return 0;
    }

    @Nullable
    @Override
    public TrailerModel getItem(int position) {
        return trailers.get(position);
    }

    @Override
    public int getPosition(Object item) {
        return super.getPosition(item);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.trailer_list_row, parent, false);
        }
        TextView trailerTextView = (TextView)convertView.findViewById(R.id.trailerTextView);
        trailerTextView.setText(trailers.get(position).getTrailerName());
        return convertView;
    }
}