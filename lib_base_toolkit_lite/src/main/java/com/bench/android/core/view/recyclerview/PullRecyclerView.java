package com.bench.android.core.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.library.R;

/**
 * 封装了下拉刷新的RecyclerView
 *@author danao on 2019/2/13.
 * <p>
 * 使用方法见文档
 */
public class PullRecyclerView extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private OnRefreshListener listener;

    private boolean mLoadMoreEnable = true;

    public PullRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public PullRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpView(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void setUpView(Context context) {
        View container = LayoutInflater.from(context).inflate(R.layout.layout_pull_recycler_view, this, true);
        mSwipeRefreshLayout = container.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = container.findViewById(R.id.recycleList);
        mRecyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.google_blue,
                R.color.google_green,
                R.color.google_red,
                R.color.google_yellow);
    }

    @Override
    public void onRefresh() {
        if (listener != null) {
            listener.onRefresh(true);
        }
    }

    public void startRefresh() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    /**
     * 设置是否可下拉刷新
     *
     * @param enable 是否可下拉
     */
    public void setRefreshEnable(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }

    public void setLoadMoreEnable(boolean enable) {
        this.mLoadMoreEnable = enable;
    }

    public boolean isLoadMoreEnable() {
        return mLoadMoreEnable;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration ItemDecoration) {
        mRecyclerView.addItemDecoration(ItemDecoration);
    }

    public void setRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public void setmSwipeRefreshLayout(SwipeRefreshLayout mSwipeRefreshLayout) {
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }

    public void setVerticalScrollBarGone(){
        mRecyclerView.setVerticalScrollBarEnabled(false);
    }
}
