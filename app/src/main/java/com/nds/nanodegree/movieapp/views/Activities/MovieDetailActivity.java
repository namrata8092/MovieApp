package com.nds.nanodegree.movieapp.views.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nds.nanodegree.movieapp.R;
import com.nds.nanodegree.movieapp.common.Constants;
import com.nds.nanodegree.movieapp.common.Util;
import com.nds.nanodegree.movieapp.model.MovieModel;

/* MovieDetailActivity will display detail of selected movie.
Movie detail includes
1. Original name of movie,
2. Movie thumbnail,
3. Release date,
4. Movie average rating
5. Movie overview
* */
/**
 * Created by Namrata Shah on 2/26/2017.
 */
public class MovieDetailActivity extends AppCompatActivity {
    private MovieModel movieModel;
    private static int[] dimensions = new int[2];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        if(savedInstanceState != null && savedInstanceState.containsKey(Constants.SELECTED_MOVIE_DETAIL_BUNDLE_KEY)){
            movieModel = savedInstanceState.getParcelable(Constants.SELECTED_MOVIE_DETAIL_BUNDLE_KEY);
        }else{
            Intent callerIntent = getIntent();
            if(callerIntent != null && callerIntent.hasExtra(Constants.SELECTED_MOVIE_DETAIL_BUNDLE_KEY)){
                movieModel = callerIntent.getParcelableExtra(Constants.SELECTED_MOVIE_DETAIL_BUNDLE_KEY);
            }
        }

        TextView mTitleTV = (TextView)findViewById(R.id.movieOriginalTitle);
        ImageView mThumbNailImageView = (ImageView)findViewById(R.id.movieThumbnail);
        TextView mReleaseDateTV = (TextView)findViewById(R.id.releaseDate);
        TextView mAverageRatingTV = (TextView)findViewById(R.id.averageRating);
        TextView mOverviewTV = (TextView)findViewById(R.id.overview);

        mTitleTV.setText(movieModel.getOriginalTitle());

        mReleaseDateTV.setText(getResources().getString(R.string.release_date)+" "+movieModel.getReleaseDate());
        mAverageRatingTV.setText(getResources().getString(R.string.average_rating)+" "+movieModel.getVoteAverage());

        mOverviewTV.setText(movieModel.getOverview());
        dimensions = Util.getScreenDimensions(mThumbNailImageView.getContext());
        Glide.with(mThumbNailImageView.getContext())
                .load(Util.createImageURI(movieModel.getBackDropURL()))
                .override(dimensions[0], dimensions[1]).into(mThumbNailImageView);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.SELECTED_MOVIE_DETAIL_BUNDLE_KEY, movieModel);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        dimensions = Util.getScreenDimensions(getApplicationContext());
        super.onConfigurationChanged(newConfig);
    }
}
