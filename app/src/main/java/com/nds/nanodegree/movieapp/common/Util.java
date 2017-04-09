package com.nds.nanodegree.movieapp.common;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**Util class for all different utilities like
 * 1. Network availability
 * 2. Device screen dimensions
 * 3. Building appropriate URLs
 */

/**
 * Created by Namrata Shah on 2/26/2017.
 */
public final class Util {

    public Util() {
    }

    /**
     * Check whether device has valid internet connection or not.
     * @param mContext
     * @return true if internet connection is available else returns false.
     */
    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return isWifiConnected(networkInfo) || isMobileConnected(networkInfo);
    }

    /**
     * Check whether device has valid wifi connectivity on device.
     * @param networkInfo
     * @return true if wifi connection is available else returns false.
     */
    public static boolean isWifiConnected(NetworkInfo networkInfo) {
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                && (networkInfo.isConnected() || networkInfo.isConnectedOrConnecting())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check whether device has valid mobile data connectivity on device.
     * @param networkInfo
     * @return true if mobile data is available else returns false.
     */
    public static boolean isMobileConnected(NetworkInfo networkInfo) {
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE
                && (networkInfo.isConnected() || networkInfo.isConnectedOrConnecting())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *This method opens HTTP connection to fetch movie data from TMDB server.
     * @param url
     * @return Search result as string data.
     * @throws IOException
     */
    public static String getResponseFromHttpURL(URL url) throws IOException{
        HttpURLConnection httpUrlConnection = (HttpURLConnection)url.openConnection();
        try{
            InputStream in = httpUrlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean isNext = scanner.hasNext();
            if(isNext){
                return scanner.next();
            }else{
                return null;
            }
        }finally {
            httpUrlConnection.disconnect();
        }
    }

    /**
     * Build URI to download images from TMDB server
     * @param posterURL
     * @return URI as string
     */
    public static String createImageURI(String posterURL) {
        Uri uri = Uri.parse(Constants.MOVIE_IMAGE_BASE_URL).buildUpon().
                appendPath(Constants.DEVICE_SIZE).appendEncodedPath(posterURL).build();
        return uri.toString();
    }

    /**
     * Builds movie search URL based on selection.
     * @return search URL as string
     */
    public static String buildMovieURL(int selection){
        switch(selection){
            case Constants.SEARCH_BY_POPULARITY :
                Uri popularUri = Uri.parse(Constants.MOVIE_SEARCH_BY_POPULAR).buildUpon().
                        appendQueryParameter(Constants.QUERY_PARAM_API_KEY, Constants.API_KEY).build();
                return popularUri.toString();
            case Constants.SEARCH_BY_RATING:
                Uri ratingUri = Uri.parse(Constants.MOVIE_SEARCH_BY_TOP_RATED).buildUpon().
                        appendQueryParameter(Constants.QUERY_PARAM_API_KEY, Constants.API_KEY).build();
                return ratingUri.toString();
            default:
                break;
        }
        return null;
    }

    /**
     * Builds movie search URL based on selection.
     * @return search URL as string
     */
    public static String buildMovieURL(int selection, String movieID){
        switch(selection){
            case Constants.TRAILER_URL :
                Uri.Builder trailerUri = Uri.parse(Constants.MOVIE_BASE_URL).buildUpon();
                trailerUri.appendPath(Constants.MOVIE_KEY).
                        appendPath(String.valueOf(movieID)).
                        appendPath(Constants.VIDEO_KEY).
                        appendQueryParameter(Constants.QUERY_PARAM_API_KEY, Constants.API_KEY).build();
                return trailerUri.toString();
            case Constants.REVIEW_URL :
                Uri.Builder reviewUri = Uri.parse(Constants.MOVIE_BASE_URL).buildUpon();
                reviewUri.appendPath(Constants.MOVIE_KEY).
                        appendPath(String.valueOf(movieID)).
                        appendPath(Constants.REVIEW_KEY).
                        appendQueryParameter(Constants.QUERY_PARAM_API_KEY, Constants.API_KEY).build();
                return reviewUri.toString();
        }
        return null;
    }

    /**
     * Method to check if API KEY is not empty or null.
     * returns true when key is missing, otherwise false.
     * @param API_KEY
     * @return
     */
    public static boolean isAPIKeyMissing(String API_KEY){
        return API_KEY == null || TextUtils.isEmpty(API_KEY);
    }

    /**
     * Retrieve device screen dimensions
     * @param context
     * @return screen width & height based on orientation, as array of integer.
     */
    public static int[] getScreenDimensions(Context context){
        DisplayMetrics displayMetrics = (DisplayMetrics)context.getResources().getDisplayMetrics();
        int[] dimensions;
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            dimensions = new int[]{displayMetrics.widthPixels /4 , displayMetrics.heightPixels / 2};
        }else{
            dimensions = new int[]{displayMetrics.widthPixels / 2, displayMetrics.heightPixels / 4};
        }
        return dimensions;
    }

    public static int calculateNoOfColumns(Context applicationContext, int CELL_WIDTH) {
        DisplayMetrics displayMetric = applicationContext.getResources().getDisplayMetrics();
        float deviceWidth = displayMetric.widthPixels/displayMetric.density;
        return (int) deviceWidth/CELL_WIDTH;
    }

    public static void createToastMsg(Context cnt, String msg){
        if(cnt != null)
        Toast.makeText(cnt, msg, Toast.LENGTH_SHORT).show();
    }
}
