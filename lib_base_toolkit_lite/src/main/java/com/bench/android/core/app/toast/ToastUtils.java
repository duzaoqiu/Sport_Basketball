package com.bench.android.core.app.toast;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bench.android.core.app.application.BaseApplication;


/**
 * @author zhouyi
 */
public class ToastUtils {

    private static boolean isShow = true;//默认显示
    private static Toast mToast;

    /*private控制不应该被实例化*/
    private ToastUtils() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    /**
     * 全局控制是否显示Toast
     *
     * @param isShowToast
     */
    public static void controlShow(boolean isShowToast) {
        isShow = isShowToast;
    }

    /**
     * 取消Toast显示
     */
    public void cancelToast() {
        if (isShow && mToast != null) {
            mToast.cancel();
        }
    }

    public static void showTip(int stringRes) {
        showTip(BaseApplication.getContext(), BaseApplication.getContext().getString(stringRes));
    }

    public static void showTip(CharSequence tip) {
        showTip(BaseApplication.getContext(), tip);
    }

    public static void showTip(Context context, CharSequence tip) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(context.getApplicationContext(), tip, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * @param context
     * @param tip
     * @param duration
     * @param gravity
     */
    public static void showTip(Context context, String tip, int duration, int gravity) {

        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }

        mToast = Toast.makeText(context, tip, duration);

        if (gravity > -1) {
            mToast.setGravity(gravity, 0, 0);
        }
        mToast.show();

    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow) {
            if (mToast == null) {
                mToast = SafeToast.newInstance(context, message, Toast.LENGTH_SHORT);
            } else {
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param resId   资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showShort(Context context, int resId) {
        if (isShow) {
            if (mToast == null) {
                mToast = SafeToast.newInstance(context, resId, Toast.LENGTH_SHORT);
            } else {
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.setText(resId);
            }
            mToast.show();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow) {
            if (mToast == null) {
                mToast = SafeToast.newInstance(context, message, Toast.LENGTH_LONG);
            } else {
                mToast.setDuration(Toast.LENGTH_LONG);
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param resId   资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showLong(Context context, int resId) {
        if (isShow) {
            if (mToast == null) {
                mToast = SafeToast.newInstance(context, resId, Toast.LENGTH_LONG);
            } else {
                mToast.setDuration(Toast.LENGTH_LONG);
                mToast.setText(resId);
            }
            mToast.show();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration 单位:毫秒
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow) {
            if (mToast == null) {
                mToast = SafeToast.newInstance(BaseApplication.getContext(), message, duration);
            } else {
                mToast.setDuration(duration);
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param resId    资源ID:getResources().getString(R.string.xxxxxx);
     * @param duration 单位:毫秒
     */
    public static void show(Context context, int resId, int duration) {
        if (isShow) {
            if (mToast == null) {
                mToast = SafeToast.newInstance(context, resId, duration);
            } else {
                mToast.setDuration(duration);
                mToast.setText(resId);
            }
            mToast.show();
        }
    }

    public static void show(String message, int gravity) {
        if (isShow) {
            if (mToast == null) {
                mToast = SafeToast.newInstance(BaseApplication.getContext(), message, Toast.LENGTH_SHORT);
            } else {
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.setText(message);
                if (gravity != -1) {
                    mToast.setGravity(gravity, 0, 0);
                }
            }
            mToast.show();
        }
    }


    /**
     * 自定义Toast的View
     *
     * @param context
     * @param message
     * @param duration 单位:毫秒
     * @param view     显示自己的View
     */
    public static void customToastView(Context context, CharSequence message, int duration, View view) {
        if (isShow) {
            if (mToast == null) {
                mToast = SafeToast.newInstance(context, message, duration);
            } else {
                mToast.setDuration(duration);
                mToast.setText(message);
            }
            if (view != null) {
                mToast.setView(view);
            }
            mToast.show();
        }
    }

    /**
     * 自定义Toast的位置
     *
     * @param context
     * @param message
     * @param duration 单位:毫秒
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void customToastGravity(Context context, CharSequence message, int duration, int gravity, int xOffset, int yOffset) {
        if (isShow) {
            if (mToast == null) {
                mToast = SafeToast.newInstance(context, message, duration);
            } else {
                mToast.setDuration(duration);
                mToast.setText(message);
            }
            mToast.setGravity(gravity, xOffset, yOffset);
            mToast.show();
        }
    }

    /**
     * 自定义带图片和文字的Toast，最终的效果就是上面是图片，下面是文字
     *
     * @param context
     * @param message
     * @param iconResId 图片的资源id,如:R.drawable.icon
     * @param duration
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void showToastWithImageAndText(Context context, CharSequence message, int iconResId, int duration, int gravity, int xOffset, int yOffset) {
        if (isShow) {
            if (mToast == null) {
                mToast = SafeToast.newInstance(context, message, duration);
            } else {
                mToast.setDuration(duration);
                mToast.setText(message);
            }
            mToast.setGravity(gravity, xOffset, yOffset);
            LinearLayout toastView = (LinearLayout) mToast.getView();
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(iconResId);
            toastView.addView(imageView, 0);
            mToast.show();
        }
    }

    /**
     * 自定义Toast,针对类型CharSequence
     *
     * @param context
     * @param message
     * @param duration
     * @param view
     * @param isGravity        true,表示后面的三个布局参数生效,false,表示不生效
     * @param gravity
     * @param xOffset
     * @param yOffset
     * @param isMargin         true,表示后面的两个参数生效，false,表示不生效
     * @param horizontalMargin
     * @param verticalMargin
     */
    public static void customToastAll(Context context, CharSequence message, int duration, View view, boolean isGravity, int gravity, int xOffset, int yOffset, boolean isMargin, float horizontalMargin, float verticalMargin) {
        if (isShow) {
            if (mToast == null) {
                mToast = SafeToast.newInstance(context, message, duration);
            } else {
                mToast.setDuration(duration);
                mToast.setText(message);
            }
            if (view != null) {
                mToast.setView(view);
            }
            if (isMargin) {
                mToast.setMargin(horizontalMargin, verticalMargin);
            }
            if (isGravity) {
                mToast.setGravity(gravity, xOffset, yOffset);
            }
            mToast.show();
        }
    }

    /**
     * 自定义Toast,针对类型resId
     *
     * @param context
     * @param resId
     * @param duration
     * @param view             :应该是一个布局，布局中包含了自己设置好的内容
     * @param isGravity        true,表示后面的三个布局参数生效,false,表示不生效
     * @param gravity
     * @param xOffset
     * @param yOffset
     * @param isMargin         true,表示后面的两个参数生效，false,表示不生效
     * @param horizontalMargin
     * @param verticalMargin
     */
    public static void customToastAll(Context context, int resId, int duration, View view, boolean isGravity, int gravity, int xOffset, int yOffset, boolean isMargin, float horizontalMargin, float verticalMargin) {
        if (isShow) {
            if (mToast == null) {
                mToast = SafeToast.newInstance(context, resId, duration);
            } else {
                mToast.setDuration(duration);
                mToast.setText(resId);
            }
            if (view != null) {
                mToast.setView(view);
            }
            if (isMargin) {
                mToast.setMargin(horizontalMargin, verticalMargin);
            }
            if (isGravity) {
                mToast.setGravity(gravity, xOffset, yOffset);
            }
            mToast.show();
        }
    }

    /**
     * 只传入一个message
     */
    public static void show(CharSequence message) {
        if (isShow) {
            if (mToast == null) {
                mToast = SafeToast.newInstance(BaseApplication.getContext(), message, Toast.LENGTH_SHORT);
            } else {
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.setText(message);
            }
            mToast.show();
        }
    }
}
