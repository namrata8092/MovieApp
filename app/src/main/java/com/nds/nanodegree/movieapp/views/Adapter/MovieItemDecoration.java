package com.nds.nanodegree.movieapp.views.Adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Namrata Shah on 3/26/2017.
 */

public class MovieItemDecoration extends RecyclerView.ItemDecoration {
    private int mItemOffset;

    public MovieItemDecoration(int offset){
        this.mItemOffset = offset;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        super.getItemOffsets(outRect, itemPosition, parent);
        outRect.set(mItemOffset, 0, mItemOffset,0);
    }
}
