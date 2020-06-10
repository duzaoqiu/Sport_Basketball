package com.bench.android.core.app.dialog.holder;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.library.R;
import com.bench.android.core.app.dialog.DialogHelper;
import com.bench.android.core.view.recyclerview.MyDividerItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/************************************************************************
 *@Project: core
 *@Package_Name: com.bench.android.core.app.dialog
 *@Descriptions: 底部弹出的一种dialog的holder
 *@Author: xingjiu
 *@Date: 2019/8/20 
 *************************************************************************/
public class BottomDialogHolder extends ViewHolder {

    protected BottomDialogHolder(Context context) {
        super(context, R.layout.base_dialog_bottom);
    }

    protected BottomDialogHolder(Context context, int redId) {
        super(context, redId);
    }

    @Override
    public void init(final Dialog dialog, DialogHelper helper) {
        final DialogHelper.BottomDialogOptions options = (DialogHelper.BottomDialogOptions) helper.options;
        //进行相关View操作的回调
        if (!TextUtils.isEmpty(options.head)) {
            setText(R.id.header_tv, options.head);
            setViewVisibility(R.id.header_tv, View.VISIBLE);
        } else {
            setViewVisibility(R.id.header_tv, View.GONE);
        }
        if (!TextUtils.isEmpty(options.footer)) {
            setText(R.id.footer_tv, options.footer);
        }

        setOnClickListener(R.id.footer_tv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        RecyclerView rcvList = getView(R.id.rcv_list);
        rcvList.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        rcvList.addItemDecoration(new MyDividerItemDecoration(mContext, R.drawable.divider_horizontal_line));
        BottomDialogAdapter bottomMessageAdapter = new BottomDialogAdapter(options.list);
        bottomMessageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                dialog.dismiss();
                if (options.onItemClickListener != null) {
                    options.onItemClickListener.onItemClick(adapter, view, position);
                }
            }
        });
        rcvList.setAdapter(bottomMessageAdapter);

        //todo 这边计算如果列表数量大于5个的话，需要将列表高度设置为固定，但是有bug，后续修改
      /*  if (options.list.size() > 5) {
            ViewGroup.LayoutParams layoutParams = rcvList.getLayoutParams();
            layoutParams.height = PxConverter.dp2px(46) * 5;
            rcvList.requestLayout();
        }*/
    }

    class BottomDialogAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public BottomDialogAdapter(@Nullable List<String> data) {
            super(R.layout.item_dialog_bottom, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.tv_title, item);
        }
    }

}
