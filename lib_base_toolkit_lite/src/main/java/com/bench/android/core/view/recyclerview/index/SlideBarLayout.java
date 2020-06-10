package com.bench.android.core.view.recyclerview.index;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.library.R;
import com.bench.android.core.util.PingYinUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * SlideBar封装，用于快速创建联系人列表
 *
 * @author zhouyi
 */
public class SlideBarLayout extends FrameLayout {

    private final int TYPE_FIRST_ALPHABET = 100;
    private final int TYPE_CONTENT = 101;

    private RecyclerView mRecyclerView;
    private SlideBar mSlideBar;
    private TextView mDialogTv;
    private LinearLayoutManager mLinearLayoutManager;
    private MyMultiAdapter mAdapter;

    /**
     *
     */
    private int contentResId;

    private ISidebarViewConverter mViewConverter;


    public SlideBarLayout(Context context) {
        super(context);
        init();
    }

    public SlideBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlideBarLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_wrap_slidebar, this);
        mRecyclerView = findViewById(R.id.recyclerView);
        mSlideBar = findViewById(R.id.slideBar);
        mDialogTv = findViewById(R.id.tv_dialog);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    public void setData(List<? extends ISidebarContentGet> list, int resId, ISidebarViewConverter viewConverter) {
        contentResId = resId;
        mViewConverter = viewConverter;
        setAdapter(list);
    }

    private void setAdapter(List<? extends ISidebarContentGet> list) {

        final List<AlphabetBean> tempList = converterData(list);

        mSlideBar.setTextView(mDialogTv);

        mAdapter = new MyMultiAdapter(tempList);

        mAdapter.addItemType(TYPE_FIRST_ALPHABET, R.layout.base_item_first_alphabet);
        mAdapter.addItemType(TYPE_CONTENT, contentResId);

        mRecyclerView.setAdapter(mAdapter);

        mSlideBar.setOnIndexListener(new SlideBar.OnIndexListener() {
            @Override
            public void onIndex(String s) {
                List<AlphabetBean> adapterList = mAdapter.getData();
                for (int i = 0; i < adapterList.size(); i++) {
                    if (adapterList.get(i).content == null && adapterList.get(i).firstLetter.equals(s)) {
                        mLinearLayoutManager.scrollToPositionWithOffset(i, 0);
                        break;
                    }
                }
            }
        });
    }



    private List<AlphabetBean> converterData(List<? extends ISidebarContentGet> list) {
        if(list==null){
            return new ArrayList<>();
        }
        List<AlphabetBean> letterList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            AlphabetBean bean = new AlphabetBean();
            bean.setContent(list.get(i));
            bean.setFirstLetter(PingYinUtil.getInstance().getLetter(list.get(i).getContent()));
            letterList.add(bean);
        }

        //排序
        Collections.sort(letterList, new Comparator<AlphabetBean>() {
            @Override
            public int compare(AlphabetBean o1, AlphabetBean o2) {
                if (o1.firstLetter.equals("#")) {
                    return 1;
                } else if (o2.firstLetter.equals("#")) {
                    return -1;
                }
                return o1.firstLetter.compareTo(o2.firstLetter);
            }
        });

        //抽取字母
        List<String> firstLetterList = new ArrayList<>();
        for (AlphabetBean bean : letterList) {
            if (!firstLetterList.contains(bean.getFirstLetter())) {
                firstLetterList.add(bean.firstLetter);
            }
        }

        final List<AlphabetBean> tempList = new ArrayList<>();

        //先添加首个字母
        AlphabetBean bean = new AlphabetBean();
        bean.firstLetter = letterList.get(0).firstLetter;
        tempList.add(bean);

        for (int i = 0; i < letterList.size(); i++) {
            //添加内容
            tempList.add(letterList.get(i));

            //如果序列为最后一个的话，不用对比，因为后面已经没有了
            if (i == letterList.size() - 1) {
                break;
            }

            //如果当前首字母不等于下个内容首字母，则添加下个内容的首字母
            if (!letterList.get(i).firstLetter.equals(letterList.get(i + 1).firstLetter)) {
                bean = new AlphabetBean();
                bean.setFirstLetter(letterList.get(i + 1).firstLetter);
                tempList.add(bean);
            }
        }

        mSlideBar.setIndexText(firstLetterList);

        return tempList;
    }

    public void setOnItemClickListener(final OnSlideBarItemClickListener listener) {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<AlphabetBean> data = mAdapter.getData();
                AlphabetBean alphabetBean = data.get(position);
                if (alphabetBean.content != null) {
                    listener.onItemClick(alphabetBean.content);
                }
            }
        });
    }

    public void setNewData(List<? extends ISidebarContentGet> list) {
        mAdapter.setNewData(converterData(list));
    }

    public List<Object> getData() {
        List<Object> tempList = new ArrayList<>();
        for (AlphabetBean bean : mAdapter.getData()) {
            tempList.add(bean.content);
        }
        return tempList;
    }

    public interface OnSlideBarItemClickListener {

        void onItemClick(Object data);

    }

    class MyMultiAdapter extends BaseMultiItemQuickAdapter<AlphabetBean, BaseViewHolder> {


        public MyMultiAdapter(List<AlphabetBean> data) {
            super(data);
        }

        @Override
        protected void addItemType(int type, int layoutResId) {
            super.addItemType(type, layoutResId);
        }


        @Override
        protected void convert(BaseViewHolder helper, AlphabetBean item) {
            if (item.content == null) {
                helper.setText(R.id.alphabetTv, item.firstLetter);
            } else {
                mViewConverter.converter(helper, item.content);
            }
        }
    }

    private class AlphabetBean implements MultiItemEntity {

        Object content;
        String firstLetter;


        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public String getFirstLetter() {
            return firstLetter;
        }

        public void setFirstLetter(String firstLetter) {
            this.firstLetter = firstLetter;
        }

        @Override
        public int getItemType() {
            if (content == null) {
                return TYPE_FIRST_ALPHABET;
            }
            return TYPE_CONTENT;
        }
    }

}
