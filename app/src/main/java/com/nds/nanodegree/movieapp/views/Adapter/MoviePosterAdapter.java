package com.nds.nanodegree.movieapp.views.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nds.nanodegree.movieapp.R;
import com.nds.nanodegree.movieapp.common.Util;
import com.nds.nanodegree.movieapp.model.MovieModel;

import java.util.List;

/*MoviePosterAdapter is custom adapter to display movie poster and movie name.
* */

/**
 * Created by Namrata Shah on 2/26/2017.
 */
public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MoviePosterViewHolder> {
    private List<MovieModel> movieList;
    private LayoutInflater mInflater;
    private AdapterView.OnItemClickListener cellClickListener;

    public MoviePosterAdapter(Context context, List<MovieModel> movies, AdapterView.OnItemClickListener clickListener){
        this.movieList = movies;
        this.mInflater = LayoutInflater.from(context);
        this.cellClickListener = clickListener;
    }

    public class MoviePosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mPosterIV;
        TextView mMovieTitleTV;

        public MoviePosterViewHolder(View view){
            super(view);
            //uncomment below lines & comment findviewbyid , grid is not populated.
//            ViewGroup viewGroup = (ViewGroup) view.getRootView();
//            GridCellLayoutBinding gridCellLayoutBinding = DataBindingUtil.inflate(mInflater, R.layout.grid_cell_layout, viewGroup,false);
//            mPosterIV = gridCellLayoutBinding.moviePoster;
//            mMovieTitleTV = gridCellLayoutBinding.movieTitle;
            mPosterIV = (ImageView) view.findViewById(R.id.moviePoster);
            mMovieTitleTV = (TextView)view.findViewById(R.id.movieTitle);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            cellClickListener.onItemClick(null, v, getAdapterPosition(), v.getId());
        }
    }

    @Override
    public MoviePosterViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View moviePosterView = mInflater.inflate(R.layout.grid_cell_layout, parent, false);
        final MoviePosterViewHolder holder = new MoviePosterViewHolder(moviePosterView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MoviePosterViewHolder holder, int position) {
        MovieModel model = movieList.get(position);
        if(model != null){
            holder.mMovieTitleTV.setText(model.getTitle());
            Glide.with(holder.mPosterIV.getContext()).load(
                    Util.createImageURI(model.getPosterURL())).into(holder.mPosterIV);
        }
    }

    @Override
    public int getItemCount() {
        if(movieList!= null)
            return movieList.size();
        else return 0;
    }


}
