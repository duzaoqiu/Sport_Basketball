package com.bench.android.core.cache;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.bench.android.core.app.application.BaseApplication;
import com.bench.android.core.graphics.BitmapUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/************************************************************************
 *@Project: packages
 *@Package_Name: com.bench.android.core.util
 *@Descriptions: 内部存储缓存管理器，限制存储数量和大小
 *@Author: xingjiu
 *@Date: 2019/8/5
 *************************************************************************/
public class InternalCacheManager {
    public static final int TIME_HOUR = 60 * 60;
    public static final int TIME_DAY = TIME_HOUR * 24;
    /**
     * 20 mb
     */
    private static final int MAX_SIZE = 1000 * 1000 * 20;
    /**
     * 最多存放100个文件
     */
    private static final int MAX_COUNT = 200;
    private static InternalCacheManager manager;
    private final File cacheDir;
    private CacheManager mCache;


    public static InternalCacheManager get() {
        if (manager == null) {
            manager = new InternalCacheManager();
        }
        return manager;
    }

    private InternalCacheManager() {
        cacheDir = BaseApplication.getContext().getCacheDir();
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            throw new RuntimeException("can't make dirs in "
                    + cacheDir.getAbsolutePath());
        }
        mCache = new CacheManager(cacheDir, MAX_SIZE, MAX_COUNT);
    }


    /**
     * 获取文件存储路径，为了统一存储目录
     *
     * @param fileName 文件名,必须带后缀文件类型
     * @return 文件路径
     */
    public File getInternalFilePath(String fileName) {
        File file = new File(cacheDir, fileName);
        //如果文件存在，返回文件
        if(file.exists()){
            return file;
        }
        try {
            //否则创建文件
            boolean success = file.createNewFile();
            if (success) {
//             在获取路径前先创建文件，然后作判断1. 是否超过最大缓存个数限制 2. 是否超过缓存大小
//             如果存储个数或者大小超出限制，则清理掉最不常用的文件
//             因为在往文件里面写入数据前，不知道文件最终大小，所以此处总大小可能会超过最大缓存大小，这个是正常现象
                mCache.put(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    /**
     * 保存 String数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的String数据
     */
    public void put(String key, String value) {
        synchronized (InternalCacheManager.class) {
            //存入文件之前先判断这个文件是否存在
            //如果存在，则删除，主要是为了删除缓存管理器里面记录的数量和占用空间总大小
            File file = mCache.get(key);
            if (file.exists()) {
                boolean remove = remove(key);
                if (!remove) {
                    return;
                }
            }
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new FileWriter(file), 1024);
                out.write(value);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mCache.put(file);
            }
        }
    }

    /**
     * 保存 String数据 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的String数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, String value, int saveTime) {
        put(key, Utils.newStringWithDateInfo(saveTime, value));
    }

    /**
     * 读取 String数据
     *
     * @param key
     * @return String 数据
     */
    public String getAsString(String key) {
        File file = mCache.get(key);
        if (!file.exists()) {
            return null;
        }
        boolean removeFile = false;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            StringBuilder readString = new StringBuilder();
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                readString.append(currentLine);
            }
            if (!Utils.isDue(readString.toString())) {
                return Utils.clearDateInfo(readString.toString());
            } else {
                removeFile = true;
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (removeFile) {
                remove(key);
            }
        }
    }


    // =======================================
    // ============== byte 数据 读写 =============
    // =======================================

    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的数据
     */
    public void put(String key, byte[] value) {
        //存入文件之前先判断这个文件是否存在
        //如果存在，则删除，主要是为了删除缓存管理器里面记录的数量和占用空间总大小
        synchronized (InternalCacheManager.class) {
            File file = get(key);
            if (file.exists()) {
                boolean remove = remove(key);
                if (remove) {
                    return;
                }
            }
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                out.write(value);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, byte[] value, int saveTime) {
        put(key, Utils.newByteArrayWithDateInfo(saveTime, value));
    }

    /**
     * 获取 byte 数据
     *
     * @param key
     * @return byte 数据
     */
    public byte[] getAsBinary(String key) {
        RandomAccessFile RAFile = null;
        boolean removeFile = false;
        try {
            File file = mCache.get(key);
            if (!file.exists()) {
                return null;
            }
            RAFile = new RandomAccessFile(file, "r");
            byte[] byteArray = new byte[(int) RAFile.length()];
            RAFile.read(byteArray);
            if (!Utils.isDue(byteArray)) {
                return Utils.clearDateInfo(byteArray);
            } else {
                removeFile = true;
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (RAFile != null) {
                try {
                    RAFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (removeFile) {
                remove(key);
            }
        }
    }

    // =======================================
    // ============= 序列化 数据 读写 ===============
    // =======================================

    /**
     * 保存 Serializable数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的value
     */
    public void put(String key, Serializable value) {
        put(key, value, -1);
    }

    /**
     * 保存 Serializable数据到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的value
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, Serializable value, int saveTime) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            byte[] data = baos.toByteArray();
            if (saveTime != -1) {
                put(key, data, saveTime);
            } else {
                put(key, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取 Serializable数据
     *
     * @param key
     * @return Serializable 数据
     */
    public Object getAsObject(String key) {
        byte[] data = getAsBinary(key);
        if (data != null) {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                bais = new ByteArrayInputStream(data);
                ois = new ObjectInputStream(bais);
                Object reObject = ois.readObject();
                return reObject;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    // =======================================
    // ============== bitmap 数据 读写 =============
    // =======================================

    /**
     * 保存 bitmap 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的bitmap数据
     */
    public void put(String key, Bitmap value) {
        put(key, BitmapUtils.bitmapToByte(value));
    }

    /**
     * 保存 bitmap 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的 bitmap 数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, Bitmap value, int saveTime) {
        put(key, BitmapUtils.bitmapToByte(value), saveTime);
    }

    /**
     * 读取 bitmap 数据
     *
     * @param key
     * @return bitmap 数据
     */
    public Bitmap getAsBitmap(String key) {
        if (getAsBinary(key) == null) {
            return null;
        }
        return BitmapUtils.byteToBitmap(getAsBinary(key));
    }

    // =======================================
    // ============= drawable 数据 读写 =============
    // =======================================

    /**
     * 保存 drawable 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的drawable数据
     */
    public void put(String key, Drawable value) {
        put(key, BitmapUtils.drawableToBitmap(value));
    }

    /**
     * 保存 drawable 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的 drawable 数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, Drawable value, int saveTime) {
        put(key, BitmapUtils.drawableToBitmap(value), saveTime);
    }

    /**
     * 读取 Drawable 数据
     *
     * @param key
     * @return Drawable 数据
     */
    public Drawable getAsDrawable(String key) {
        if (getAsBinary(key) == null) {
            return null;
        }
        return BitmapUtils.bitmapToDrawable(BitmapUtils.byteToBitmap(getAsBinary(key)));
    }

    /**
     * 获取缓存文件
     *
     * @param key 键
     * @return value 缓存的文件
     */
    public File get(String key) {
        return mCache.get(key);
    }

    /**
     * 移除某个key
     *
     * @param key
     * @return 是否移除成功
     */
    public boolean remove(String key) {
        return mCache.remove(key);
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        mCache.clear();
    }

    /**
     * 文件缓存管理
     */
    public class CacheManager {
        private final AtomicLong cacheSize;
        private final long sizeLimit;
        private final int countLimit;
        private final List<File> mFileList = Collections
                .synchronizedList(new ArrayList<File>());
        protected File cacheDir;

        private CacheManager(File cacheDir, long sizeLimit, int countLimit) {
            this.cacheDir = cacheDir;
            this.sizeLimit = sizeLimit;
            this.countLimit = countLimit;
            cacheSize = new AtomicLong();
            calculateCacheSizeAndCacheCount();
        }

        /**
         * 计算 cacheSize和cacheCount
         */
        private void calculateCacheSizeAndCacheCount() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int size = 0;
                    File[] cachedFiles = cacheDir.listFiles();
                    if (cachedFiles != null) {
                        for (File cachedFile : cachedFiles) {
                            size += calculateSize(cachedFile);
                            mFileList.add(cachedFile);
                        }
                        cacheSize.set(size);
                    }
                }
            }).start();
        }

        private void put(File file) {
            while (mFileList.size() + 1 > countLimit) {
                removeOldestFile();
            }
            long valueSize = calculateSize(file);
            while (cacheSize.get() + valueSize > sizeLimit) {
                removeOldestFile();
            }
            cacheSize.addAndGet(valueSize);
            mFileList.add(file);
        }

        /**
         * 根据key拿到File对象
         *
         * @param key 键
         * @return File对象
         */
        private File get(String key) {
            return new File(cacheDir, key.hashCode() + "");
        }

        /**
         * 移除文件
         *
         * @param key 键
         * @return 是否remove成功
         */
        private boolean remove(String key) {
            File file = get(key);
            return remove(file);
        }

        private boolean remove(File file) {
            cacheSize.addAndGet(-file.length());
            mFileList.remove(file);
            return file.delete();
        }

        /**
         * 清空缓存文件
         */
        private void clear() {
            mFileList.clear();
            cacheSize.set(0);
            File[] files = cacheDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    f.delete();
                }
            }
        }

        /**
         * 移除旧的文件
         */
        private void removeOldestFile() {
            if (mFileList.isEmpty()) {
                return;
            }
            File oldestFile = null;
            synchronized (mFileList) {
                for (File file : mFileList) {
                    if (oldestFile == null) {
                        oldestFile = file;
                    } else {
                        if (file.lastModified() < oldestFile.lastModified()) {
                            oldestFile = file;
                        }
                    }
                }
            }
            remove(oldestFile);
        }

        private long calculateSize(File file) {
            return file.length();
        }
    }

    private static class Utils {

        /**
         * 判断缓存的String数据是否到期
         *
         * @param str
         * @return true：到期了 false：还没有到期
         */
        private static boolean isDue(String str) {
            return isDue(str.getBytes());
        }

        /**
         * 判断缓存的byte数据是否到期
         *
         * @param data
         * @return true：到期了 false：还没有到期
         */
        private static boolean isDue(byte[] data) {
            String[] strs = getDateInfoFromDate(data);
            if (strs != null && strs.length == 2) {
                String saveTimeStr = strs[0];
                while (saveTimeStr.startsWith("0")) {
                    saveTimeStr = saveTimeStr
                            .substring(1, saveTimeStr.length());
                }
                long saveTime = Long.valueOf(saveTimeStr);
                long deleteAfter = Long.valueOf(strs[1]);
                if (System.currentTimeMillis() > saveTime + deleteAfter * 1000) {
                    return true;
                }
            }
            return false;
        }

        private static String newStringWithDateInfo(int second, String strInfo) {
            return createDateInfo(second) + strInfo;
        }

        private static byte[] newByteArrayWithDateInfo(int second, byte[] data2) {
            byte[] data1 = createDateInfo(second).getBytes();
            byte[] retdata = new byte[data1.length + data2.length];
            System.arraycopy(data1, 0, retdata, 0, data1.length);
            System.arraycopy(data2, 0, retdata, data1.length, data2.length);
            return retdata;
        }

        private static String clearDateInfo(String strInfo) {
            if (strInfo != null && hasDateInfo(strInfo.getBytes())) {
                strInfo = strInfo.substring(strInfo.indexOf(mSeparator) + 1
                );
            }
            return strInfo;
        }

        private static byte[] clearDateInfo(byte[] data) {
            if (hasDateInfo(data)) {
                return copyOfRange(data, indexOf(data, mSeparator) + 1,
                        data.length);
            }
            return data;
        }

        private static boolean hasDateInfo(byte[] data) {
            return data != null && data.length > 15 && data[13] == '-'
                    && indexOf(data, mSeparator) > 14;
        }

        private static String[] getDateInfoFromDate(byte[] data) {
            if (hasDateInfo(data)) {
                String saveDate = new String(copyOfRange(data, 0, 13));
                String deleteAfter = new String(copyOfRange(data, 14,
                        indexOf(data, mSeparator)));
                return new String[]{saveDate, deleteAfter};
            }
            return null;
        }

        private static int indexOf(byte[] data, char c) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] == c) {
                    return i;
                }
            }
            return -1;
        }

        private static byte[] copyOfRange(byte[] original, int from, int to) {
            int newLength = to - from;
            if (newLength < 0) {
                throw new IllegalArgumentException(from + " > " + to);
            }
            byte[] copy = new byte[newLength];
            System.arraycopy(original, from, copy, 0,
                    Math.min(original.length - from, newLength));
            return copy;
        }

        private static final char mSeparator = ' ';

        private static String createDateInfo(int second) {
            String currentTime = System.currentTimeMillis() + "";
            while (currentTime.length() < 13) {
                currentTime = "0" + currentTime;
            }
            return currentTime + "-" + second + mSeparator;
        }

    }

}