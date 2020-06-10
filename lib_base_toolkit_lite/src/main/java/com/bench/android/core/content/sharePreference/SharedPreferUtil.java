package com.bench.android.core.content.sharePreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.bench.android.core.util.AesEncodeUtil;
import com.google.gson.Gson;

import java.util.Map;

/**
 * @author xiaolu
 */
public class SharedPreferUtil {

    private static SharedPreferUtil instance;
    private Context context;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private String prefsName = "";

    private SharedPreferUtil(Context context, String prefsName) {
        this.context = context.getApplicationContext();
        this.prefsName = prefsName;
    }

    public static void init(Context ctx, @Nullable String prefsName) {
        if (instance == null) {
            instance = new SharedPreferUtil(ctx, prefsName);
        }
    }

    public static SharedPreferUtil getInstance() {
        if (instance == null) {
            throw new IllegalArgumentException("SharedPreferUtil没被初始化");
        }
        instance.initSettings();
        instance.initEditor();
        return instance;
    }

    private void initSettings() {
        if (this.settings == null) {
            if (TextUtils.isEmpty(prefsName)) {
                prefsName = context.getPackageName();
            }
            this.settings = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        }
    }

    private void initEditor() {
        if (this.editor == null) {
            this.editor = this.settings.edit();
        }
    }

    /**
     * 保存一个对象
     *
     * @param key
     * @param object
     * @return
     */
    public SharedPreferUtil putObject(String key, Object object) {
        if (object == null) {
            return this;
        }
        String value = new Gson().toJson(object);
        putString(key, value);
        return this;
    }

    public SharedPreferUtil putString(String key, String value) {
        this.editor.putString(key, value == null ? "" : value).commit();
        return this;
    }

    public SharedPreferUtil putStringWithEncrypt(String key, String value) {
        key = AesEncodeUtil.encrypt(key);
        value = AesEncodeUtil.encrypt(value);
        return putString(key, value);
    }

    public SharedPreferUtil putBoolean(String key, boolean value) {
        this.editor.putBoolean(key, value).commit();
        return this;
    }

    public SharedPreferUtil putBooleanWithEncrypt(String key, boolean value) {
        key = AesEncodeUtil.encrypt(key);
        String v = AesEncodeUtil.encrypt(String.valueOf(value));
        return putString(key, v);
    }

    public SharedPreferUtil putInt(String key, int value) {
        this.editor.putInt(key, value).commit();
        return this;
    }

    public SharedPreferUtil putIntWithEncrypt(String key, int value) {
        key = AesEncodeUtil.encrypt(key);
        String v = AesEncodeUtil.encrypt(String.valueOf(value));
        return putString(key, v);
    }


    public SharedPreferUtil putDouble(String key, double value) {
        this.editor.putString(key, String.valueOf(value)).commit();
        return this;
    }

    public SharedPreferUtil putDoubleWithEncrypt(String key, double value) {
        key = AesEncodeUtil.encrypt(key);
        String v = AesEncodeUtil.encrypt(String.valueOf(value));
        return putString(key, v);
    }

    public SharedPreferUtil putLong(String key, long value) {
        this.editor.putLong(key, value).commit();
        return this;
    }

    public SharedPreferUtil putLongWithEncrypt(String key, long value) {
        key = AesEncodeUtil.encrypt(key);
        String v = AesEncodeUtil.encrypt(String.valueOf(value));
        return putString(key, v);
    }

    public SharedPreferUtil putFloat(String key, float value) {
        this.editor.putFloat(key, value).commit();
        return this;
    }

    public SharedPreferUtil putFloatWithEncrypt(String key, float value) {
        key = AesEncodeUtil.encrypt(key);
        String v = AesEncodeUtil.encrypt(String.valueOf(value));
        return putString(key, v);
    }


    public SharedPreferUtil remove(String key) {
        this.editor.remove(key).commit();
        return this;
    }

    public SharedPreferUtil removeWithEncrypt(String key) {
        this.editor.remove(AesEncodeUtil.encrypt(key)).commit();
        return this;
    }

    public String getString(String key, String defValue) {
        return this.settings.getString(key, defValue);
    }

    public String getStringWithEncrypt(String key, String defValue) {
        key = AesEncodeUtil.encrypt(key);
        return AesEncodeUtil.decrypt(settings.getString(key, AesEncodeUtil.encrypt(defValue)));
    }

    public boolean getBoolean(String key, boolean defValue) {
        return this.settings.getBoolean(key, defValue);
    }

    public boolean getBooleanWithEncrypt(String key, boolean defValue) {
        return Boolean.parseBoolean(getStringWithEncrypt(key, String.valueOf(defValue)));
    }

    public long getLong(String key, long defValue) {
        return this.settings.getLong(key, defValue);
    }

    public long getLongWithEncrypt(String key, long defValue) {
        return Long.parseLong(getStringWithEncrypt(key, String.valueOf(defValue)));
    }


    public boolean contains(String key) {
        return this.settings.contains(key);
    }

    public boolean containsWithEncrypt(String key) {
        return this.settings.contains(AesEncodeUtil.encrypt(key));
    }

    /**
     * 获取一个对象
     *
     * @param key
     * @param clz
     * @param <T>
     * @return
     */
    public <T> T getObject(String key, Class<T> clz) {
        String value = getString(key, null);
        if (value == null) {
            return null;
        }
        T t1 = new Gson().fromJson(value, clz);
        return t1;
    }

    /**
     * 获取一个对象
     *
     * @param key
     * @param clz
     * @param <T>
     * @return
     */
    public <T> T getObjectWithEncrypt(String key, Class<T> clz) {
        String value = getStringWithEncrypt(key, null);
        if (value == null) {
            return null;
        }
        T t1 = new Gson().fromJson(value, clz);
        return t1;
    }

    public int getInt(String key, int defValue) {
        return this.settings.getInt(key, defValue);
    }

    public long getIntWithEncrypt(String key, int defValue) {
        return Integer.parseInt(getStringWithEncrypt(key, String.valueOf(defValue)));
    }

    public byte getByte(String key, byte defValue) {
        return (byte) this.settings.getInt(key, defValue);
    }

    public byte getByteWithEncrypt(String key, byte defValue) {
        return (byte) getIntWithEncrypt(key, defValue);
    }

    public short getShort(String key, short defValue) {
        return (short) this.settings.getInt(key, defValue);
    }

    public short getShortWithEncrypt(String key, short defValue) {
        return (short) getIntWithEncrypt(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return this.settings.getFloat(key, defValue);
    }

    public float getFloatWithEncrypt(String key, float defValue) {
        return Float.parseFloat(getStringWithEncrypt(key, String.valueOf(defValue)));
    }

    //返回所有的键值对
    public Map<String, ?> getAll() {
        return this.settings.getAll();
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
