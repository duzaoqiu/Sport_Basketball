package com.bench.android.core.app.activity;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * EasyActivityResult
 *
 * @author fangyuan
 * @date 2019/5/27
 */
public class EasyActivityResult {
    
    public interface OnActivityResultListener{
        /**
         *
         * @param resultCode
         * @param data
         */
        void onActivityResult(int resultCode, Intent data);
    }
    
    private static Map<Fragment, Map<Integer, OnActivityResultListener>> sFragmentContainer =
            new HashMap<>();
    
    private static Map<Activity, Map<Integer, OnActivityResultListener>> sActivityContainer =
            new HashMap<>();
    
    public static void startActivity(Fragment fragment, Intent intent,
            OnActivityResultListener listener) {
        if (listener == null) {
            return;
        }
        int requestCode = new Random().nextInt(0x0000FFFF);
        HashMap<Integer, OnActivityResultListener> map = new HashMap<>();
        map.put(requestCode, listener);
        sFragmentContainer.put(fragment, map);
        fragment.startActivityForResult(intent, requestCode);
    }
    
    public static void startActivity(Activity activity, Intent intent,
            OnActivityResultListener listener) {
        if (listener == null) {
            return;
        }
        int requestCode = new Random().nextInt(0x0000FFFF);
        
        if (sActivityContainer.containsKey(activity)) {
            Map<Integer, OnActivityResultListener> map = sActivityContainer.get(activity);
            map.put(requestCode, listener);
        } else {
            HashMap<Integer, OnActivityResultListener> map = new HashMap<>();
            map.put(requestCode, listener);
            sActivityContainer.put(activity, map);
        }
        activity.startActivityForResult(intent, requestCode);
    }
    
    public static void dispatch(Fragment fragment, int requestCode, int resultCode, Intent data) {
        if (!sFragmentContainer.containsKey(fragment)) {
            return;
        }
        Map<Integer, OnActivityResultListener> map = sFragmentContainer.get(fragment);
        if (!map.containsKey(requestCode)) {
            sFragmentContainer.remove(fragment);
        }
        OnActivityResultListener listener = map.get(requestCode);
        if (listener == null) {
            return;
        }
        listener.onActivityResult(resultCode, data);
    }
    
    public static void dispatch(Activity activity, int requestCode, int resultCode, Intent data) {
        if (!sActivityContainer.containsKey(activity)) {
            return;
        }
        Map<Integer, OnActivityResultListener> map = sActivityContainer.get(activity);
        if (!map.containsKey(requestCode)) {
            return;
        }
        OnActivityResultListener listener = map.get(requestCode);
        if (listener != null) {
            listener.onActivityResult(resultCode, data);
        }
    }
    
    public static void releaseFragment(Fragment fragment) {
        if (!sFragmentContainer.containsKey(fragment)) {
            return;
        }
        Map<Integer, OnActivityResultListener> map = sFragmentContainer.get(fragment);
        if (!map.isEmpty()) {
            map.clear();
        }
        sFragmentContainer.remove(fragment);
    }
    
    public static void releaseActivity(Activity activity) {
        if (!sFragmentContainer.containsKey(activity)) {
            return;
        }
        Map<Integer, OnActivityResultListener> map = sActivityContainer.get(activity);
        if (map!=null && !map.isEmpty()) {
            map.clear();
        }
        sActivityContainer.remove(activity);
    }
}
