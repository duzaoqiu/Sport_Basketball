package com.bench.android.core.view.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

import com.android.library.R;
import com.bench.android.core.app.application.BaseApplication;
import com.bench.android.core.util.LogUtils;
import com.bench.android.core.util.dimension.PxConverter;
import com.bench.android.core.view.widget.TagView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Created by zaozao on 2018/7/9.
 */
public class TagFlowLayout extends MyFlowLayout implements OnDataChangeListener {
    private FlowLayoutAdapter mTagAdapter;

    private int mSelectedMax = -1;//-1为不限制数量

    private Set<Integer> mSelectedView = new HashSet<Integer>();

    private OnSelectListener mOnSelectListener;

    private OnTagClickListener mOnTagClickListener;

    private int itemTagMargin = 5;//

    private int mItemTagVerticalSpace, mItemTagHorizontalSpace = 5;

    //xml中配置的默认布局
    @LayoutRes
    int mTagViewResId;
    //默认文字布局的资源id
    @IdRes
    int mTagTextViewId;
    //tag是否能点击(默认可点击)
    boolean mTagIsClickable;
    //单选的时候是否可以切换
    boolean changeable;

    public FlowLayoutAdapter getAdapter() {
        return mTagAdapter;
    }

    private static final String KEY_CHOOSE_POS = "key_choose_pos";

    private static final String KEY_DEFAULT = "key_default";

    public TagFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFlowLayout(Context context) {
        this(context, null);
    }

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
        mSelectedMax = ta.getInt(R.styleable.TagFlowLayout_max_select, -1);
        mTagViewResId = ta.getResourceId(R.styleable.TagFlowLayout_tag_flow_view, -1);
        mTagTextViewId = ta.getResourceId(R.styleable.TagFlowLayout_tag_flow_text_view_id, -1);
        mTagIsClickable = ta.getBoolean(R.styleable.TagFlowLayout_tag_is_clickable, true);
        changeable = ta.getBoolean(R.styleable.TagFlowLayout_tag_is_changeable, true);
        ta.recycle();

    }

    @Override
    public void setAddLastOne(boolean addLastOne) {
        super.setAddLastOne(addLastOne);
    }

    //外面传进来的tag集合
    public void setTags(List<String> list) {
        if (mTagViewResId == -1 || mTagTextViewId == -1) {
            return;
        }
        if (list == null || list.size() == 0) {
            setVisibility(View.GONE);
        }
        setVisibility(View.VISIBLE);
        if (mTagAdapter == null) {
            mTagAdapter = new FlowLayoutAdapter<String>(list) {
                @Override
                public View getView(TagFlowLayout parent, int position, String str) {
                    View view = LayoutInflater.from(getContext()).inflate(mTagViewResId, parent, false);
                    ((TextView) view.findViewById(mTagTextViewId)).setText(str);
                    return view;
                }
            };
            mTagAdapter.setOnDataChangedListener(this);
            mSelectedView.clear();
            changeAdapter();
        }
        mTagAdapter.notifyDataChanged(list);
    }

    //外面设置Adapter
    public TagFlowLayout setLayoutAdapter(FlowLayoutAdapter adapter) {
        mTagAdapter = adapter;
        mTagAdapter.setOnDataChangedListener(this);
        mSelectedView.clear();
        changeAdapter();
        return this;
    }

    /***
     * 刷新view
     */
    public void notifySetDataChanged() {
        if (mTagAdapter != null) {
            mTagAdapter.notifyDataChanged();
        }
    }

    public void notifySetDataChanged(List list) {
        if (mTagAdapter != null) {
            mTagAdapter.notifyDataChanged(list);
        }
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        mOnTagClickListener = onTagClickListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            TagView tagView = (TagView) getChildAt(i);
            if (tagView.getVisibility() == View.GONE) {
                continue;
            }
            if (tagView.getTagView().getVisibility() == View.GONE) {
                tagView.setVisibility(View.GONE);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @SuppressWarnings("ResourceType")
    private void changeAdapter() {

        //edit by zhouyi 如果删除的count数为0，计算会错误
        if (mTagAdapter.getCount() == 0) {
            setVisibility(View.GONE);
            return;
        }

        removeAllViews();

        FlowLayoutAdapter adapter = mTagAdapter;

        TagView tagViewContainer = null;

        HashSet preCheckedList = mTagAdapter.getPreCheckedList();

        for (int i = 0; i < adapter.getCount(); i++) {

            View tagView = adapter.getView(this, i, adapter.getItem(i));

            tagViewContainer = new TagView(getContext());

            //向子view传递selected属性
            tagView.setDuplicateParentStateEnabled(true);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            tagView.setLayoutParams(lp);
            MarginLayoutParams lp1 = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp1.setMargins(PxConverter.dp2px(getContext(), mItemTagHorizontalSpace),
                    PxConverter.dp2px(getContext(), mItemTagVerticalSpace),
                    PxConverter.dp2px(getContext(), mItemTagHorizontalSpace),
                    PxConverter.dp2px(getContext(), 0));

            tagViewContainer.setLayoutParams(lp1);

            tagViewContainer.addView(tagView);
            addView(tagViewContainer);

            if (preCheckedList.contains(i)) {
                setChildChecked(i, tagViewContainer);
            }

            if (mTagAdapter.setSelected(i, adapter.getItem(i))) {
                setChildChecked(i, tagViewContainer);
            }

            tagView.setClickable(false);

            final TagView finalTagViewContainer = tagViewContainer;

            final int position = i;

            if (!mTagIsClickable) {
                continue;
            }

            tagViewContainer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    doSelect(finalTagViewContainer, position);
                    if (mOnTagClickListener != null) {
                        mOnTagClickListener.onTagClick(finalTagViewContainer, position,
                                TagFlowLayout.this);
                    }
                }
            });
        }
        mSelectedView.addAll(preCheckedList);
    }

    public void setMaxSelectCount(int count) {
        if (mSelectedView.size() > count) {
            LogUtils.e("kk", "you has already select more than " + count + " views , so it will be clear .");
            mSelectedView.clear();
        }
        mSelectedMax = count;
    }


    public TagFlowLayout setItemTagMargin(int margin) {
        mItemTagHorizontalSpace = mItemTagVerticalSpace = margin;
        return this;
    }

    /**
     * 纵向的间距设置
     *
     * @param itemTagVerticalSpace
     */
    public TagFlowLayout setTagVerticalSpace(int itemTagVerticalSpace) {
        this.mItemTagVerticalSpace = itemTagVerticalSpace;
        return this;
    }

    /**
     * 横向的间距设置
     *
     * @param itemTagHorizontalSpace
     */
    public TagFlowLayout setTagHorizontalSpace(int itemTagHorizontalSpace) {
        this.mItemTagHorizontalSpace = itemTagHorizontalSpace;
        return this;
    }

    /**
     * 选中selectedPos
     *
     * @param selectedPos
     * @return
     */
    public TagFlowLayout setSelected(int selectedPos) {
        if (selectedPos >= getChildCount() || selectedPos < 0) {
            return this;
        }
        doSelect((TagView) getChildAt(selectedPos), selectedPos);
        return this;
    }

    public Set<Integer> getSelectedList() {
        return new HashSet<Integer>(mSelectedView);
    }

    private void setChildChecked(int position, TagView view) {
        view.setChecked(true);
        mTagAdapter.onSelected(position, view.getTagView());
    }

    private void setChildUnChecked(int position, TagView view) {
        view.setChecked(false);
        mTagAdapter.unSelected(position, view.getTagView());
    }

    private void doSelect(TagView child, int position) {
        if (!child.isChecked()) {
            //处理max_select=1的情况，而且可以切换的时候
            if (mSelectedMax == 1 && mSelectedView.size() == 1 && changeable) {
                Iterator<Integer> iterator = mSelectedView.iterator();
                Integer preIndex = iterator.next();
                TagView pre = (TagView) getChildAt(preIndex);
                setChildUnChecked(preIndex, pre);
                setChildChecked(position, child);

                mSelectedView.remove(preIndex);
                mSelectedView.add(position);
            } else {
                if (mSelectedMax > 0 && mSelectedView.size() >= mSelectedMax) {
                    Toast.makeText(BaseApplication.getContext(), "您已经选过了哦", Toast.LENGTH_LONG).show();
                    return;
                }
                setChildChecked(position, child);
                mSelectedView.add(position);
            }
        } else {
            //mSelectedMax=1的时候如果当前点击的view已选中,不再取消选中
            if (mSelectedMax == 1 && mSelectedView.size() == 1 && child.isChecked()) {
                return;
            }

            setChildUnChecked(position, child);
            mSelectedView.remove(position);
        }
        if (mOnSelectListener != null) {
            mOnSelectListener.onSelected(new HashSet<Integer>(mSelectedView));
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DEFAULT, super.onSaveInstanceState());

        String selectPos = "";
        if (mSelectedView.size() > 0) {
            for (int key : mSelectedView) {
                selectPos += key + "|";
            }
            selectPos = selectPos.substring(0, selectPos.length() - 1);
        }
        bundle.putString(KEY_CHOOSE_POS, selectPos);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            String mSelectPos = bundle.getString(KEY_CHOOSE_POS);
            if (!TextUtils.isEmpty(mSelectPos)) {
                String[] split = mSelectPos.split("\\|");
                for (String pos : split) {
                    int index = Integer.parseInt(pos);
                    mSelectedView.add(index);

                    TagView tagView = (TagView) getChildAt(index);
                    if (tagView != null) {
                        setChildChecked(index, tagView);
                    }
                }
            }
            super.onRestoreInstanceState(bundle.getParcelable(KEY_DEFAULT));
            return;
        }
        super.onRestoreInstanceState(state);
    }


    @Override
    public void onChanged() {
        mSelectedView.clear();
        changeAdapter();
    }

    public interface OnSelectListener {
        void onSelected(Set<Integer> selectPosSet);
    }

    public interface OnTagClickListener {
        boolean onTagClick(View view, int position, MyFlowLayout parent);
    }
}
