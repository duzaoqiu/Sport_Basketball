package com.bench.android.core.app.dialog.holder;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;

import com.bench.android.core.app.dialog.DialogHelper;


/**
 * Created by lingjiu on 2018/2/2.
 */

public class ViewHolder {
    private SparseArray<View> views;
    private View convertView;
    protected Context mContext;

    public ViewHolder(Context context, int layoutId) {
        convertView = LayoutInflater.from(context).inflate(layoutId, null);
        views = new SparseArray<>();
        this.mContext = context;
    }

    public ViewHolder(View view) {
        convertView = view;
        views = new SparseArray<>();
    }

    public static ViewHolder create(View view) {
        return new ViewHolder(view);
    }

    /**
     * 初始化view
     *
     * @param dialog Dialog对象
     */
    public void init(Dialog dialog, DialogHelper helper) {
    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }


    public View getConvertView() {
        return convertView;
    }

    public ViewHolder setText(int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    public ViewHolder setGone(int viewId, boolean gone) {
        if (gone) {
            getView(viewId).setVisibility(View.GONE);
        } else {
            getView(viewId).setVisibility(View.VISIBLE);
        }
        return this;
    }

    public ViewHolder setChecked(int viewId, boolean checked) {
        ((Checkable) getView(viewId)).setChecked(checked);
        return this;
    }

    public ViewHolder setText(int viewId, int textId) {
        TextView textView = getView(viewId);
        textView.setText(textId);
        return this;
    }

    public ViewHolder setTextColor(int viewId, int colorId) {
        TextView textView = getView(viewId);
        textView.setTextColor(colorId);
        return this;
    }

    public ViewHolder setTextGravity(int viewId, int gravity) {
        TextView textView = getView(viewId);
        textView.setGravity(gravity);
        return this;
    }

    public ViewHolder setOnClickListener(int viewId, View.OnClickListener clickListener) {
        if (clickListener != null) {
            View view = getView(viewId);
            view.setOnClickListener(clickListener);
        }
        return this;
    }

    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView imageView = getView(viewId);
        imageView.setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setBackgroundResource(int viewId, int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public ViewHolder setBackgroundDrawable(int viewId, Drawable drawable) {
        View view = getView(viewId);
        view.setBackgroundDrawable(drawable);
        return this;
    }

    public ViewHolder setBackgroundColor(int viewId, int colorId) {
        View view = getView(viewId);
        view.setBackgroundColor(colorId);
        return this;
    }

    public ViewHolder setViewVisibility(@IdRes int viewId, int visibility) {
        View view = getView(viewId);
        if (view != null) {
            view.setVisibility(visibility);
        }
        return this;
    }


    /**
     * picasso加载
     */
    public ViewHolder setImageByUrl(Context context, @IdRes int viewId, String path, @DrawableRes int placeHolder) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {

            if (TextUtils.isEmpty(path)) {
                imageView.setImageResource(placeHolder);
            } else {
                if (placeHolder != 0) {
                    imageView.setImageResource(placeHolder);
                }
                //PicUtils.load(context, imageView, path, placeHolder);
            }
        }
        return this;
    }


    public ViewHolder setImageByUrl(Context context, @IdRes int viewId, String path) {
        return setImageByUrl(context, viewId, path, 0);
    }


}
