package com.bench.android.core.app.fragment;


import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.android.library.R;
import com.bench.android.core.view.recyclerview.BaseAdapter;
import com.bench.android.core.view.recyclerview.ListController;
import com.bench.android.core.view.recyclerview.MultiTypeLayout;
import com.bench.android.core.view.recyclerview.OnRefreshListener;
import com.bench.android.core.view.recyclerview.PullRecyclerView;
import com.bench.android.core.util.bean.PaginatorBean;
import com.bench.android.core.net.http.base.RequestContainer;
import com.chad.library.adapter.base.BaseViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 适用于除了一个RecyclerView，其他啥都没有的fragment
 *
 * @author danao
 * @date 2019/2/13
 */
public abstract class BaseListFragment<T> extends BaseFragment implements OnRefreshListener {

    /**
     * 列表view(包括空白页面)
     */
    protected PullRecyclerView mRecyclerView;
    /**
     * 适配器
     */
    protected BaseAdapter<T> mAdapter;
    /**
     * 列表控制,分页器逻辑,空白页面显示,界面刷新
     */
    protected ListController mListController;

    /**
     * 默认布局，想换则重写
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void onCreateView() {
        mRecyclerView = (PullRecyclerView) rootView;
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = createAdapter();
        RecyclerView.ItemDecoration itemDecoration = createItemDecoration();
        if (itemDecoration != null) {
            mRecyclerView.addItemDecoration(itemDecoration);
        }
        mListController = new ListController<>(mRecyclerView, mAdapter, this);
        initData();
        mRecyclerView.startRefresh();
    }

    protected void initData() {
    }

    /**
     * 可复写,自己设定adapter
     * 如多条目使用BaseMultiItemQuickAdapter
     *
     * @return
     */
    protected BaseAdapter<T> createAdapter() {
        return new BaseAdapter<T>(getMultiTypeLayout()) {
            @Override
            public void onBindViewHolder(BaseViewHolder helper, T item) {
                BaseListFragment.this.onBindViewHolder(helper, item);
            }
        };
    }

    protected List<T> getDatas() {
        return mAdapter.getData();
    }

    public void setEnableLoadMore(boolean enable) {
        mAdapter.setEnableLoadMore(enable);
    }

    protected void addHeaderView(View headerView) {
        mAdapter.addHeaderView(headerView);
    }

    protected void addFooterView(View footerView) {
        mAdapter.addFooterView(footerView);
    }

    protected void setEmptyView(CharSequence charSequence, int imgResId) {
        mAdapter.setEmptyView(charSequence, imgResId, mRecyclerView.getRecyclerView());
    }


    /**
     * 数据加载完成
     *
     * @param list
     * @param mPaginator
     */
    protected void loadCompleted(List<T> list, PaginatorBean mPaginator) {
        mListController.loadComplete(list, mPaginator);
    }

    //如果需要多条目,复写此方法
    protected MultiTypeLayout<T> getMultiTypeLayout() {
        return new MultiTypeLayout<T>() {
            @Override
            protected int getItemResId(T o) {
                return getItemLayoutId();
            }
        };
    }

    protected RecyclerView.ItemDecoration createItemDecoration() {
        return null;
    }

    //单条目LayoutId
    protected abstract int getItemLayoutId();

    //数据绑定
    protected abstract void onBindViewHolder(BaseViewHolder helper, T item);

    public int getCurPage() {
        return mListController.getCurPage();
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) {
        mRecyclerView.setRefreshing(false);
        mActivity.showErrorMsg(jsonObject);
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) throws JSONException {
    }

    @Override
    public void onStartRequest() {
        if (!mRecyclerView.getSwipeRefreshLayout().isRefreshing() && !mAdapter.isLoading()) {
            showProgressDialog("数据加载中..");
        }
    }

    @Override
    public void onFinishRequest(int stateCode) {
        super.onFinishRequest(stateCode);
    }
}
