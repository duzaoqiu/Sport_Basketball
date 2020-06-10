package com.bench.android.core.app.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.android.library.R;
import com.bench.android.core.app.dialog.holder.ViewHolder;
import com.bench.android.core.util.LibAppUtil;
import com.bench.android.core.util.StatusBarCompat;
import com.bench.android.core.util.dimension.PxConverter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


/**
 * 可设置属性值
 * MARGIN
 * WIDTH
 * HEIGHT
 * DIM
 * BOTTOM
 * CANCEL
 * ANIM
 * LAYOUT
 * Created by lingjiu on 2018/2/2.
 */
public abstract class BaseDialog extends Dialog {

    private int margin;//左右边距
    private int verticalMargin;//距下边距
    private int width = -2;//宽度,默认wrap_content
    private int height = -2;//高度,默认wrap_content
    private float dimAmount = 0.5f;//灰度深浅
    private boolean showBottom;//是否底部显示
    private boolean outCancel = true;//是否点击外部取消
    private int x, y;//dialog弹出坐标
    @StyleRes
    private int animStyle;//弹出动画
    @LayoutRes
    protected int layoutId;//布局id
    protected Context mContext;
    private boolean isCoverPx = true;//宽高是否需要由dp转化为px
    private int gravity = Gravity.NO_GRAVITY;
    private OnDismissListener onDismissListener;

    public abstract int intLayoutId();

    public abstract void convertView(ViewHolder holder, BaseDialog dialog);

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.dialog_base);
        layoutId = intLayoutId();
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(getContext(), layoutId, null);
        convertView(ViewHolder.create(view), this);
        setContentView(wrapInBottomSheet(0, view, null));
    }

    @Override
    protected void onStart() {
        super.onStart();
        initParams();
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        //隐藏导航栏,以防止部分手机,导航栏被灰色遮罩遮挡看不清的问题
//        StatusBarCompat.hideNavigationBarUI(getWindow());
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void initParams() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount = dimAmount;
            //是否在底部显示
            if (showBottom || isSupportBottomSheet) {
                lp.gravity = Gravity.BOTTOM;
                if (animStyle == 0) {
                    animStyle = R.style.dialog_anim_bottom;
                }
                lp.y = PxConverter.dp2px(mContext, verticalMargin);
            } else {
                lp.gravity = gravity;
            }
            //设置dialog宽度
            if (width == -1 || isSupportBottomSheet) {
                lp.width = LibAppUtil.getScreenWidth(mContext) - 2 * PxConverter.dp2px(mContext, margin);
            } else if (width == -2) {
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            } else {
                lp.width = isCoverPx ? PxConverter.dp2px(mContext, width) : width;
            }

            //设置dialog高度
            if (height == -2) {
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            } else {
                lp.height = isCoverPx ? PxConverter.dp2px(getContext(), height) : height;
            }

            if (x != 0) {
                lp.x = x;
                lp.gravity = lp.gravity | Gravity.LEFT;
            }
            if (y != 0) {
                lp.y = y;
                lp.gravity = lp.gravity | Gravity.TOP;
            }

            if (onDismissListener != null) {
                setOnDismissListener(onDismissListener);
            }

            //设置dialog进入、退出的动画
            window.setWindowAnimations(animStyle);
            window.setAttributes(lp);
        }
        setCancelable(outCancel);
        setCanceledOnTouchOutside(outCancel);
    }

    /***
     *
     * @param margin  dp值
     * @return
     */
    public BaseDialog setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    //距上边距
    public BaseDialog setVerticalMargin(int margin) {
        this.verticalMargin = margin;
        return this;
    }


    public BaseDialog setWidth(int width) {
        this.width = width;
        return this;
    }

    public BaseDialog setHeight(int height) {
        this.height = height;
        return this;
    }

    public BaseDialog setX(int x) {
        this.x = x;
        return this;
    }

    public BaseDialog setY(int y) {
        this.y = y;
        return this;
    }

    public void updateY(int y) {
        if (this.y == y)
            return;
        this.y = y;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.y = y;
        onWindowAttributesChanged(lp);
    }

    public void updateX(int x) {
        if (this.x == x)
            return;
        this.x = x;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.x = x;
        onWindowAttributesChanged(lp);
    }

    public BaseDialog setIsNeedCoverPx(boolean coverPx) {
        this.isCoverPx = coverPx;
        return this;
    }

    /**
     * dialog灰度深浅
     *
     * @param dimAmount 0-1 (0代表无灰色背景)
     * @return
     */
    public BaseDialog setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    public BaseDialog setShowBottom(boolean showBottom) {
        this.showBottom = showBottom;
        return this;
    }

    public BaseDialog setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public BaseDialog setDismissListener(OnDismissListener listener) {
        this.onDismissListener = listener;
        return this;
    }

    public BaseDialog setCanBeCancel(boolean cancelable) {
        setCancelable(cancelable);
        setCanceledOnTouchOutside(cancelable);
        return this;
    }

    /**
     * @param outCancel 是否点击外部取消 默认true
     * @return
     */
    public BaseDialog setOutCancel(boolean outCancel) {
        this.outCancel = outCancel;
        return this;
    }

    public BaseDialog setAnimStyle(@StyleRes int animStyle) {
        this.animStyle = animStyle;
        return this;
    }

    /**
     * 如果复写的话必须设置在super.onCreate()之前
     *
     * @param isSupportBottomSheet
     * @return
     */
    public BaseDialog setSupportBottomSheet(boolean isSupportBottomSheet) {
        this.isSupportBottomSheet = isSupportBottomSheet;
        return this;
    }

    public BaseDialog setPeekHeight(int peekHeight) {
        this.peekHeight = peekHeight;
        if (mBehavior != null)
            mBehavior.setPeekHeight(PxConverter.dp2px(getContext(), peekHeight));
        return this;
    }


    @Override
    public void show() {
        super.show();
        Activity activity = (Activity) mContext;
        if (!activity.isFinishing()) {
            super.show();
        }
    }

    /*************************以下是为了得到BottomSheet新增的内容************************************/
    protected BottomSheetBehavior<FrameLayout> mBehavior;
    boolean mCancelable = true;
    private boolean mCanceledOnTouchOutside = true;
    private boolean mCanceledOnTouchOutsideSet;
    private boolean isSupportBottomSheet;
    private int peekHeight = 200;

    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        if (mCancelable != cancelable) {
            mCancelable = cancelable;
            if (mBehavior != null) {
                mBehavior.setHideable(cancelable);
            }
        }
    }


    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        if (cancel && !mCancelable) {
            mCancelable = true;
        }
        mCanceledOnTouchOutside = cancel;
        mCanceledOnTouchOutsideSet = true;
    }


    private View wrapInBottomSheet(int layoutResId, View view, ViewGroup.LayoutParams params) {
        if (!isSupportBottomSheet)
            return view;
        final View container = View.inflate(getContext(),
                R.layout.design_bottom_sheet_dialog, null);
        /*final CoordinatorLayout coordinator =
                (CoordinatorLayout) container.findViewById(android.support.design.R.id.coordinator);
        if (layoutResId != 0 && view == null) {
            view = getLayoutInflater().inflate(layoutResId, coordinator, false);
        }*/
        FrameLayout bottomSheet = container.findViewById(R.id.design_bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottomSheet);
        mBehavior.setBottomSheetCallback(mBottomSheetCallback);
        mBehavior.setHideable(mCancelable);
        mBehavior.setPeekHeight(PxConverter.dp2px(getContext(), peekHeight));
        if (params == null) {
            bottomSheet.addView(view);
        } else {
            bottomSheet.addView(view, params);
        }
        // We treat the CoordinatorLayout as outside the dialog though it is technically inside
        container.findViewById(R.id.touch_outside).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCancelable && isShowing() && shouldWindowCloseOnTouchOutside()) {
                    cancel();
                }
            }
        });
        // Handle accessibility events
        ViewCompat.setAccessibilityDelegate(bottomSheet, new AccessibilityDelegateCompat() {
            @Override
            public void onInitializeAccessibilityNodeInfo(View host,
                                                          AccessibilityNodeInfoCompat info) {
                super.onInitializeAccessibilityNodeInfo(host, info);
                if (mCancelable) {
                    info.addAction(AccessibilityNodeInfoCompat.ACTION_DISMISS);
                    info.setDismissable(true);
                } else {
                    info.setDismissable(false);
                }
            }

            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                if (action == AccessibilityNodeInfoCompat.ACTION_DISMISS && mCancelable) {
                    cancel();
                    return true;
                }
                return super.performAccessibilityAction(host, action, args);
            }
        });
        bottomSheet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // Consume the event and prevent it from falling through
                return true;
            }
        });
        return container;
    }

    boolean shouldWindowCloseOnTouchOutside() {
        if (!mCanceledOnTouchOutsideSet) {
            if (Build.VERSION.SDK_INT < 11) {
                mCanceledOnTouchOutside = true;
            } else {
                TypedArray a = getContext().obtainStyledAttributes(
                        new int[]{android.R.attr.windowCloseOnTouchOutside});
                mCanceledOnTouchOutside = a.getBoolean(0, true);
                a.recycle();
            }
            mCanceledOnTouchOutsideSet = true;
        }
        return mCanceledOnTouchOutside;
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback
            = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet,
                                   @BottomSheetBehavior.State int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                cancel();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };
}
