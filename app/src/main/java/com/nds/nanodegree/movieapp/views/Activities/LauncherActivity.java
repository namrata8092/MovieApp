package com.nds.nanodegree.movieapp.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.nds.nanodegree.movieapp.R;
import com.nds.nanodegree.movieapp.common.Constants;
import com.nds.nanodegree.movieapp.views.fragments.MoviePosterFragment;

/**
 * Created by Namrata Shah on 2/26/2017.
 */

public class LauncherActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_activity);
        if(savedInstanceState != null){
            return;
        }else{
            addMoviePosterFragment();
        }
    }

    private void addMoviePosterFragment() {
        if(!isFinishing()){
            MoviePosterFragment moviePosterFragment =  MoviePosterFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.moviePosterGridContainer,
                    moviePosterFragment, Constants.MOVIE_POSTER_FRAGMENT_TAG).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_search_by, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settings){
            Class destinationClass = MovieSettingsActivity.class;
            Context sourceContext = LauncherActivity.this;
            Intent settingsIntent = new Intent(sourceContext, destinationClass);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
