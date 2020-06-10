package com.bench.android.core.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bench.android.core.content.sharePreference.SharedPreferUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.HONEYCOMB;
import static android.os.Build.VERSION_CODES.HONEYCOMB_MR1;

/**
 * Created by zhouyi on 2015/11/3 10:10.
 */
public class LibAppUtil {


    public static PackageInfo getPackageInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getApkVersionName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPackageMetaData(Context context, String key) {
        String value = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    /**
     * 将金额格式化为每三位添加一个逗号
     */
    public static String formatMoney(long n, boolean isYuan) {
        if (n == -1) {
            return Long.toString(0);
        }

        if (isYuan) {
            StringBuffer sb = new StringBuffer(Long.toString(n));
            for (int i = sb.length(); i > 0; i -= 3) {
                if (i != sb.length()) {
                    sb.insert(i, ",");
                }

            }
            return sb.toString();
        } else {
            return Long.toString(n);
        }

    }

    /**
     * 将金额格式化为每三位添加一个逗号
     */
    public static String formatMoney(long n) {
        if (n == -1) {
            return Long.toString(0);
        }
        return convert(n);


    }

    public static String convert(long money) {
        int danYi = 100000000;
        int danWan = 10000;
        int yi = (int) (money / danYi);
        int wan = (int) ((money - yi * danYi) / danWan);
        int yuan = (int) (money - (yi * danYi) - (wan * danWan));
        StringBuffer sb = new StringBuffer();
        if (yi > 0) {
            sb.append(yi + "亿");
        }
        if (wan > 0) {
            sb.append(wan + "万");
        }
        sb.append(yuan + "元");
        return sb.toString();

    }

    /**
     * 截取到单位万
     *
     * @param s
     * @return
     */
    public static String getWan(String s) {
        int index = s.indexOf("万");
        if (index > 0) {
            return s.substring(0, index + 1);
        }
        return s;
    }

    /**
     * 格式化亚赔数据
     * 四舍六入5显示
     */
    public static String formatAsianOdds(double d) {

        String str = Double.toString(d);
        if (str.length() >= 5) {
            String b = str.substring(str.length() - 1);
            if (Integer.parseInt(b) == 5) {
                return new DecimalFormat("#0.000").format(d);
            } else {
                return new DecimalFormat("#0.00").format(d);
            }
        } else {
            return new DecimalFormat("#0.00").format(d);
        }
    }

    public static String formatScore(int homeScore, int guestScore) {
        if (homeScore < 0 || guestScore < 0) {
            return "-";
        } else {
            return homeScore + ":" + guestScore;
        }
    }


    /**
     * 设置EditText最大长度
     *
     * @param et
     * @param maxlength
     */
    public static void setEditTextMaxLength(EditText et, int maxlength) {
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxlength)});
    }

    /**
     * 通过id名,获取id
     */
    public static int getId(Context context, String name) {
        Resources res = context.getResources();
        final String packageName = context.getPackageName();
        int id = res.getIdentifier(name, "id", packageName);
        return id;
    }

    /**
     * 如果数字小于10，则增加一个0
     *
     * @param i
     * @return
     */
    public static String numAddFrontZero(int i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return Integer.toString(i);
        }
    }


    public static StringBuffer deleteLastSpecialChar(StringBuffer sb, String s) {

        if (sb.lastIndexOf(s) != -1) {

            sb.deleteCharAt(sb.length() - 1);

        }
        return sb;

    }

    /**
     * 该方法不一定能关闭键盘 比如在dialog或popwindow中就不行
     *
     * @param context
     * @deprecated 请使用{@link KeyboardUtil#hideSoftInput(Context, View)}
     */
    @Deprecated
    public static void hideSoftKeyboard(Activity context) {
        View view = context.getWindow().peekDecorView();
        hideSoftKeyboard(context, view);
    }

    /**
     * @param context
     * @param view    键盘绑定的view
     * @deprecated 请使用{@link KeyboardUtil#hideSoftInput(Context, View)}
     */
    @Deprecated
    public static void hideSoftKeyboard(Activity context, View view) {
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static Double maxValue(double[] ds) {
        double maxValue = Double.MIN_VALUE;
        for (Double d : ds) {
            if (d > maxValue) {
                maxValue = d;
            }
        }
        return maxValue;
    }

    public static Double minValue(double[] ds) {
        double minValue = Double.MAX_VALUE;
        for (Double d : ds) {
            if (d < minValue) {
                minValue = d;
            }
        }
        return minValue;
    }

    public static SpannableString setSpan(String value) {

        SpannableString ss = new SpannableString(value);

        BackgroundColorSpan span = new BackgroundColorSpan(Color.parseColor("#f63f3f"));

        ss.setSpan(span, 0, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan span1 = new ForegroundColorSpan(Color.WHITE);

        ss.setSpan(span1, 0, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    /**
     * 请使用{@link KeyboardUtil#showSoftInput(Context, EditText)}
     *
     * @param context
     * @param view
     */
    @Deprecated
    public static void showSoftKeyboard(Context context, EditText view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
    }

    /**
     * 判断App是否处于后台运行
     *
     * @param context
     * @return
     */
    public static boolean isAppBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {

        boolean isWork = false;

        ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);

        if (myList.size() <= 0) {
            return false;
        }

        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }

        return isWork;

    }

    public static int calculateMemoryCacheSize(Context context) {

        ActivityManager am = getService(context, ACTIVITY_SERVICE);

        boolean largeHeap = (context.getApplicationInfo().flags & FLAG_LARGE_HEAP) != 0;

        int memoryClass = am.getMemoryClass();

        if (largeHeap && SDK_INT >= HONEYCOMB) {
            memoryClass = ActivityManagerHoneycomb.getLargeMemoryClass(am);
        }

        // Target ~15% of the available heap.
        return 1024 * 1024 * memoryClass / 7;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getService(Context context, String service) {
        return (T) context.getSystemService(service);
    }

    static int getBitmapBytes(Bitmap bitmap) {
        int result;
        if (SDK_INT >= HONEYCOMB_MR1) {
            result = BitmapHoneycombMR1.getByteCount(bitmap);
        } else {
            result = bitmap.getRowBytes() * bitmap.getHeight();
        }
        if (result < 0) {
            throw new IllegalStateException("Negative size: " + bitmap);
        }
        return result;
    }


    @TargetApi(HONEYCOMB)
    private static class ActivityManagerHoneycomb {
        static int getLargeMemoryClass(ActivityManager activityManager) {
            return activityManager.getLargeMemoryClass();
        }
    }

    @TargetApi(HONEYCOMB_MR1)
    private static class BitmapHoneycombMR1 {
        static int getByteCount(Bitmap bitmap) {
            return bitmap.getByteCount();
        }
    }


    public static boolean isListBlank(List<? extends Object> list) {
        if (list == null) {
            return true;
        }
        return list.size() == 0;
    }


    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Point size = new Point();

        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);

        return size.x;

    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Point size = new Point();

        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);

        return size.y;

    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }


    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    public static String getAppDeviceId() {
        String appDeviceId = SharedPreferUtil.getInstance().getString("app_device_id", null);
        if (!TextUtils.isEmpty(appDeviceId)) return appDeviceId;
        appDeviceId = getUniquePsuedoID();
        SharedPreferUtil.getInstance().putString("app_device_id", appDeviceId);
        return appDeviceId;
    }

    public static String getUniquePsuedoID() {
        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static String getPesudoUniqueID() {
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10 +//13 digits
                Build.SERIAL.length() % 10;
        return m_szDevIDShort;
    }

    public static String getMacAddress() {
        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return macAddress;
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return macAddress;
        }
        return macAddress;
    }


    public static String getFromRaw(Context context, String fileName) {

        StringBuilder buf = new StringBuilder();

        InputStream json = null;

        BufferedReader in = null;

        String mainContent = "";

        try {

            json = context.getAssets().open(fileName);

            in = new BufferedReader(new InputStreamReader(json, StandardCharsets.UTF_8));

            String str;

            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            mainContent = buf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (json != null) {
                try {
                    json.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        LogUtils.e("aaaa", mainContent);
        return mainContent;
    }

    public static byte[] getBytesByBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        return outputStream.toByteArray();
    }

    public static String formatReplyString(String str) {
        String replyStr = str.replace("<br>", "").replace("</p>", "").replace("<p>", "");

        return replyStr;

    }
}
