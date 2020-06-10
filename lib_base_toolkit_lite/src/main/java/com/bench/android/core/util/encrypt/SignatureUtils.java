package com.bench.android.core.util.encrypt;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 签名工具类
 * 
 * @author chenbug
 * 
 * @version $Id: SignatureUtils.java, v 0.1 2011-7-27 下午04:24:13 chenbug Exp $
 */
public class SignatureUtils {

	/**
	 * 获取签名内容
	 * 
	 * @param dataMap
	 * @return
	 */
	public static String getSignatureContent(Map<String, ?> dataMap) {
		return getSignatureContent(dataMap, null, false);
	}

	/**
	 * 获取签名内容
	 * 
	 * @param dataMap
	 * @return
	 */
	public static String getSignatureContent(Map<String, ?> dataMap, String[] filterKeys) {
		return getSignatureContent(dataMap, filterKeys, false);
	}

	/**
	 * 转换为签名用的Map
	 * 
	 * @param parameterMap
	 * @return
	 */
	public static Map<String, Object> convertToDataMap(Map<?, ?> parameterMap) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		// 获得POST 过来参数设置到新的params中
		for (Map.Entry<?, ?> entry : parameterMap.entrySet()) {
			// 过滤
			String key = ObjectUtils.toString(entry.getKey());
			if (entry.getValue() == null) {
				dataMap.put(key, null);
			}

			if (entry.getValue().getClass().isArray()) {

				String[] values = (String[]) entry.getValue();
				StringBuffer valueBuf = new StringBuffer();
				for (String value : values) {
					valueBuf.append(value).append(",");
				}
				if (valueBuf.length() >= 1) {
					valueBuf = valueBuf.deleteCharAt(valueBuf.length() - 1);
				}
				dataMap.put(key, valueBuf.toString());
			} else {
				dataMap.put(key, entry.getValue());
			}

		}
		return dataMap;
	}

	/**
	 * 获取签名内容
	 * 
	 * @param dataMap
	 * @return
	 */
	public static String getSignatureContent(Map<String, ?> dataMap, String[] filterKeys, boolean appendNull) {
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>();
		keys.addAll(dataMap.keySet());
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = ObjectUtils.toString(dataMap.get(key));
			if (!appendNull && StringUtils.isEmpty(value)) {
				continue;
			}
			if (filterKeys != null && ArrayUtils.contains(filterKeys, key)) {
				continue;
			}
			content.append((i == 0 ? "" : "&") + key + "=" + value);
		}

		return content.toString();
	}
}