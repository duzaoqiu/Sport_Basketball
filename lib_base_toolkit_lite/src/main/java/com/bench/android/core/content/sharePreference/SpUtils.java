package com.bench.android.core.content.sharePreference;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * SharePreference保存对象
 *
 * @author zhouyi
 */
public final class SpUtils {

    /**
     * 获取数据
     *
     * @param clz
     * @return
     */
    public static <T> T getData(T t, Class<T> clz) {
        String className = clz.getName();
        try {
            if (t == null) {
                t = clz.newInstance();
            }
            Field[] declaredFields = t.getClass().getDeclaredFields();
            for (Field f : declaredFields) {
                if (isExclude(f)) {
                    continue;
                }
                String key = className + "_" + f.getName();
                Class<?> declaringClass = f.getType();
                String typeName = declaringClass.getSimpleName();
                f.setAccessible(true);
                switch (typeName) {
                    case "int":
                        f.set(t, SharedPreferUtil.getInstance().getInt(key, 0));
                        break;
                    case "double":
                        f.set(t, Double.parseDouble(SharedPreferUtil.getInstance().getString(key, "0")));
                        break;
                    case "short":
                        f.set(t, SharedPreferUtil.getInstance().getShort(key, (short) 0));
                        break;
                    case "byte":
                        f.set(t, SharedPreferUtil.getInstance().getByte(key, (byte) 0));
                        break;
                    case "boolean":
                        f.set(t, SharedPreferUtil.getInstance().getBoolean(key, false));
                        break;
                    case "long":
                        f.set(t, SharedPreferUtil.getInstance().getLong(key, 0));
                        break;
                    case "String":
                        f.set(t, SharedPreferUtil.getInstance().getString(key, null));
                        break;
                    case "float":
                        f.set(t, SharedPreferUtil.getInstance().getFloat(key, 0));
                        break;
                    default:
                        break;
                }
            }
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 存储数据
     *
     * @param data
     */
    public static <T> void putData(T data) {
        String className = data.getClass().getName();
        Field[] declaredFields = data.getClass().getDeclaredFields();
        int fieldLength = declaredFields.length;
        for (int i = 0; i < fieldLength; i++) {
            Field f = declaredFields[i];
            if (isExclude(f)) {
                continue;
            }
            String name = f.getName();
            String keyName = className + "_" + name;
            try {
                f.setAccessible(true);
                Object o = f.get(data);
                if (o != null) {
                    String typeName = f.getType().getSimpleName();
                    switch (typeName) {
                        case "int":
                            SharedPreferUtil.getInstance().putInt(keyName, (Integer) o);
                            break;
                        case "double":
                            SharedPreferUtil.getInstance().putDouble(keyName, (Double) o);
                            break;
                        case "short":
                            SharedPreferUtil.getInstance().putInt(keyName, (Short) o);
                            break;
                        case "byte":
                            SharedPreferUtil.getInstance().putInt(keyName, (Byte) o);
                            break;
                        case "boolean":
                            SharedPreferUtil.getInstance().putBoolean(keyName, (Boolean) o);
                            break;
                        case "long":
                            SharedPreferUtil.getInstance().putLong(keyName, (Long) o);
                            break;
                        case "String":
                            SharedPreferUtil.getInstance().putString(keyName, (String) o);
                            break;
                        case "float":
                            SharedPreferUtil.getInstance().putFloat(keyName, (Float) o);
                            break;
                        default:
                            break;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 更新数据
     *
     * @param data
     */
    public static <T> void updateData(T data) {
        putData(data);
    }

    /**
     * @param clz
     */
    public static <T> void clearAllData(Class<T> clz) {
        String className = clz.getName();
        Field[] declaredFields = clz.getDeclaredFields();
        for (Field f : declaredFields) {
            if (isExclude(f)) {
                continue;
            }
            String key = className + "_" + f.getName();
            SharedPreferUtil.getInstance().remove(key);
        }

    }

    private static boolean isExclude(Field f) {
        if ((f.getModifiers() & Modifier.FINAL) == Modifier.FINAL) {
            return true;
        }
        SPExclude annotation = f.getAnnotation(SPExclude.class);
        return annotation != null;
    }

}
