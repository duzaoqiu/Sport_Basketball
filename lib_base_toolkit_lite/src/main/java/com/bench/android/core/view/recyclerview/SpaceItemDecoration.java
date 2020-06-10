package com.bench.android.core.view.recyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * author: wanshi
 * created on: 2019/3/21 下午5:10
 * description: 用于设置RecyclerView的行间距
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    //上间距
    private int mSpace;


    private int spanCount;

    private boolean isGridLayout;

    public SpaceItemDecoration(int space) {
        this.mSpace = space;
        isGridLayout = false;
    }


    public SpaceItemDecoration(int mSpace, int spanCount) {
        this.mSpace = mSpace;
        this.spanCount = spanCount;
        isGridLayout = true;
    }


    /**
     * Retrieve any offsets for the given item. Each field of <code>outRect</code> specifies
     * the number of pixels that the item view should be inset by, similar to padding or margin.
     * The default implementation sets the bounds of outRect to 0 and returns.
     * <p>
     * <p>
     * If this ItemDecoration does not affect the positioning of item views, it should set
     * all four fields of <code>outRect</code> (left, top, right, bottom) to zero
     * before returning.
     * <p>
     * <p>
     * If you need to access Adapter for additional data, you can call
     * {@link RecyclerView#getChildAdapterPosition(View)} to get the adapter position of the
     * View.
     *
     * @param outRect Rect to receive the output.
     * @param view    The child view to decorate
     * @param parent  RecyclerView this ItemDecoration is decorating
     * @param state   The current state of RecyclerView.
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position

        if (isGridLayout) {
            if (position >= spanCount) {
                outRect.top = mSpace;
            }
            if (position % spanCount != 0) {
                outRect.left = mSpace;
            }
        } else {
            if (position != 0) {
                outRect.top = mSpace;
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

    }
}
