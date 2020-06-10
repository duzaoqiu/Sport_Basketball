package com.bench.android.core.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.library.R;
import com.bench.android.core.app.dialog.adapter.BottomSelectAdapter;
import com.bench.android.core.app.dialog.holder.ViewHolder;
import com.bench.android.core.util.LibAppUtil;
import com.bench.android.core.util.LogUtils;
import com.bench.android.core.util.dimension.PxConverter;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by lingjiu on 2019/2/18.
 * <p>
 * <p>
 * 2019/8/23
 * 原来直接调用的方法已经废弃，推荐使用builder模式构建
 */
public class DialogHelper {
    @Deprecated
    public static Dialog showSimpleMessageDialog(Context context,
                                                 final CharSequence positiveMsg,
                                                 final View.OnClickListener ok,
                                                 final String message) {
        return showSimpleMessageDialog(context, null, positiveMsg, ok, message, 0);
    }

    @Deprecated
    public static Dialog showMessageDialog(Context context,
                                           final View.OnClickListener ok,
                                           final String message) {
        return showMessageDialog(context, null, null, ok, null, null, message, true);
    }

    @Deprecated
    public static Dialog showMessageDialog(Context context,
                                           final View.OnClickListener ok,
                                           final View.OnClickListener cancel,
                                           final String message) {
        return showMessageDialog(context, null, null, ok, null, cancel, message, true);
    }

    @Deprecated
    public static Dialog showBottomListDialog(Context context, final OnMyItemClickListener<CharSequence> clickListener,
                                              final CharSequence... message) {
        return showBottomListDialog(context, null, null, clickListener, false, null, message);
    }

    @Deprecated
    public static Dialog showBottomListDialog(Context context, final CharSequence headerMsg, final CharSequence footMsg,
                                              final OnMyItemClickListener<CharSequence> clickListener,
                                              final CharSequence... message) {
        return showBottomListDialog(context, headerMsg, footMsg, clickListener, false, null, message);
    }


    /**
     * 单按钮dialog
     *
     * @param context
     * @param titleMsg
     * @param positiveMsg
     * @param ok
     * @param message
     * @param textGravity
     * @return
     */
    @Deprecated
    public static Dialog showSimpleMessageDialog(Context context, final CharSequence titleMsg,
                                                 final CharSequence positiveMsg,
                                                 final View.OnClickListener ok,
                                                 final String message, final int textGravity) {

        ViewConvertListener convertListener = new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, final BaseDialog dialog) {
                //进行相关View操作的回调
                holder.setOnClickListener(R.id.okBtn, new WrapClickListener(dialog, ok));
                holder.setViewVisibility(R.id.cancelBtn, View.GONE);
                holder.setText(R.id.messageTv, message);
                if (!TextUtils.isEmpty(positiveMsg)) {
                    holder.setText(R.id.okBtn, positiveMsg);
                }
                if (!TextUtils.isEmpty(titleMsg)) {
                    holder.setText(R.id.titleTv, titleMsg);
                }
                if (textGravity > 0) {
                    holder.setTextGravity(R.id.messageTv, textGravity);
                }
            }
        };
        BaseDialog dialog = GeneralDialog.init(context)
                .setLayoutId(R.layout.dialog_layout)     //设置dialog布局文件
                .setConvertListener(convertListener)
                .setDimAmount(0.3f)     //调节灰色背景透明度[0-1]，默认0.5f
                .setOutCancel(false);
        dialog.show();
        return dialog;
    }

    @Deprecated
    public static Dialog showMessageDialog(Context context, final CharSequence titleMsg,
                                           final CharSequence positiveMsg, final View.OnClickListener ok,
                                           final CharSequence negativeMsg, final View.OnClickListener cancel,
                                           final String message) {
        return showMessageDialog(context, titleMsg, positiveMsg, ok, negativeMsg, cancel, message, true);
    }

    /**
     * 双按钮dialog
     *
     * @param context
     * @param positiveMsg
     * @param ok
     * @param negativeMsg
     * @param cancel
     * @param message
     * @return
     */
    @Deprecated
    public static Dialog showMessageDialog(Context context, final CharSequence titleMsg,
                                           final CharSequence positiveMsg, final View.OnClickListener ok,
                                           final CharSequence negativeMsg, final View.OnClickListener cancel,
                                           final String message, final boolean autoDismiss) {

        ViewConvertListener convertListener = new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, final BaseDialog dialog) {
                //进行相关View操作的回调
                holder.setOnClickListener(R.id.okBtn, new WrapClickListener(dialog, ok, autoDismiss));
                holder.setOnClickListener(R.id.cancelBtn, new WrapClickListener(dialog, cancel));
                holder.setText(R.id.messageTv, message);
                if (!TextUtils.isEmpty(negativeMsg)) {
                    holder.setText(R.id.cancelBtn, negativeMsg);
                }
                if (!TextUtils.isEmpty(positiveMsg)) {
                    holder.setText(R.id.okBtn, positiveMsg);
                }
                if (!TextUtils.isEmpty(titleMsg)) {
                    holder.setText(R.id.titleTv, titleMsg);
                }
            }
        };
        BaseDialog dialog = GeneralDialog.init(context)
                .setLayoutId(R.layout.dialog_layout)     //设置dialog布局文件
                .setConvertListener(convertListener)
                .setDimAmount(0.3f)     //调节灰色背景透明度[0-1]，默认0.5f
                .setOutCancel(false);
        dialog.show();
        return dialog;
    }

    /**
     * 双按钮dialog
     *
     * @param context
     * @param positiveMsg
     * @param ok
     * @param negativeMsg
     * @param cancel
     * @param message
     * @param textGravity
     * @return
     */
    @Deprecated
    public static Dialog showMessageDialog(Context context, final CharSequence titleMsg,
                                           final CharSequence positiveMsg, final View.OnClickListener ok,
                                           final CharSequence negativeMsg, final View.OnClickListener cancel,
                                           final String message, final int textGravity) {

        ViewConvertListener convertListener = new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, final BaseDialog dialog) {
                //进行相关View操作的回调
                holder.setOnClickListener(R.id.okBtn, new WrapClickListener(dialog, ok));
                holder.setOnClickListener(R.id.cancelBtn, new WrapClickListener(dialog, cancel));
                holder.setText(R.id.messageTv, message);
                if (!TextUtils.isEmpty(negativeMsg)) {
                    holder.setText(R.id.cancelBtn, negativeMsg);
                }
                if (!TextUtils.isEmpty(positiveMsg)) {
                    holder.setText(R.id.okBtn, positiveMsg);
                }
                if (!TextUtils.isEmpty(titleMsg)) {
                    holder.setText(R.id.titleTv, titleMsg);
                }
                if (textGravity > 0) {
                    holder.setTextGravity(R.id.messageTv, textGravity);
                }
            }
        };
        BaseDialog dialog = GeneralDialog.init(context)
                .setLayoutId(R.layout.dialog_layout)     //设置dialog布局文件
                .setConvertListener(convertListener)
                .setDimAmount(0.3f)     //调节灰色背景透明度[0-1]，默认0.5f
                .setOutCancel(false);
        dialog.show();
        return dialog;
    }

    /**
     * 底部列表展示的单选dialog
     *
     * @param context       上下文
     * @param headerMsg     头部Msg
     * @param footMsg       底部Msg
     * @param clickListener 监听回调
     * @param overScroll    数量过多(>5个)是否可以滑动,不能滑动则全部显示
     * @param baseAdapter   自定义adapter
     * @param message       数据
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> Dialog showBottomListDialog(final Context context, final CharSequence headerMsg, final CharSequence footMsg,
                                                  final OnMyItemClickListener<T> clickListener, final boolean overScroll,
                                                  final BaseAdapter baseAdapter, final T... message) {
        ViewConvertListener convertListener = new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, final BaseDialog dialog) {
                //进行相关View操作的回调
                if (!TextUtils.isEmpty(headerMsg)) {
                    holder.setText(R.id.header_tv, headerMsg);
                    holder.setViewVisibility(R.id.header_tv, View.VISIBLE);
                } else {
                    holder.setViewVisibility(R.id.header_tv, View.GONE);
                }
                if (!TextUtils.isEmpty(footMsg)) {
                    holder.setText(R.id.footer_tv, footMsg);
                    holder.setViewVisibility(R.id.footer_tv, View.VISIBLE);
                } else {
                    holder.setViewVisibility(R.id.footer_tv, View.GONE);
                }

                holder.setOnClickListener(R.id.footer_tv, new WrapClickListener(dialog, null));

                ListView listView = holder.getView(R.id.listView);
                //设置自定义适配器,默认ArrayAdapter
                if (baseAdapter != null) {
                    listView.setAdapter(baseAdapter);
                } else {
                    listView.setAdapter(new BottomSelectAdapter<T>(context, message));
                    listView.setDivider(listView.getResources().getDrawable(R.drawable.divider_horizontal_line));
                }
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (clickListener != null) {
                            clickListener.onClickListener(message[position], position, dialog);
                        }
                        dialog.dismiss();
                    }
                });
                if (overScroll && message.length > 5) {
                    listView.setLayoutParams(new FrameLayout.LayoutParams(-1, PxConverter.dp2px(context, 200)));
                }
            }
        };

        BaseDialog dialog = GeneralDialog.init(context)
                .setLayoutId(R.layout.base_dialog_list_layout)     //设置dialog布局文件
                .setConvertListener(convertListener)
                .setDimAmount(0.3f)     //调节灰色背景透明度[0-1]，默认0.5f
                .setWidth(-1)
                .setShowBottom(true);
        dialog.show();
        return dialog;
    }

    public static Dialog showPopupDialog(Context context, View anchorView,
                                         final OnMyItemClickListener<String> itemClickListener,
                                         List<String> data) {
        //设置适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.simple_menu_list_item1, R.id.text1, data);
        Dialog dialog = showPopupDialog(context, anchorView,
                Gravity.BOTTOM, 0, 0, adapter, itemClickListener, 0, 5);
        return dialog;
    }

    /**
     * 图片加文字菜单栏
     * 例子:kk首页菜单
     *
     * @param context
     * @param anchorView        锚点view
     * @param gravity           位于锚点的方向
     * @param offsetX           x方向上偏移量
     * @param offsetY           y方向上偏移量
     * @param itemClickListener 回调监听
     * @param width             dialog宽度值 (0的时候,默认匹配anchorView的宽度)
     * @param data              数据源
     * @return
     */
    @Deprecated
    public static Dialog showPopupDialogWithImg(final Context context, View anchorView,
                                                int gravity,
                                                int offsetX,
                                                int offsetY,
                                                OnMyItemClickListener<String> itemClickListener,
                                                int width,
                                                final List<Map<String, Object>> data) {
        ListAdapter adapter = new SimpleAdapter(context, data,
                R.layout.simple_menu_list_item, new String[]{"text", "image"}, new int[]{R.id.textView,
                R.id.imageView});
        Dialog dialog = showPopupDialog(context, anchorView, gravity, offsetX, offsetY,
                adapter, itemClickListener, 0, 5);
        return dialog;
    }

    /**
     * 纯文字菜单栏
     * 例子:比分直播时间选择
     *
     * @param context
     * @param anchorView        锚点view
     * @param gravity           位于锚点的方向
     * @param offsetX           x方向上偏移量
     * @param offsetY           y方向上偏移量
     * @param itemClickListener 回调监听
     * @param width             dialog宽度值(0的时候,默认匹配anchorView的宽度)
     * @param data              数据源
     * @return
     */
    @Deprecated
    public static Dialog showPopupDialog(Context context, View anchorView,
                                         int gravity,
                                         int offsetX,
                                         int offsetY,
                                         final OnMyItemClickListener<String> itemClickListener,
                                         int width,
                                         List<String> data) {
        //设置适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.simple_menu_list_item1, R.id.text1, data);
        Dialog dialog = showPopupDialog(context, anchorView,
                gravity, offsetX, offsetY, adapter, itemClickListener, width, 5);
        return dialog;
    }

    @Deprecated
    public static <T> Dialog showPopupDialog(final Context context, final View anchorView,
                                             final int gravity,
                                             final int offsetX,
                                             final int offsetY,
                                             final ListAdapter baseAdapter,
                                             final OnMyItemClickListener<T> clickListener,
                                             int width,
                                             final int overCountScroll) {

        //定位坐标位置
        final int[] location = new int[2];
        final int x, y;
        anchorView.getLocationInWindow(location);
        x = location[0];
        y = location[1] + anchorView.getHeight() / 2;
        LogUtils.e("Dialog", "x = " + x + " y = " + y);
        ViewConvertListener convertListener = new ViewConvertListener() {
            @Override
            public void convertView(final ViewHolder holder, final BaseDialog dialog) {
                if (baseAdapter == null) {
                    return;
                }
                ListView listView = holder.getView(R.id.listView);
                //设置自定义适配器,默认SimpleAdapter
                listView.setAdapter(baseAdapter);
                //超过可滑动
                if (baseAdapter.getCount() > overCountScroll) {
                    listView.setLayoutParams(
                            new LinearLayout.LayoutParams(-1, PxConverter.dp2px(context, 160)));
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (clickListener != null) {
                            clickListener.onClickListener((T) baseAdapter.getItem(position), position, dialog);
                        }
                        dialog.dismiss();
                    }
                });
                holder.getConvertView().post(new Runnable() {
                    @Override
                    public void run() {
                        int locationY;
                        int locationX;
                        //LogUtils.e("Dialog", "height=" + offsetY + "  anchorView.getHeight()=" + anchorView.getHeight());
                        int contentViewHeight = holder.getConvertView().getHeight();
                        int anchorViewHeight = anchorView.getHeight();
                        int anchorViewWidth = anchorView.getWidth();
                        //还有其余几个方向例如Gravity.LEFT|Gravity.TOP,这边就不列举了,可以通过offset去做
                        switch (gravity) {
                            case Gravity.LEFT:
                                locationY = y - anchorViewHeight;
                                locationX = x - anchorViewWidth;
                                break;
                            case Gravity.TOP:
                                locationY = y - contentViewHeight - anchorViewHeight;
                                locationX = x;
                                break;
                            case Gravity.RIGHT:
                                locationY = y - anchorViewHeight;
                                locationX = x + anchorViewWidth;
                                break;
                            default:
                            case Gravity.BOTTOM:
                                locationY = y;
                                locationX = x;
                                break;
                        }
                        //如果dialog显示不完整,则向反方向显示
                        locationX += offsetX;
                        locationY += offsetY;
                        if (locationX < 0) {
                            locationX = x + anchorViewWidth + offsetX;
                        } else if (locationX > LibAppUtil.getScreenWidth(context)) {
                            locationX = x - anchorViewWidth + offsetX;
                        }
                        if (locationY < 0) {
                            locationY = y + offsetX;
                        } else if (locationY > LibAppUtil.getScreenHeight(context)) {
                            locationY = y - contentViewHeight - anchorViewHeight + offsetX;
                        }
                        dialog.updateX(locationX);
                        dialog.updateY(locationY);
                    }
                });
            }
        };
        BaseDialog dialog = GeneralDialog.init(context)
                .setLayoutId(R.layout.base_dialog_menu_list_layout)     //设置dialog布局文件
                .setConvertListener(convertListener)
                .setX(x)
                //默认宽度为anchorView的宽度,-1为MatchParent
                .setWidth(width == 0 ? anchorView.getWidth() : width)
                .setY(y)
                .setDimAmount(0.3f);     //调节灰色背景透明度[0-1]，默认0.5f
        dialog.show();
        return dialog;
    }

    static class WrapClickListener implements View.OnClickListener {
        private Dialog dialog;
        private View.OnClickListener onClickListener;
        private boolean autoDismiss;

        WrapClickListener(Dialog dialog, View.OnClickListener onClickListener) {
            this(dialog, onClickListener, true);
        }

        WrapClickListener(Dialog dialog, View.OnClickListener onClickListener, boolean autoDismiss) {
            this.dialog = dialog;
            this.onClickListener = onClickListener;
            this.autoDismiss = autoDismiss;
        }

        @Override
        public void onClick(View v) {
            if (autoDismiss) {
                dialog.dismiss();
            }
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        }
    }

    /**
     * build模式构建的Dialog
     * =================================================================================
     * =================================================================================
     */

    /**
     * 上下文
     */
    public Context context;

    /**
     * dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
     */
    public boolean canceledOnTouchOutside = true;
    /**
     * dialog弹出后会点击屏幕或物理返回键，dialog不消失
     */
    public boolean cancelable = false;

    /**
     * dialog宽高
     */
    public int width = WindowManager.LayoutParams.WRAP_CONTENT;
    public int height = WindowManager.LayoutParams.WRAP_CONTENT;

    /**
     * dialog位置，默认居中
     */

    public int gravity = Gravity.CENTER;

    /**
     * 动画
     */
    public int windowAnimations = -1;


    /**
     * options里面保存的是默认两种样式列表的一些文字、标题、颜色等
     */
    public Options options;

    /**
     * 自定义的dialog才需要Holder
     */
    public ViewHolder holder;

    /**
     * 坐标
     */
    public int x, y;

    /**
     * dialog弹出时，背景的透明度
     */
    public float dimAmount = 0.3f;

    /**
     * dialog消失的回调
     */
    public DialogInterface.OnDismissListener onDismissListener;

    public DialogHelper(Context context) {
        this.context = context;
    }

    public DialogHelper width(int width) {
        this.width = width;
        return this;
    }

    public DialogHelper height(int height) {
        this.height = height;
        return this;
    }

    public DialogHelper dimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    public DialogHelper gravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public DialogHelper windowAnimations(int windowAnimations) {
        this.windowAnimations = windowAnimations;
        return this;
    }

    public DialogHelper canceledOnTouchOutside(boolean canceledOnTouchOutside) {
        this.canceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }

    public DialogHelper cancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public DialogHelper onDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public DialogHelper x(int x) {
        this.x = x;
        return this;
    }

    public DialogHelper y(int y) {
        this.y = y;
        return this;
    }

    public CommonDialog build(Options options) {
        this.options = options;
        return new CommonDialog(context, this);
    }

    public CommonDialog build(ViewHolder holder) {
        this.holder = holder;
        return new CommonDialog(context, this);
    }

    /**
     * dialog的一些配置
     */
    public interface Options {

    }

    /**
     * 通用样式的dialog的一些配置
     */
    public static class CommonDialogOptions implements Options {
        /**
         * 点确认
         */
        public View.OnClickListener onPositiveListener;

        /**
         * 点取消
         */
        public View.OnClickListener onNegativeListener;

        /**
         * 标题
         */
        public String title;
        /**
         * 是否显示标题
         */
        public boolean showTitle = true;
        /**
         * 内容
         */
        public String content;

        /**
         * 确认按钮文字
         */
        public String positiveMsg;
        /**
         * 取消按钮文字
         */
        public String negativeMsg;

        /**
         * 标题的位置
         */
        public int titleGravity = -1;
        /**
         * 内容的位置
         */
        public int contentGravity = -1;

        /**
         * 是否只有一个确定按钮，默认两个按钮
         */
        public boolean singleButton = false;

        public CommonDialogOptions onPositive(View.OnClickListener onClickOkListener) {
            this.onPositiveListener = onClickOkListener;
            return this;
        }

        public CommonDialogOptions onNegative(View.OnClickListener onClickCancelListener) {
            this.onNegativeListener = onClickCancelListener;
            return this;
        }

        public CommonDialogOptions title(String title) {
            this.title = title;
            return this;
        }

        public CommonDialogOptions showTitle(boolean showTitle) {
            this.showTitle = showTitle;
            return this;
        }

        public CommonDialogOptions content(String content) {
            this.content = content;
            return this;
        }

        public CommonDialogOptions positiveMsg(String positiveMsg) {
            this.positiveMsg = positiveMsg;
            return this;
        }

        public CommonDialogOptions negativeMsg(String negativeMsg) {
            this.negativeMsg = negativeMsg;
            return this;
        }

        public CommonDialogOptions titleGravity(int titleGravity) {
            this.titleGravity = titleGravity;
            return this;
        }

        public CommonDialogOptions contentGravity(int contentGravity) {
            this.contentGravity = contentGravity;
            return this;
        }

        public CommonDialogOptions singleButton(boolean singleButton) {
            this.singleButton = singleButton;
            return this;
        }
    }

    /**
     * 从底部弹出的dialog的一些配置
     */
    public static class BottomDialogOptions implements Options {
        public String head;
        public String footer;
        public List<String> list;
        public BaseQuickAdapter.OnItemClickListener onItemClickListener;

        public BottomDialogOptions header(String head) {
            this.head = head;
            return this;
        }

        public BottomDialogOptions footer(String bottom) {
            this.footer = bottom;
            return this;
        }

        public BottomDialogOptions list(List<String> list) {
            this.list = list;
            return this;
        }

        public BottomDialogOptions onItemClickListener(BaseQuickAdapter.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            return this;
        }
    }


}
