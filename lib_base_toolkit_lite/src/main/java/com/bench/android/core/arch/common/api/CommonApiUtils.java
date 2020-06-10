package com.bench.android.core.arch.common.api;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zhouyi
 */
public class CommonApiUtils {

    public static JSONObject generateError(String message) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("detailMessage", message);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
