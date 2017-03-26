package com.nds.nanodegree.movieapp.views.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.nds.nanodegree.movieapp.R;
import com.nds.nanodegree.movieapp.common.Constants;
import com.nds.nanodegree.movieapp.common.Util;
import com.nds.nanodegree.movieapp.databinding.ActivityLauncherBinding;
import com.nds.nanodegree.movieapp.model.MovieModel;
import com.nds.nanodegree.movieapp.model.MovieSearchResult;
import com.nds.nanodegree.movieapp.services.FetchMovieData;
import com.nds.nanodegree.movieapp.views.Adapter.MovieItemDecoration;
import com.nds.nanodegree.movieapp.views.Adapter.MoviePosterAdapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Namrata Shah on 2/26/2017.
 */
public class LauncherActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private RecyclerView mPosterRecyclerView;
    private MoviePosterAdapter mMovieAdapter;
    private MovieSearchResult movieSearchResult;
    private List<MovieModel> mMovieList;
    private Context mSourceContext;
    private ProgressBar mMovieDataFetchProgressBar;
    private AlertDialog alertDialog;
    private ActivityLauncherBinding mActivityLauncherBinding;
    private GridLayoutManager mGridLayoutManager;
    private static final int CELL_WIDTH = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLauncherBinding = DataBindingUtil.setContentView(this, R.layout.activity_launcher);
        mPosterRecyclerView = mActivityLauncherBinding.moviePosterGrid;
        mMovieDataFetchProgressBar = mActivityLauncherBinding.movieFetchProgress;
        mSourceContext = this;

        mGridLayoutManager = new GridLayoutManager(getApplicationContext(), Util.calculateNoOfColumns(getApplicationContext(), CELL_WIDTH));
        mPosterRecyclerView.setLayoutManager(mGridLayoutManager);
        MovieItemDecoration itemDecoration = new MovieItemDecoration((int)getResources().getDimension(R.dimen.grid_offset));
        mPosterRecyclerView.addItemDecoration(itemDecoration);

        if(savedInstanceState != null && savedInstanceState.containsKey(Constants.MOVIE_LIST_BUNDLE_KEY)){
            movieSearchResult = savedInstanceState.getParcelable(Constants.MOVIE_LIST_BUNDLE_KEY);
            mMovieList = movieSearchResult.getMovieList();
            setGridAdapter();
        }else{
            fetchMovieData(mSourceContext, Constants.SEARCH_BY_POPULARITY);
        }

    }

    /**
     * This method will check for API KEY, if no API KEY, alert dialog will be shown to user.
     * Otherwise based on search criteria it will fetch movie results.
     * @param mSourceContext
     * @param searchBy
     */
    private void fetchMovieData(Context mSourceContext, int searchBy) {
        if(Util.isAPIKeyMissing(Constants.API_KEY)){
            displayAlertDialog(mSourceContext, getResources().getString(R.string.no_api_key));
        }else{
            fetchMovieDataFor(Util.buildMovieSearchURL(searchBy));
            switch(searchBy){
                case Constants.SEARCH_BY_POPULARITY :
                    setTitle(R.string.popular_movie);
                    break;
                case Constants.SEARCH_BY_RATING :
                    setTitle(R.string.movie_by_rating);
                    break;
                default :
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(alertDialog != null){
            alertDialog.dismiss();
            alertDialog = null;
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
                fetchMovieData(mSourceContext, Constants.SEARCH_BY_POPULARITY);
                break;
            case R.id.byRating :
                fetchMovieData(mSourceContext, Constants.SEARCH_BY_RATING);
                break;
            default :
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Display alert dialog when API KEY is not present.
     * @param mSourceContext
     */
    private void displayAlertDialog(Context mSourceContext, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(mSourceContext).setCancelable(false)
                .setMessage(msg).setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });
        alertDialog = builder.show();
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
            displayAlertDialog(mSourceContext, getResources().getString(R.string.no_internet_error));
        }
    }

    private void setGridAdapter() {
        mMovieAdapter = new MoviePosterAdapter(getApplicationContext(), mMovieList, this);
        mPosterRecyclerView.setAdapter(mMovieAdapter);
        mMovieAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MovieModel movie = mMovieList.get(position);
        displayDetailActivity(movie, mSourceContext);
    }
}
