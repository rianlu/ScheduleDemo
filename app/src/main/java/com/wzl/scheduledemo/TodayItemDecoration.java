package com.wzl.scheduledemo;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class TodayItemDecoration extends RecyclerView.ItemDecoration {

    private int verticalSpaceHeight;
    public TodayItemDecoration(int verticalSpaceHeight){
        this.verticalSpaceHeight = verticalSpaceHeight;
    }
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = verticalSpaceHeight;
    }
}
