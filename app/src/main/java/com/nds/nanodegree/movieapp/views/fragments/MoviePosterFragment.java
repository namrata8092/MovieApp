package com.nds.nanodegree.movieapp.views.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nds.nanodegree.movieapp.Converter.MovieSearchConverter;
import com.nds.nanodegree.movieapp.R;
import com.nds.nanodegree.movieapp.common.Constants;
import com.nds.nanodegree.movieapp.common.Util;
import com.nds.nanodegree.movieapp.dbo.MovieContract;
import com.nds.nanodegree.movieapp.model.MovieModel;
import com.nds.nanodegree.movieapp.model.MovieSearchResult;
import com.nds.nanodegree.movieapp.services.FetchMovieData;
import com.nds.nanodegree.movieapp.views.Activities.MovieDetailActivity;
import com.nds.nanodegree.movieapp.views.Adapter.MovieItemDecoration;
import com.nds.nanodegree.movieapp.views.Adapter.MoviePosterAdapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**MoviePosterFragment will display Movie Name & movie poster based on selection.
 * Created by Namrata Shah on 4/2/2017.
 */

public class MoviePosterFragment extends Fragment implements AdapterView.OnItemClickListener{
        private RecyclerView mPosterRecyclerView;
        private MoviePosterAdapter mMovieAdapter;
        private MovieSearchResult movieSearchResult;
        private List<MovieModel> mMovieList;
        private Context mSourceContext;
        private ProgressBar mMovieDataFetchProgressBar;
        private AlertDialog alertDialog;
        private GridLayoutManager mGridLayoutManager;
        private static final int CELL_WIDTH = 150;
        private static final int SEARCH_MOVIE_LOADER_ID = 10;
        private static final int FAVORITE_MOVIE_LOADER_ID = 20;
        private static final String URL_KEY = "url";
        LoaderManager.LoaderCallbacks<String> mMovieSearchLoaderListener;
        LoaderManager.LoaderCallbacks<Cursor> mFavoriteMovieFromDBLoaderListener;
        private boolean mTwoPanelLayout = false;
        private SharedPreferences mMoviePreferences;
        private String mMovieSortChoice;

    private static final String[] FAV_MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_NAME_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ORIGINAL_TITLE,
            MovieContract.MovieEntry.COLUMN_NAME_MOVIE_POSTER_URL,
            MovieContract.MovieEntry.COLUMN_NAME_MOVIE_BACKDROP_URL,
            MovieContract.MovieEntry.COLUMN_NAME_MOVIE_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_NAME_MOVIE_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_NAME_MOVIE_RELEASE_DATE
    };

    public static MoviePosterFragment newInstance() {
        MoviePosterFragment fragment = new MoviePosterFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_launcher_fragment, container, false);

        mPosterRecyclerView = (RecyclerView) view.findViewById(R.id.moviePosterGrid);
        mMovieDataFetchProgressBar = (ProgressBar) view.findViewById(R.id.movieFetchProgress);

        mGridLayoutManager = new GridLayoutManager(mSourceContext, Util.calculateNoOfColumns(mSourceContext, CELL_WIDTH));
        mPosterRecyclerView.setLayoutManager(mGridLayoutManager);

        MovieItemDecoration itemDecoration = new MovieItemDecoration((int)getResources().getDimension(R.dimen.grid_offset));
        mPosterRecyclerView.addItemDecoration(itemDecoration);

        if(getActivity().findViewById(R.id.movieDetailContainer) != null){
            mTwoPanelLayout = true;
        }
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSourceContext = getActivity().getApplicationContext();
        mMoviePreferences = PreferenceManager.getDefaultSharedPreferences(mSourceContext);

        mMovieSearchLoaderListener = new LoaderManager.LoaderCallbacks<String>() {
            @Override
            public Loader<String> onCreateLoader(int id, final Bundle args) {
                return new AsyncTaskLoader<String>(mSourceContext) {
                    private String mData = null;
                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        if(args == null || args.isEmpty())
                            return;
                        displayProgressBar();
                        if(mData == null)
                            forceLoad();
                    }

                    @Override
                    public String loadInBackground() {
                        try{
                            final String url = args.getString(URL_KEY);
                            if(url == null || TextUtils.isEmpty(url))
                                return null;
                            String movieSearchResult = Util.getResponseFromHttpURL(new URL(url));
                            mData = movieSearchResult;
                            return movieSearchResult;
                        }catch(IOException e){
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<String> loader, String data) {
                if(data != null){
                    MovieSearchResult searchResult = MovieSearchConverter.getMovieSearchModel(data);
                    if(searchResult != null && !searchResult.equals("")){
                        mMovieList = searchResult.getMovieList();
                        setGridAdapter();
                    }
                }else{
                    Toast.makeText(mSourceContext,"No Data fetch from loader", Toast.LENGTH_SHORT).show();
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
                return new AsyncTaskLoader<Cursor>(mSourceContext) {
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
                            return mSourceContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,FAV_MOVIE_COLUMNS, null, null, null);
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

        if(savedInstanceState == null || !savedInstanceState.containsKey(Constants.MOVIE_SORT_CHOICE_BUNDLE_KEY) || mMovieList == null){
            mMovieSortChoice = mMoviePreferences.getString(getString(R.string.movie_pref_sort_key),"");
            mMovieList = new ArrayList<>();
            refreshMovieListWithNewChoice(mMovieSortChoice, true);
        }else{
            mMovieSortChoice = savedInstanceState.getString(Constants.MOVIE_SORT_CHOICE_BUNDLE_KEY);
            mMovieList = savedInstanceState.getParcelableArrayList(Constants.MOVIE_LIST_BUNDLE_KEY);

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        String savedMoviePreference = mMoviePreferences.getString(getString(R.string.movie_pref_sort_key),"");
        if(!mMovieSortChoice.equals(savedMoviePreference) || mMovieList == null ){
            mMovieSortChoice = savedMoviePreference;
            refreshMovieListWithNewChoice(mMovieSortChoice, true);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.MOVIE_SORT_CHOICE_BUNDLE_KEY, mMovieSortChoice);
        outState.putParcelableArrayList(Constants.MOVIE_LIST_BUNDLE_KEY, (ArrayList<? extends Parcelable>) mMovieList);
        super.onSaveInstanceState(outState);

    }

    private void refreshMovieListWithNewChoice(String mMovieSearchPreference, boolean fetchMovies) {
        if(fetchMovies){
            int choice = identifySearchTypeFromPreference(mMovieSearchPreference);
            switch(choice) {
                case Constants.SEARCH_BY_POPULARITY:
                case Constants.SEARCH_BY_RATING:
                    fetchMovieDataUsingLoader(mMovieSearchLoaderListener, choice);
                    break;
                case Constants.SEARCH_BY_FAVORITE:
                    fetchFavoriteMovies();
                    break;
                default:
            }
        }else{
            return;
        }

    }

    private int identifySearchTypeFromPreference(String preference){
        if(preference.equals(getString(R.string.preference_top_rated_movie_key)))
            return Constants.SEARCH_BY_RATING;
        else if(preference.equals(getString(R.string.preference_favorite_movie_key)))
            return Constants.SEARCH_BY_FAVORITE;
        else
            return Constants.SEARCH_BY_POPULARITY;
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

    /**
     * This method will fetch movie data using TMDB API & updated adapter data.
     * It accepts URL as input.
     * If there no internet connection it will show alertdialog.
     * @param URL
     */

    private void fetchMovieDataUsingLoader(LoaderManager.LoaderCallbacks<String> movieSearchLoaderListener, int searchBy) {
        if(Util.isAPIKeyMissing(Constants.API_KEY)){
            displayAlertDialog(mSourceContext, getResources().getString(R.string.no_api_key));
        }else if(!Util.isNetworkAvailable(mSourceContext)){
            displayAlertDialog(mSourceContext, getResources().getString(R.string.no_internet_error));
        }else{
            LoaderManager loaderManager = getLoaderManager();
            Loader<String> movieSearchLoader = loaderManager.getLoader(SEARCH_MOVIE_LOADER_ID);
            Bundle bundle = new Bundle();
            bundle.putString(URL_KEY,Util.buildMovieURL(searchBy));

            if(movieSearchLoader == null){
                loaderManager.initLoader(SEARCH_MOVIE_LOADER_ID, bundle,movieSearchLoaderListener);
            }else{
                loaderManager.restartLoader(SEARCH_MOVIE_LOADER_ID, bundle,movieSearchLoaderListener);
            }

            switch(searchBy){
                case Constants.SEARCH_BY_POPULARITY :
                    getActivity().setTitle(R.string.popular_movie);
                    break;
                case Constants.SEARCH_BY_RATING :
                    getActivity().setTitle(R.string.movie_by_rating);
                    break;
                default :
            }
        }
    }


    @Override
    public void onDestroy() {
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
    private void displayDetailActivity(MovieModel movie, Context sourceContext, boolean mTwoPanelLayout) {
        if(mTwoPanelLayout){
            MovieDetailFragment detailFragment = MovieDetailFragment.newInstance(movie);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.movieDetailContainer,
                    detailFragment, Constants.MOVIE_DETAIL_FRAGMENT_TAG).commit();
        }else{
            Class destinationClass = MovieDetailActivity.class;
            Intent movieDetailActivity = new Intent(sourceContext, destinationClass);
            movieDetailActivity.putExtra(Constants.SELECTED_MOVIE_DETAIL_BUNDLE_KEY,movie);
            startActivity(movieDetailActivity);
        }
    }


    /**
     * query database to get favorite movies
     * */
    private void fetchFavoriteMovies() {
        getActivity().setTitle(R.string.favorite_movie);
        LoaderManager loaderManager = getLoaderManager();
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
                        getActivity().finish();
                    }
                });
        alertDialog = builder.show();
    }


    private void fetchMovieDataFor(String URL){
        if(Util.isNetworkAvailable(mSourceContext)){
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
            }
        }else{
            displayAlertDialog(mSourceContext, getResources().getString(R.string.no_internet_error));
        }
    }

    private void setGridAdapter() {
        if(mMovieList == null)
            return;
        mMovieAdapter = new MoviePosterAdapter(mSourceContext, mMovieList, this);
        mPosterRecyclerView.setAdapter(mMovieAdapter);
        mMovieAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MovieModel movie = mMovieList.get(position);
        displayDetailActivity(movie, mSourceContext, mTwoPanelLayout);
    }

    private List<MovieModel> getDataFromCursor(Cursor data) {
        List<MovieModel> models = new ArrayList<>();
        int movieIDIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID);
        int movieTitleIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_TITLE);
        int movieOriginalTitleIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ORIGINAL_TITLE);
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
                    model.setOriginalTitle(data.getString(movieOriginalTitleIndex));
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

