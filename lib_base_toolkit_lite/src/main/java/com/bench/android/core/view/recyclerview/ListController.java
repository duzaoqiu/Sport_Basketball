package com.bench.android.core.view.recyclerview;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.library.R;
import com.bench.android.core.util.bean.PaginatorBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 页面中有PullRecyclerView的时候，使用此controller
 * 1、确定布局
 * 例如:一些基础的layoutResId
 * R.layout.base_list_layout; 列表
 * R.layout.base_title_list_layout;列表加"ActionBar"
 * 2、创建此控制器,关联PullRecyclerView,和adapter,onRefreshListener
 * PullRecyclerView可设置基础LayoutManager,如不设置,默认为LinearLayoutManager
 * BaseAdapter 可设置
 * addHeaderView、addFooterView、setEnableLoadMore、setEmptyView(不设置的话,这边有一个默认的EmptyView)
 * 3、onRefreshListener里面加载网络数据
 * 加载完成之后调用loadComplete即可
 * <p>
 * Created on 2019/2/27.
 * T: 列表数据类型
 * A: 适配器类型
 * 最常用 BaseAdapter,单条目,多条目
 * 拖拽,侧滑删除:BaseItemDraggableAdapter
 * 三级菜单:BaseMultiItemQuickAdapter结合AbstractExpandableItem
 *
 * @author danao
 */
public class ListController<T, A extends BaseQuickAdapter<T, BaseViewHolder>> {

    public final int DEFAULT_PAGE = 1;

    public final int PAGE_SIZE = 20;

    public PullRecyclerView mPullRecycle;

    public A mAdapter;

    private int mCurPage = DEFAULT_PAGE;

    private OnRefreshListener mOnRefreshListener;

    /**
     * @param pullRecyclerView
     * @param adapter
     * @param onRefreshListener 传null时不可下拉
     */
    public ListController(PullRecyclerView pullRecyclerView, A adapter, OnRefreshListener onRefreshListener) {
        mPullRecycle = pullRecyclerView;
        mAdapter = adapter;
        mPullRecycle.setAdapter(mAdapter);
        mOnRefreshListener = onRefreshListener;
        init();
    }

    private void init() {
        if (mPullRecycle.getRecyclerView().getLayoutManager() == null) {
            mPullRecycle.setLayoutManager(new LinearLayoutManager(mPullRecycle.getContext()));
        }
        if (mAdapter.getEmptyView() == null) {
            View defaultEmptyView = LayoutInflater.from(mPullRecycle.getContext()).inflate(R.layout.base_widget_empty_layout,
                    mPullRecycle.getRecyclerView(), false);
            mAdapter.setEmptyView(defaultEmptyView);
            mAdapter.isUseEmpty(false);
        }
        mAdapter.setHeaderFooterEmpty(true, true);
        if (mOnRefreshListener != null) {
            mPullRecycle.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(boolean refresh) {
                    resetPage();
                    mAdapter.setEnableLoadMore(false);
                    mOnRefreshListener.onRefresh(true);
                }
            });
            mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    mOnRefreshListener.onRefresh(false);
                }
            }, mPullRecycle.getRecyclerView());
        } else {
            mPullRecycle.setLoadMoreEnable(false);
            mPullRecycle.setRefreshEnable(false);
        }
    }

    public void loadComplete(List<T> list, PaginatorBean paginator) {
        if (list != null) {
            if (mCurPage == DEFAULT_PAGE) {
                mAdapter.isUseEmpty(true);
                mAdapter.setNewData(list);
                mPullRecycle.setRefreshing(false);
            } else {
                mAdapter.addData(list);
                mAdapter.loadMoreComplete();
            }
            mCurPage++;
        }else{
            if (mCurPage == DEFAULT_PAGE) {
                mAdapter.isUseEmpty(true);
                mPullRecycle.setRefreshing(false);
            } else {
                mAdapter.loadMoreComplete();
            }
        }
        //加载更多，不能注释掉
        if (paginator == null || paginator.getPage() >= paginator.getPages()) {
            mAdapter.loadMoreEnd(!mPullRecycle.isLoadMoreEnable());
        }

    }

    public void resetPage() {
        mCurPage = DEFAULT_PAGE;
    }

    public int getCurPage() {
        return mCurPage;
    }

    public void setEmptyView(CharSequence charSequence, int imgResId) {
        if (mAdapter == null) {
            throw new NullPointerException("you must init ListController first");
        }
        ImageView imageView = mAdapter.getEmptyView().findViewById(R.id.imageView);
        TextView textView = mAdapter.getEmptyView().findViewById(R.id.emptyTipTv);
        if (imageView != null && imgResId != 0) {
            imageView.setImageResource(imgResId);
        }
        if (textView != null && charSequence != null) {
            textView.setText(charSequence);
        }
    }
}
