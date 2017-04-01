package com.nds.nanodegree.movieapp.views.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nds.nanodegree.movieapp.Converter.MovieSearchConverter;
import com.nds.nanodegree.movieapp.R;
import com.nds.nanodegree.movieapp.common.Constants;
import com.nds.nanodegree.movieapp.common.Util;
import com.nds.nanodegree.movieapp.databinding.ActivityLauncherBinding;
import com.nds.nanodegree.movieapp.dbo.MovieContract;
import com.nds.nanodegree.movieapp.model.MovieModel;
import com.nds.nanodegree.movieapp.model.MovieSearchResult;
import com.nds.nanodegree.movieapp.services.FetchMovieData;
import com.nds.nanodegree.movieapp.views.Adapter.MovieItemDecoration;
import com.nds.nanodegree.movieapp.views.Adapter.MoviePosterAdapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Namrata Shah on 2/26/2017.
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class LauncherActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
//    public class LauncherActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

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
    private static final int SEARCH_MOVIE_LOADER_ID = 10;
    private static final int FAVORITE_MOVIE_LOADER_ID = 20;
    private static final String URL_KEY = "url";
    LoaderManager.LoaderCallbacks<String> mMovieSearchLoaderListener;
    LoaderManager.LoaderCallbacks<Cursor> mFavoriteMovieFromDBLoaderListener;

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

        mMovieSearchLoaderListener = new LoaderManager.LoaderCallbacks<String>() {
            @Override
            public Loader<String> onCreateLoader(int id, final Bundle args) {


                return new AsyncTaskLoader<String>(getApplicationContext()) {
                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        if(args == null || args.isEmpty())
                            return;
                        displayProgressBar();
                    }

                    @Override
                    public String loadInBackground() {
                        try{
                            final String url = args.getString(URL_KEY);
                            if(url == null || TextUtils.isEmpty(url))
                                return null;
                            String movieSearchResult = Util.getResponseFromHttpURL(new URL(url));
                            return movieSearchResult;
                        }catch(IOException e){
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<String> loader, String data) {
                MovieSearchResult searchResult = MovieSearchConverter.getMovieSearchModel(data);
                if(searchResult != null && !searchResult.equals("")){
                    mMovieList = searchResult.getMovieList();
                    setGridAdapter();
                }else{
                    Toast.makeText(getApplicationContext(),"No Data fetch from loader", Toast.LENGTH_SHORT).show();
                }

                hideProgressBar();
            }

            @Override
            public void onLoaderReset(Loader<String> loader) {
                mMovieList = null;
                mMovieAdapter.notifyDataSetChanged();
            }
        };

        mFavoriteMovieFromDBLoaderListener = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<Cursor>(getApplicationContext()) {
                    Cursor favoriteMoviesCursor = null;
                    @Override
                    protected void onStartLoading() {
                        displayProgressBar();
                        if(favoriteMoviesCursor != null){
                            deliverResult(favoriteMoviesCursor);
                        }else{
                            forceLoad();
                        }
                    }

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public Cursor loadInBackground() {
                        try{
                            return getContentResolver().query(MovieContract.MovieEntry.DETAIL_URI,null, MovieContract.MovieEntry.COLUMN_NAME_MOVIE_FAVORITE, null, null, null);
//                    return getContentResolver().query(MovieContract.MovieEntry.DETAIL_URI,null, MovieContract.MovieEntry.COLUMN_NAME_MOVIE_FAVORITE, true, null, null, null);
                        }catch (Exception e){
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                mMovieList = getDataFromCursor(data);
                setGridAdapter();
                hideProgressBar();
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mMovieList = null;
                mMovieAdapter.notifyDataSetChanged();
            }
        };

        if(savedInstanceState != null && savedInstanceState.containsKey(Constants.MOVIE_LIST_BUNDLE_KEY)){
            movieSearchResult = savedInstanceState.getParcelable(Constants.MOVIE_LIST_BUNDLE_KEY);
        }else{
            fetchMovieDataUsingLoader(mMovieSearchLoaderListener, Constants.SEARCH_BY_POPULARITY);
        }

    }

    /**
     * display progressbar Whenever there is server call.
     */
    private void displayProgressBar() {
        mMovieDataFetchProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * hide progressbar after getting response from server
     * and data is parsed to model object.
     */
    private void hideProgressBar() {
        mMovieDataFetchProgressBar.setVisibility(View.GONE);
    }

    private void fetchMovieDataUsingLoader(LoaderManager.LoaderCallbacks<String> movieSearchLoaderListener, int searchBy) {
        if(Util.isAPIKeyMissing(Constants.API_KEY)){
            displayAlertDialog(mSourceContext, getResources().getString(R.string.no_api_key));
        }else if(!Util.isNetworkAvailable(getApplicationContext())){
            displayAlertDialog(mSourceContext, getResources().getString(R.string.no_internet_error));
        }else{
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> movieSearchLoader = loaderManager.getLoader(SEARCH_MOVIE_LOADER_ID);
            Bundle bundle = new Bundle();
            bundle.putString(URL_KEY,Util.buildMovieSearchURL(searchBy));

            if(movieSearchLoader == null){
                loaderManager.initLoader(SEARCH_MOVIE_LOADER_ID, bundle,movieSearchLoaderListener);
            }else{
                loaderManager.restartLoader(SEARCH_MOVIE_LOADER_ID, bundle,movieSearchLoaderListener);
            }

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
    protected void onResume() {
        super.onResume();
    }

    /**
     * This method will check for API KEY, if no API KEY, alert dialog will be shown to user.
     * Otherwise based on search criteria it will fetch movie results.
//     * @param mSourceContext
//     * @param searchBy
     */
//    private void fetchMovieData(Context mSourceContext, int searchBy) {
//        if(Util.isAPIKeyMissing(Constants.API_KEY)){
//            displayAlertDialog(mSourceContext, getResources().getString(R.string.no_api_key));
//        }else{
//            fetchMovieDataFor(Util.buildMovieSearchURL(searchBy));
//            switch(searchBy){
//                case Constants.SEARCH_BY_POPULARITY :
//                    setTitle(R.string.popular_movie);
//                    break;
//                case Constants.SEARCH_BY_RATING :
//                    setTitle(R.string.movie_by_rating);
//                    break;
//                default :
//            }
//        }
//    }

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
                fetchMovieDataUsingLoader(mMovieSearchLoaderListener, Constants.SEARCH_BY_POPULARITY);
//                fetchMovieData(mSourceContext, Constants.SEARCH_BY_POPULARITY);
                break;
            case R.id.byRating :
                fetchMovieDataUsingLoader(mMovieSearchLoaderListener, Constants.SEARCH_BY_RATING);
//                fetchMovieData(mSourceContext, Constants.SEARCH_BY_RATING);
                break;
            case R.id.byFavorite:
                fetchFavoriteMovies();
                break;
            default :
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * query database to get favorite movies
     * */
    private void fetchFavoriteMovies() {
        setTitle(R.string.favorite_movie);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> favoriteMovieLoader = loaderManager.getLoader(FAVORITE_MOVIE_LOADER_ID);
        if(favoriteMovieLoader == null){
            loaderManager.initLoader(FAVORITE_MOVIE_LOADER_ID, null, mFavoriteMovieFromDBLoaderListener);
        }else{
            loaderManager.restartLoader(FAVORITE_MOVIE_LOADER_ID, null, mFavoriteMovieFromDBLoaderListener);
        }
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
        if(mMovieList == null)
            return;
        mMovieAdapter = new MoviePosterAdapter(getApplicationContext(), mMovieList, this);
        mPosterRecyclerView.setAdapter(mMovieAdapter);
        mMovieAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MovieModel movie = mMovieList.get(position);
        displayDetailActivity(movie, mSourceContext);
    }

    private List<MovieModel> getDataFromCursor(Cursor data) {
        List<MovieModel> models = new ArrayList<>();
        int movieIDIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID);
        int movieTitleIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_TITLE);
        int moviePosterIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_POSTER_URL);
        int movieBackDropIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_BACKDROP_URL);
        int movieOverviewIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_OVERVIEW);
        int movieReleaseDateIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_RELEASE_DATE);
        int movieVoteAverageIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_VOTE_AVERAGE);

        if(data != null && data.getCount() > 0){
            try{
                for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
                    MovieModel model = new MovieModel(data.getString(movieIDIndex),
                            data.getString(movieTitleIndex), data.getString(moviePosterIndex));
                    model.setBackDropURL(data.getString(movieBackDropIndex));
                    model.setOverview(data.getString(movieOverviewIndex));
                    model.setReleaseDate(data.getString(movieReleaseDateIndex));
                    model.setVoteAverage(data.getString(movieVoteAverageIndex));
                    models.add(model);
                }
            }catch (Exception e){

            }finally {
                data.close();
            }
        }
        return models;
    }
}
