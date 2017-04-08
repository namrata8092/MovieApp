package com.nds.nanodegree.movieapp.views.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.nds.nanodegree.movieapp.R;
import com.nds.nanodegree.movieapp.views.fragments.MovieSettingsFragment;

/**
 * Created by Namrata Shah on 4/3/2017.
 */

public class MovieSettingsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_settings);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.movie_settings_fragment, new MovieSettingsFragment(), getString(R.string.movie_pref_sort_key)).commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
