package com.nds.nanodegree.movieapp.views.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.nds.nanodegree.movieapp.R;
import com.nds.nanodegree.movieapp.common.Constants;
import com.nds.nanodegree.movieapp.common.Util;
import com.nds.nanodegree.movieapp.model.MovieModel;
import com.nds.nanodegree.movieapp.model.MovieSearchResult;
import com.nds.nanodegree.movieapp.services.FetchMovieData;
import com.nds.nanodegree.movieapp.views.Adapter.MoviePosterAdapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Namrata Shah on 2/26/2017.
 */
public class LauncherActivity extends AppCompatActivity{

    private GridView mPosterGrid;
    private MoviePosterAdapter mMovieAdapter;
    private MovieSearchResult movieSearchResult;
    private List<MovieModel> mMovieList;
    private Context mSourceContext;
    private ProgressBar mMovieDataFetchProgressBar;
    private AlertDialog networkAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        mPosterGrid = (GridView)findViewById(R.id.moviePosterGrid);
        mMovieDataFetchProgressBar = (ProgressBar)findViewById(R.id.movieFetchProgress);
        mSourceContext = this;
        if(savedInstanceState != null && savedInstanceState.containsKey(Constants.MOVIE_LIST_BUNDLE_KEY)){
            movieSearchResult = savedInstanceState.getParcelable(Constants.MOVIE_LIST_BUNDLE_KEY);
            mMovieList = movieSearchResult.getMovieList();
            setGridAdapter();
        }else{
            fetchMovieDataFor(Util.buildPopularMovieSearchURL());
        }

        mPosterGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MovieModel movie = mMovieList.get(i);
                displayDetailActivity(movie, mSourceContext);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(networkAlertDialog != null){
            networkAlertDialog.dismiss();
            networkAlertDialog = null;
        }
    }

    /**
     * This method will display movie detail page.
     * For selected movie, details are passed from current activity.
     * @param movie
     * @param sourceContext
     */
    private void displayDetailActivity(MovieModel movie, Context sourceContext) {
        Class destinationClass = MovieDetailActivity.class;
        Intent movieDetailActivity = new Intent(sourceContext, destinationClass);
        movieDetailActivity.putExtra(Constants.SELECTED_MOVIE_DETAIL_BUNDLE_KEY,movie);
        startActivity(movieDetailActivity);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(movieSearchResult!=null)
        outState.putParcelable(Constants.MOVIE_LIST_BUNDLE_KEY, movieSearchResult);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_search_by, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        movieSearchResult = null;
        switch(item.getItemId()){
            case R.id.byPopular :
                fetchMovieDataFor(Util.buildPopularMovieSearchURL());
                setTitle(R.string.popular_movie);
                break;
            case R.id.byRating :
                fetchMovieDataFor(Util.buildMovieSearchURLByRating());
                setTitle(R.string.movie_by_rating);
                break;
            default :
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * This method will fetch movie data using TMDB API & updated adapter data.
     * It accepts URL as input.
     * If there no internet connection it will show alertdialog.
     * @param URL
     */
    private void fetchMovieDataFor(String URL){
        if(Util.isNetworkAvailable(getApplicationContext())){
            try {
                FetchMovieData fetchData = new FetchMovieData(mMovieDataFetchProgressBar,
                        new URL(URL), new FetchMovieData.MovieResponse() {
                    @Override
                    public void getMovieSearchResults(MovieSearchResult result) {
                        movieSearchResult = result;
                        mMovieList = result.getMovieList();
                        setGridAdapter();
                    }
                });
                fetchData.execute();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(mSourceContext).setCancelable(false)
                    .setMessage(R.string.no_internet_error).setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });
            networkAlertDialog = builder.show();
        }
    }

    private void setGridAdapter() {
        mMovieAdapter = new MoviePosterAdapter(getApplicationContext(),
                R.layout.grid_cell_layout, mMovieList);
        mPosterGrid.setAdapter(mMovieAdapter);
        mMovieAdapter.notifyDataSetChanged();
    }
}
