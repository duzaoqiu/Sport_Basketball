package com.bench.android.core.view.emoji.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.library.R;
import com.bench.android.core.view.emoji.adapter.MenuMoreAdapter;
import com.bench.android.core.view.emoji.bean.MenuMoreItem;
import com.bench.android.core.view.emoji.listener.OnMenuItemMoreClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/************************************************************************
 *@Project: utils
 *@Package_Name: com.android.libEmoj.View.view
 *@Descriptions:显示网格 比如 图片 红包 ....
 *@Author: xingjiu
 *@Date: 2019/7/22 
 *************************************************************************/
public class MenuMoreView extends RecyclerView {

    private int spanCount = 5;
    private MenuMoreAdapter menuAdapter;
    private List<MenuMoreItem> menuMoreList = new ArrayList<>();
    private OnMenuItemMoreClickListener onMenuItemMoreClickListener;

    public MenuMoreView(Context context, List<MenuMoreItem> menuMoreList) {
        super(context);
        this.menuMoreList = menuMoreList;
    }

    public MenuMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuMoreView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void show() {
        if (getAdapter() == null) {
            setHasFixedSize(true);
            setLayoutManager(new GridLayoutManager(getContext(), spanCount));
            intMenuMoreList();
            menuAdapter = new MenuMoreAdapter(menuMoreList);
            setAdapter(menuAdapter);
            menuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if(onMenuItemMoreClickListener!=null){
                        onMenuItemMoreClickListener.onMenuMoreItemClick(position);
                    }
                }
            });
        }
        setVisibility(View.VISIBLE);
    }

    /**
     * 拍摄和照片的按钮
     */
    private void intMenuMoreList() {
        if (menuMoreList.size() == 0) {
            menuMoreList.add(new MenuMoreItem(getContext().getString(R.string.group_chat_menu_item_pic), R.drawable.icon_photo_gallery));
            menuMoreList.add(new MenuMoreItem(getContext().getString(R.string.group_chat_menu_item_take_photo), R.drawable.icon_take_photo));
        }
    }

    public void setMenuMoreList(List<MenuMoreItem> menuMoreList) {
        this.menuMoreList = menuMoreList;
    }

    public void setOnMenuMoreItemClickListener(OnMenuItemMoreClickListener onMenuItemMoreClickListener) {
        this.onMenuItemMoreClickListener = onMenuItemMoreClickListener;
    }

    public void updateImg(int position, int imgResId) {
        menuMoreList.get(position).setImgRes(imgResId);
        if(menuAdapter!=null) {
            menuAdapter.notifyDataSetChanged();
        }
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }
}
