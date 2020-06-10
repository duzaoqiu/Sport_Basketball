package com.bench.android.core.util.encrypt;


import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * 
 * 
 * @author leslie
 * 
 * @version $Id: SercetUtil.java, v 0.1 2014年5月23日 下午7:17:42 leslie Exp $
 */
public final class SecurityUtil {

	/**
	 * 
	 * @param paramMap
	 * @param appSercet
	 * @return
	 */
	public static String md5Sign(String appSercet, Map<String, String> paramMap, String charset) {

		if (StringUtils.isBlank(charset)) {
			charset = Charset.defaultCharset().toString();
		}

		//写死编码,和服务端保持一致
		String sign = Md5Encrypt.md5(appSercet, SignatureUtils.getSignatureContent(paramMap), "utf-8");
		return sign;
	}

}
