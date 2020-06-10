package com.bench.android.core.cache;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

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

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/************************************************************************
 *@Project: packages
 *@Package_Name: com.bench.android.core.util
 *@Descriptions: 外部存储缓存管理器，不限制存储数量和大小
 *@Author: xingjiu
 *@Date: 2019/8/5
 *************************************************************************/
public class SdCardCacheManager {

    private static SdCardCacheManager manager;
    /**
     * sd卡中的存储路径，读写需要权限
     * /storage/emulated/0/$packageName/file/$fileType/$fileName
     */
    private File externalPath;
    /**
     * /storage/emulated/0/Download/$packageName/file/$fileName
     * 该目录下不需要权限
     */
    private File cacheDir;


    public static SdCardCacheManager get() {
        if (manager == null) {
            manager = new SdCardCacheManager();
        }
        return manager;
    }

    private SdCardCacheManager() {
        //sdcard内部存储
        cacheDir = new File(BaseApplication.getContext().getExternalCacheDir() + File.separator + "file");
        //sdcard外部存储
        externalPath = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)
                + File.separator + BaseApplication.getContext().getPackageName());
    }

    /**
     * 获取存储在sdcard中的文件路径，为了统一存储目录
     *
     * @param fileName   文件名,必须带后缀文件类型
     * @param isInternal 是否是sdcard内部路径
     * @return 文件路径
     */
    public File getSdcardFilePath(String fileName, boolean isInternal) {
        File dir = isInternal ? cacheDir : externalPath;
        //获取扩展名并且全部转小写
        String extension = getFormatName(fileName).toLowerCase();
        FormatEnum format = FormatEnum.getFormat(extension);
        //获取文件类型，以文件类型建文件夹
        File fileDir = new File(dir, format.type);
        checkFileDir(fileDir);
        return new File(fileDir, fileName);
    }

    /**
     * 获取存储在sdcard中的文件路径，为了统一存储目录
     * 默认是sdcard外部存储路径
     *
     * @param fileName 文件名,必须带后缀文件类型
     * @return 文件路径
     */
    public File getSdcardFilePath(String fileName) {
        return getSdcardFilePath(fileName, false);
    }

    /**
     * 文件是否存在
     * @param fileName
     * @return
     */
    public boolean isFileExists(String fileName) {
        return new File(fileName).exists();
    }

    /**
     * 检查目录，如果目录不存在，则创建目录
     *
     * @param dir 文件夹
     * @return 文件夹File
     */
    private void checkFileDir(File dir) {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("can't make dirs in "
                    + dir.getAbsolutePath());
        }
    }

    /**
     * 获取文件格式名
     */
    public static String getFormatName(String fileName) {
        //去掉首尾的空格
        fileName = fileName.trim();
        String[] s = fileName.split("\\.");
        if (s.length > 1) {
            return s[s.length - 1];
        }
        return "";
    }

    /**
     * 保存 String数据 到 缓存中,默认sdcard外部存储
     *
     * @param key   保存的key
     * @param value 保存的String数据
     */
    public void put(String key, String value) {
        put(key, value, false);
    }

    /**
     * 保存 String数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的String数据
     */
    public void put(String key, String value, boolean isInternal) {
        synchronized (SdCardCacheManager.class) {
            File dir = isInternal ? cacheDir : externalPath;
            File fileDir = new File(dir, FormatEnum.TXT.type);
            checkFileDir(fileDir);
            File file = new File(fileDir, key.hashCode() + "." + FormatEnum.TXT.formats[0]);
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
            }
        }
    }

    /**
     * 读取 String数据,默认外部存储
     *
     * @param key key值
     * @return String 数据
     */
    public String getAsString(String key) {
        return getAsString(key, false);
    }

    /**
     * 读取 String数据
     *
     * @param key        key值
     * @param isInternal 是否sdcard内部存储
     * @return String 数据
     */
    public String getAsString(String key, boolean isInternal) {
        File dir = isInternal ? cacheDir : externalPath;
        //文本存储路径
        File fileDir = new File(dir, FormatEnum.TXT.type);
        File file = new File(fileDir, key.hashCode() + "." + FormatEnum.TXT.formats[0]);
        if (!file.exists()) {
            return null;
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            StringBuilder readString = new StringBuilder();
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                readString.append(currentLine);
            }
            return readString.toString();
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
        }
    }


    /**
     * 保存 bitmap 到 缓存中,默认sdcard外部存储
     *
     * @param key   保存的key
     * @param value 保存的bitmap数据
     */
    public void put(String key, Bitmap value) {
        put(key, value, false);
    }

    /**
     * 保存 bitmap 到 缓存中
     *
     * @param key        保存的key
     * @param value      保存的bitmap数据
     * @param isInternal 是否内部sdcard存储
     */
    public void put(String key, Bitmap value, boolean isInternal) {
        put(key, BitmapUtils.bitmapToByte(value), FormatEnum.IMG, isInternal);
    }

    /**
     * 读取 bitmap 数据,默认外部存储
     *
     * @param key
     * @return bitmap 数据
     */
    public Bitmap getAsBitmap(String key) {
        return getAsBitmap(key, false);
    }

    /**
     * 读取 bitmap 数据
     *
     * @param key
     * @param isInternal 是否内部存储
     * @return bitmap 数据
     */
    public Bitmap getAsBitmap(String key, boolean isInternal) {
        byte[] bytes = getAsBinary(key, FormatEnum.IMG.formats[0], isInternal);
        if (bytes == null) {
            return null;
        }
        return BitmapUtils.byteToBitmap(bytes);
    }

    /**
     * 保存 drawable 到 缓存中
     *
     * @param key        保存的key
     * @param value      保存的drawable数据
     * @param isInternal 是否内部存储
     */
    public void put(String key, Drawable value, boolean isInternal) {
        put(key, BitmapUtils.drawableToBitmap(value), isInternal);
    }

    /**
     * 保存 drawable 到 缓存中,默认外部存储
     *
     * @param key   保存的key
     * @param value 保存的drawable数据
     */
    public void put(String key, Drawable value) {
        put(key, BitmapUtils.drawableToBitmap(value), false);
    }


    /**
     * 读取 Drawable 数据
     *
     * @param key
     * @return Drawable 数据
     */
    public Drawable getAsDrawable(String key) {
        return getAsDrawable(key, false);
    }

    /**
     * 读取 Drawable 数据
     *
     * @param key
     * @param isInternal 是否内部存储
     * @return Drawable 数据
     */
    public Drawable getAsDrawable(String key, boolean isInternal) {
        byte[] bytes = getAsBinary(key, FormatEnum.IMG.formats[0]);
        if (bytes == null) {
            return null;
        }
        return BitmapUtils.bitmapToDrawable(BitmapUtils.byteToBitmap(bytes));
    }

    /**
     * 保存 byte数据 到 缓存中,默认外部存储
     *
     * @param key      保存的key
     * @param fileType 文件类型
     * @param value    保存的数据
     */
    public void put(String key, byte[] value, FormatEnum fileType) {
        put(key, value, fileType, false);
    }

    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key        保存的key
     * @param fileType   文件类型
     * @param isInternal 是否sdcard内部存储
     * @param value      保存的数据
     */

    public void put(String key, byte[] value, FormatEnum fileType, boolean isInternal) {
        synchronized (SdCardCacheManager.class) {
            File fileDir = isInternal ? cacheDir : externalPath;
            checkFileDir(fileDir);
            String fileName = key.hashCode() + "." + (fileType.formats == null ? "" : fileType.formats[0]);
            File file = new File(fileDir, fileName);
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
                if (FormatEnum.IMG.equals(fileType)) {
                    Uri contentUri = FileProvider.getUriForFile(BaseApplication.getContext(), BaseApplication.getContext().getPackageName() + ".fileprovider", file);
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
                    BaseApplication.getContext().sendBroadcast(mediaScanIntent);
                }

            }
        }
    }

    /**
     * 获取 byte 数据，默认外部存储
     *
     * @param key      key
     * @param fileType 文件类型
     * @return byte 数据
     */
    public byte[] getAsBinary(String key, String fileType) {
        return getAsBinary(key, fileType, false);
    }

    /**
     * 获取 byte 数据
     *
     * @param key        key
     * @param fileType   文件类型
     * @param isInternal 是否内部sdcard存储
     * @return byte 数据
     */
    public byte[] getAsBinary(String key, String fileType, boolean isInternal) {
        RandomAccessFile rafile = null;
        try {
            File dir = isInternal ? cacheDir : externalPath;
            File fileDir = new File(dir, fileType);
            File file = new File(fileDir, key.hashCode() + "." + fileType);
            if (!file.exists()) {
                return null;
            }
            rafile = new RandomAccessFile(file, "r");
            byte[] byteArray = new byte[(int) rafile.length()];
            rafile.read(byteArray);
            return byteArray;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (rafile != null) {
                try {
                    rafile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存 Serializable数据到 缓存中，默认外部存储
     *
     * @param key
     * @param value
     */
    public void put(String key, Serializable value) {
        put(key, value, false);
    }

    /**
     * 保存 Serializable数据到 缓存中
     *
     * @param key        保存的key
     * @param value      保存的value
     * @param isInternal 是否内部存储
     */
    public void put(String key, Serializable value, boolean isInternal) {
        ByteArrayOutputStream baos;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            byte[] data = baos.toByteArray();
            put(key, data, FormatEnum.UNKNOWN, isInternal);
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
     * 读取 Serializable数据,默认外部存储
     *
     * @param key
     * @return
     */
    public Object getAsObject(String key) {
        return getAsObject(key, false);
    }

    /**
     * 读取 Serializable数据
     *
     * @param key
     * @param isInternal 是否内部存储
     * @return Serializable 数据
     */
    public Object getAsObject(String key, boolean isInternal) {
        byte[] data = getAsBinary(key, FormatEnum.UNKNOWN.type, isInternal);
        if (data != null) {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                bais = new ByteArrayInputStream(data);
                ois = new ObjectInputStream(bais);
                return ois.readObject();
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

}