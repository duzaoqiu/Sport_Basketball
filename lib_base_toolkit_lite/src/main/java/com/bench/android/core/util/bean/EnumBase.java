package com.bench.android.core.util.bean;

/**
 * 枚举接口
 * 
 * @author chenbug
 * @version $Id: EnumBase.java,v 0.1 2008-12-30 上午10:22:20 chenbug Exp $
 */
public interface EnumBase {

	/**
	 * 获取枚举名
	 * 
	 * @return
	 */
    String name();

	/**
	 * 获取枚举消息
	 * 
	 * @return
	 */
    String message();

	/**
	 * 获取枚举值
	 * 
	 * @return
	 */
    Number value();

}
