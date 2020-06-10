package com.bench.android.core.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/************************************************************************
 *@Project: packages
 *@Package_Name: com.bench.android.core.util
 *@Descriptions: 保存异常信息到本地
 *@Author: xingjiu
 *@Date: 2019/8/5
 *************************************************************************/
public class CrashExceptionHandler implements UncaughtExceptionHandler {

    public static final String TAG = CrashExceptionHandler.class.getCanonicalName();
    /**
     * 存储目录
     */
    private File cacheDir;
    /**
     * 系统默认的异常处理器
     */
    private UncaughtExceptionHandler mDefaultHandler;
    private static CrashExceptionHandler INSTANCE = new CrashExceptionHandler();
    private Context mContext;
    /**
     * 存储一些信息
     */
    private Map<String, String> infos = new HashMap<String, String>();

    private DateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");

    private CrashExceptionHandler() {
    }

    /**
     * 获取实例
     *
     * @return instances
     */
    public static CrashExceptionHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context context
     */
    public void init(Context context) {
        mContext = context;
        cacheDir = context.getExternalFilesDir("logs");
        //系统默认的异常处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置当前类为异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        //超过7天自动删除
        autoClear(7);
    }

    /**
     * 自定义存储路径
     *
     * @param context context
     * @param path    自定义存储路径
     */
    public void init(Context context, String path) {
        init(context);
        cacheDir = new File(path);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    /**
     * 异常处理回调
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            SystemClock.sleep(3000);
            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序出现异常,即将重启.",
                        Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            collectDeviceInfo(mContext);
            String str = saveCrashInfo2File(ex);
            Log.e(TAG, str);
        }
        return true;
    }

    /**
     * 收集日志信息
     *
     * @param ctx context
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存错误信息
     *
     * @param ex Throwable
     * @return 文件路径
     */
    private String saveCrashInfo2File(Throwable ex) {

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append("[").append(key).append(", ").append(value).append("]\n");
        }
        sb.append("\n").append(getStackTraceString(ex));
        try {
            String time = formatter.format(new Date());
            String fileName = "CRS_" + time + ".txt";
            File file = new File(cacheDir + File.separator + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(sb.toString().getBytes());
            fos.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取错误信息
     *
     * @param tr 异常
     * @return 异常信息
     */
    public static String getStackTraceString(Throwable tr) {
        PrintWriter pw;
        try {
            if (tr == null) {
                return "";
            }
            StringWriter sw = new StringWriter();
            pw = new PrintWriter(sw);
            //因为直接输出的信息可能会显示不全，所以要循环调用getCause输出信息
            while (tr != null) {
                tr.printStackTrace(pw);
                sw.write("\n");
                tr = tr.getCause();
            }
            pw.close();
            return sw.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 文件删除
     *
     * @param autoClearDay 文件保存天数
     */
    public void autoClear(final int autoClearDay) {
        File[] files = cacheDir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File f : files) {
            String fileName = f.getName().split("\\.")[0];
            fileName = fileName.substring(fileName.indexOf("_") + 1);
            try {
                Date date = formatter.parse(fileName);
                //超过7天自动删除
                if (System.currentTimeMillis() - date.getTime() > autoClearDay * 24 * 60 * 60 * 1000) {
                    f.delete();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }
}