package com.nds.nanodegree.movieapp.model;

/**FavoriteMovieUpdateListener is interface, which will allow UI update based on DB operation.
 * Created by Namrata Shah on 4/4/2017.
 */

public interface FavoriteMovieUpdateListener {
    void onSuccess(int status);
    void onFailure();
}
