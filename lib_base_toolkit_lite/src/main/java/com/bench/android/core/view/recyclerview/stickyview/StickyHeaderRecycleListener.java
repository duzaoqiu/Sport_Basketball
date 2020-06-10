package com.bench.android.core.view.recyclerview.stickyview;


import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/************************************************************************
 *@Project: utils
 *@Package_Name: com.android.library.tools.Utils
 *@Descriptions:
 *@Author: xingjiu
 *@Date: 2019/7/9 
 *************************************************************************/
public class StickyHeaderRecycleListener extends RecyclerView.OnScrollListener {

    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;

    private HeaderViewInitListener listener;
    private View header;

    public StickyHeaderRecycleListener(View header, HeaderViewInitListener listener) {
        this.listener = listener;
        this.header = header;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        View stickyInfoView = recyclerView.getChildAt(0);
        if (stickyInfoView != null && !TextUtils.isEmpty(stickyInfoView.getContentDescription())) {
            header.setVisibility(View.VISIBLE);
            if (listener != null) {
                listener.initView(stickyInfoView);
            }
        } else {
            header.setVisibility(View.GONE);
        }
        //位于headerView下方的itemView（该坐标是否在itemView内）
        View transInfoView = recyclerView.findChildViewUnder(header.getMeasuredWidth() >> 1
                , header.getMeasuredHeight() + 1);
        if (transInfoView != null && transInfoView.getTag() != null) {
            int tag = (int) transInfoView.getTag();
            int deltaY = transInfoView.getTop() - header.getMeasuredHeight();
            //当Item包含粘性头部一类时
            if (tag == HAS_STICKY_VIEW) {
                //当Item还未移动出顶部时
                if (transInfoView.getTop() > 0) {
                    header.setTranslationY(deltaY);
                } else {
                    //当Item移出顶部，粘性头部复原
                    header.setTranslationY(0);
                }
            } else {
                //当Item不包含粘性头部时
                header.setTranslationY(0);
            }
        }
    }

    public interface HeaderViewInitListener {
        /**
         * 初始化头部
         *
         * @param firstView
         */
        void initView(View firstView);
    }
}
