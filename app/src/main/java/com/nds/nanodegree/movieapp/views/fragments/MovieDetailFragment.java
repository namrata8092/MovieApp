package com.nds.nanodegree.movieapp.views.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nds.nanodegree.movieapp.R;
import com.nds.nanodegree.movieapp.common.Constants;
import com.nds.nanodegree.movieapp.common.Util;
import com.nds.nanodegree.movieapp.model.FavoriteMovieUpdateListener;
import com.nds.nanodegree.movieapp.model.MovieModel;
import com.nds.nanodegree.movieapp.model.ReviewModel;
import com.nds.nanodegree.movieapp.model.TrailerModel;
import com.nds.nanodegree.movieapp.services.FetchMovieReviews;
import com.nds.nanodegree.movieapp.services.FetchMovieTrailers;
import com.nds.nanodegree.movieapp.services.UpdateFavoriteMovieToDB;
import com.nds.nanodegree.movieapp.views.Adapter.MovieReviewAdapter;
import com.nds.nanodegree.movieapp.views.Adapter.MovieTrailerAdapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Namrata Shah on 4/2/2017.
 */

public class MovieDetailFragment extends Fragment implements FavoriteMovieUpdateListener, View.OnClickListener {

    private MovieModel movieModel;
    private static int[] dimensions = new int[2];
    private TextView mAddToFavoriteTextView;
    private TextView mTrailerTextView;
    private TextView mReviewTextView;
    private RecyclerView mReviewRecyclerView;
    private SharedPreferences mSharedPreferences;

    public static MovieDetailFragment newInstance(MovieModel movieModel) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.SELECTED_MOVIE_DETAIL_BUNDLE_KEY, movieModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_detail_fragment, container, false);
        TextView mTitleTV = (TextView) view.findViewById(R.id.movieOriginalTitle);
        ImageView mThumbNailImageView = (ImageView) view.findViewById(R.id.movieThumbnail);
        TextView mReleaseDateTV = (TextView) view.findViewById(R.id.releaseDate);
        TextView mAverageRatingTV = (TextView) view.findViewById(R.id.averageRating);
        TextView mOverviewTV = (TextView) view.findViewById(R.id.overview);

        mAddToFavoriteTextView = (TextView) view.findViewById(R.id.addToFavorite);
        if(mSharedPreferences.getBoolean(movieModel.getID(), false)){
            mAddToFavoriteTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.favorite_enable, 0);
        }else{
            mAddToFavoriteTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.favorite_disable, 0);
        }
        mAddToFavoriteTextView.setOnClickListener(this);

        mReviewRecyclerView = (RecyclerView) view.findViewById(R.id.reviewList);

        mTrailerTextView = (TextView) view.findViewById(R.id.trailer);
        mTrailerTextView.setOnClickListener(this);

        mReviewTextView = (TextView) view.findViewById(R.id.review);
        mReviewTextView.setOnClickListener(this);

        mTitleTV.setText(movieModel.getOriginalTitle());

        mReleaseDateTV.setText(getResources().getString(R.string.release_date) + " " + movieModel.getReleaseDate());
        mAverageRatingTV.setText(getResources().getString(R.string.average_rating) + " " + movieModel.getVoteAverage());

        mOverviewTV.setText(movieModel.getOverview());
        dimensions = Util.getScreenDimensions(mThumbNailImageView.getContext());
        Glide.with(mThumbNailImageView.getContext())
                .load(Util.createImageURI(movieModel.getBackDropURL()))
                .override(dimensions[0], dimensions[1]).into(mThumbNailImageView);


        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arg = getArguments();
            movieModel = arg.getParcelable(Constants.SELECTED_MOVIE_DETAIL_BUNDLE_KEY);
        }

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.SELECTED_MOVIE_DETAIL_BUNDLE_KEY)) {
            movieModel = savedInstanceState.getParcelable(Constants.SELECTED_MOVIE_DETAIL_BUNDLE_KEY);
        } else {
            Intent callerIntent = getActivity().getIntent();
            if (callerIntent != null && callerIntent.hasExtra(Constants.SELECTED_MOVIE_DETAIL_BUNDLE_KEY)) {
                movieModel = callerIntent.getParcelableExtra(Constants.SELECTED_MOVIE_DETAIL_BUNDLE_KEY);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.SELECTED_MOVIE_DETAIL_BUNDLE_KEY, movieModel);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        dimensions = Util.getScreenDimensions(getActivity().getApplicationContext());
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onSuccess(final int status) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (status == Constants.ADDED_TO_FAVORITE) {
                    mAddToFavoriteTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.favorite_enable, 0);
                    mSharedPreferences.edit().putBoolean(movieModel.getID(), true).apply();
                } else {
                    mAddToFavoriteTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.favorite_disable, 0);
                    mSharedPreferences.edit().putBoolean(movieModel.getID(), false).apply();
                }
            }
        });
    }

    @Override
    public void onFailure() {
        Toast.makeText(getActivity(), "Can not add movie to favorite", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addToFavorite:
                UpdateFavoriteMovieToDB updateFavoriteMovieToDB = new UpdateFavoriteMovieToDB(getActivity(), movieModel, this);
                updateFavoriteMovieToDB.execute();
                break;
            case R.id.trailer:
                if (movieModel.getTrailers() == null || movieModel.getTrailers().size() == 0 || movieModel.getTrailers().isEmpty()) {
                    FetchMovieTrailers fetchMovieTrailers = null;
                    try {
                        fetchMovieTrailers = new FetchMovieTrailers( new URL(Util.buildMovieURL(Constants.TRAILER_URL, movieModel.getID())),
                                new FetchMovieTrailers.MovieTrailerResponse() {
                                    @Override
                                    public void getMovieTrailerResults(List<TrailerModel> result) {
                                        movieModel.setTrailers(result);
                                        setMovieTrailerList(result);

                                    }
                                });
                    } catch (MalformedURLException e) {
                    }
                    fetchMovieTrailers.execute();
                } else {
                    setMovieTrailerList(movieModel.getTrailers());
                }
                break;
            case R.id.review:
                if(movieModel.getReviews() == null || movieModel.getReviews().isEmpty() || movieModel.getReviews().size() == 0){
                    try {
                        FetchMovieReviews fetchMovieReviews = new FetchMovieReviews(new URL(Util.buildMovieURL(Constants.REVIEW_URL, movieModel.getID())),
                                new FetchMovieReviews.MovieReviewResponse() {
                            @Override
                            public void getMovieReviewResults(List<ReviewModel> result) {
                                movieModel.setReviews(result);
                                setMovieReviews(result);
                            }
                        });
                        fetchMovieReviews.execute();
                    } catch (MalformedURLException e) {
                    }
                }else{
                    setMovieReviews(movieModel.getReviews());
                }
                break;
        }
    }

    private void setMovieReviews(List<ReviewModel> reviews) {
        if(reviews == null || reviews.size() == 0){
            Util.createToastMsg(getActivity(),getString(R.string.no_review, movieModel.getTitle()));
            return;
        }
        mReviewRecyclerView.setVisibility(View.VISIBLE);
        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mReviewRecyclerView.setHasFixedSize(true);
        MovieReviewAdapter adapter = new MovieReviewAdapter(getContext(), reviews);
        mReviewRecyclerView.setAdapter(adapter);
    }

    private void setMovieTrailerList(final List<TrailerModel> result) {
        if(result == null || result.size() == 0){
            Util.createToastMsg(getActivity(),getString(R.string.no_trailer, movieModel.getTitle()));
            return;
        }
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.movie_trailer_popup, null);
        alertDialog.setView(convertView);
        final AlertDialog dialog = alertDialog.create();
        ListView trailerListView = (ListView) convertView.findViewById(R.id.trailerList);

        MovieTrailerAdapter adapter = new MovieTrailerAdapter(getContext(), R.layout.trailer_list_row, result);
        trailerListView.setAdapter(adapter);

        trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrailerModel model = result.get(position);
                Intent showTrailerIntent = new Intent();
                showTrailerIntent.setAction(Intent.ACTION_VIEW);
                showTrailerIntent.setData(Uri.parse(Constants.YOUTUBE_BASE_URL + model.getTrailerKey()));
                try {
                    startActivity(showTrailerIntent);
                    dialog.cancel();
                } catch (ActivityNotFoundException e) {
                    startActivity(showTrailerIntent);
                }
            }
        });

        dialog.show();
    }
}
