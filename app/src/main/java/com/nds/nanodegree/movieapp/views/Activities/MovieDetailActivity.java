package com.nds.nanodegree.movieapp.views.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.nds.nanodegree.movieapp.R;
import com.nds.nanodegree.movieapp.common.Constants;
import com.nds.nanodegree.movieapp.model.MovieModel;
import com.nds.nanodegree.movieapp.views.fragments.MovieDetailFragment;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        if(savedInstanceState !=null)
            return;
        else
            addMovieDetailFragment();
    }

    private void addMovieDetailFragment() {
        if(!isFinishing()){
            MovieModel movieModel = null;
            Intent callerIntent = getIntent();
            if(callerIntent != null && callerIntent.hasExtra(Constants.SELECTED_MOVIE_DETAIL_BUNDLE_KEY)){
                movieModel = callerIntent.getParcelableExtra(Constants.SELECTED_MOVIE_DETAIL_BUNDLE_KEY);
            }
            MovieDetailFragment detailFragment = MovieDetailFragment.newInstance(movieModel);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.movieDetailContainer,
                    detailFragment, Constants.MOVIE_DETAIL_FRAGMENT_TAG).commit();
        }
    }

}
