/**
 * 
 */
package com.bench.android.core.lang;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 数学工具类
 * 
 * @author chenbug 2009-12-11 上午09:43:28
 * 
 */
public class MathUtils {

	public static final MathUtils INSTANCE = new MathUtils();

	/**
	 * 将dataList中的数据，平均分成resultCount份，返回
	 * 
	 * @param dataList
	 * @param resultCount
	 * @return
	 */
	public static <E> List<List<E>> average(List<E> dataList, int resultCount) {
		List<List<E>> resultList = new ArrayList<>();
		int eachSize = dataList.size() / resultCount;
		if ((eachSize * resultCount) / 2 < dataList.size() / 2) {
			eachSize = eachSize + 1;
		}
		for (int i = 0; i < resultCount; i++) {
			List<E> eachList = new ArrayList<>();
			resultList.add(eachList);
			int endIndex = (i + 1) * eachSize;
			endIndex = endIndex >= dataList.size() ? dataList.size() : endIndex;
			if (i + 1 == resultCount) {
				endIndex = dataList.size();
			}
			if (i * eachSize < dataList.size()) {
				eachList.addAll(dataList.subList(i * eachSize, endIndex));
			}
		}
		return resultList;
	}

	/**
	 * 将dataList中的数据分割,平均每个part份
	 * 
	 * @param dataList
	 * @return
	 */
	public static <E> List<List<E>> part(List<E> dataList, int part) {
		int count = dataList.size() / part;
		if (count * part != dataList.size()) {
			count = count + 1;
		}
		return average(dataList, count);
	}

	/**
	 * 百分比计算
	 * 
	 * @param a
	 * @param scale
	 * @return
	 */
	public static String percentage(Number a, int scale) {
		if (a == null)
			return null;
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(scale);
		return nf.format(a);
	}


	/**
	 * 百分比计算,是否去除最后一个0
	 *
	 * @param a
	 * @param b
	 * @param scale
	 * @return
	 */
	public static String percentage(Number a, Number b, int scale, boolean clearLastZero) {
		if (a == null || b == null)
			return null;
		String percentage = percentage(a.doubleValue() / b.doubleValue(), scale);
		if (clearLastZero) {
			if ("NaN".equals(percentage)) {
				return "0%";
			} else {
				if (percentage.endsWith("%")) {
					percentage = percentage.substring(0, percentage.length() - 1);
				}
				for (int i = 0; i < 10 && percentage.indexOf(".") > 0 && percentage.endsWith("0"); i++) {
					percentage = percentage.substring(0, percentage.length() - 1);
				}
				if (percentage.endsWith(".")) {
					percentage = percentage.substring(0, percentage.length() - 1);
				}
				return percentage + "%";
			}
		} else {
			return percentage;
		}
	}



	/**
	 * 百分比计算
	 * 
	 * @param a
	 * @param b
	 * @param scale
	 * @return
	 */
	public static String percentage(Number a, Number b, int scale) {
		if (a == null || b == null)
		{
			return null;
		}
		if(b.equals(0)){
			return "";
		}
		return percentage(a.doubleValue() / b.doubleValue(), scale);
	}


	/**
	 * 小数点保留,a除以b，保留scale位
	 * 
	 * @param a
	 * @param b
	 * @param scale
	 * @return
	 */
	public static double scale(Number a, Number b, int scale) {
		return scale(a.doubleValue() / b.doubleValue(), scale);
	}

	/**
	 * 小数点保留，保留scale位,如果clearLastZero为true,则清除最后的0
	 * 
	 * @param a
	 * @param scale
	 * @param clearLastZero
	 * @return
	 */
	public static String scale(Number a, int scale, boolean clearLastZero) {
		String scaleValue = scaleString(a.doubleValue(), scale);
		if (!clearLastZero) {
			return scaleValue;
		}
		for (int i = 0; i < 10 && scaleValue.indexOf(".") > 0
				&& scaleValue.endsWith("0"); i++) {
			scaleValue = scaleValue.substring(0, scaleValue.length() - 1);
		}
		if (scaleValue.endsWith(".")) {
			scaleValue = scaleValue.substring(0, scaleValue.length() - 1);
		}
		return scaleValue;
	}

	/**
	 * 小数点保留,a除以b，保留scale位,如果clearLastZero为true,则清除最后的0
	 * 
	 * @param a
	 * @param b
	 * @param scale
	 * @param clearLastZero
	 * @return
	 */
	public static String scale(Number a, Number b, int scale,
			boolean clearLastZero) {
		return scale(a.doubleValue() / b.doubleValue(), scale, clearLastZero);
	}

	/**
	 * 小数点保留，数字a保留scale位
	 * 
	 * @param a
	 * @param scale
	 * @return
	 */
	public static double scale(Number a, int scale) {
		Double flag = null;
		String text = a.toString();
		BigDecimal bd = new BigDecimal(text).setScale(scale,
				BigDecimal.ROUND_HALF_UP);
		flag = bd.doubleValue();
		return flag;

	}

	/**
	 * 小数点保留，数字a保留scale位，不足位用0补齐
	 * 
	 * @param a
	 * @param scale
	 * @return
	 */
	public static String scaleString(Number a, int scale) {
		return scaleString(new BigDecimal(a.toString()).setScale(scale,
				BigDecimal.ROUND_HALF_UP), scale);
	}

	public static String scaleString(Double a, int scale) {
		return scaleString(
				new BigDecimal(a).setScale(scale, BigDecimal.ROUND_HALF_UP),
				scale);
	}

	public static String scaleString(BigDecimal bd, int scale, boolean fillZero) {
		return scaleString(bd.doubleValue(), scale, fillZero);
	}

	/**
	 * 小数点保留，数字a保留scale位，fillZeroe为true时，不足位用0补齐
	 * 
	 * @param value
	 * @param scale
	 * @param fillZero
	 * @return
	 */
	public static String scaleString(double value, int scale, boolean fillZero) {

		StringBuffer buf = new StringBuffer("0");
		if (scale > 0) {
			buf.append(".");
			for (int i = 0; i < scale; i++) {
				if (fillZero) {
					buf.append("0");
				} else {
					buf.append("#");
				}
			}
		}

		DecimalFormat g = new DecimalFormat(buf.toString());
		return g.format(value);
	}

	/**
	 * 小数点保留，数字a保留scale位，不足位用0补齐
	 * 
	 * @param bd
	 * @param scale
	 * @return
	 */
	public static String scaleString(BigDecimal bd, int scale) {
		return scaleString(bd, scale, true);
	}

	/**
	 * 四舍六入5成双 <br>
	 * 保留小数点的后一位如果大于5则进1，小于5则舍去 等于5 则看前一位如果奇数 则进1，如果是偶数则舍去，如果5后面有，则无论奇偶都进位
	 * 
	 * @param a
	 * @param scale
	 *            保留小数位
	 * @return
	 */
	public static double scale4S6R5S(Number a, int scale) {
		NumberFormat nfFormat = NumberFormat.getInstance();
		nfFormat.setMaximumFractionDigits(scale);
		return NumberUtils.toDouble(nfFormat.format(a).replaceAll(",", ""));
	}

	public static double scale4S6R5JE(double a, int scale) {
		String str = MathUtils.scaleString(a, 4);
		return scale4S6R5JEInner(a, scale, str);
	}

	/**
	 * 四舍六入5奇偶，保留小数点的后一位如果大于5则进1，小于5则舍去 等于5 则看前一位如果奇数 则进1，如果是偶数则舍去,即便5后面有数字，也不理
	 * 
	 * @param a
	 * @param scale
	 * @return
	 */
	public static double scale4S6R5JE(Number a, int scale) {
		String str = MathUtils.scaleString(a, 4);
		return scale4S6R5JEInner(a, scale, str);
	}

	private static double scale4S6R5JEInner(Number a, int scale, String str) {
		int position = StringUtils.indexOf(str, ".");
		if (position < 0 || position + scale + 1 >= str.length()) {
			return a.doubleValue();
		} else {
			int lastInt = NumberUtils.toInt(str.substring(position + scale + 1,
					position + scale + 2));
			if (lastInt <= 4) {
				return NumberUtils.toDouble(str.substring(0, position + scale
						+ 1));
			} else if (lastInt == 5) {
				// 偶数
				if (NumberUtils.toInt(str.substring(position + scale, position
						+ scale + 1)) % 2 == 0) {
					return NumberUtils.toDouble(str.substring(0, position
							+ scale + 1));
				}
				// 奇数
				else {
					return MathUtils.scale(
							NumberUtils.toDouble(str.substring(0, position
									+ scale + 1))
									+ Math.pow(0.1, scale), scale);
				}
			} else {
				return MathUtils.scale(
						NumberUtils.toDouble(str.substring(0, position + scale
								+ 1))
								+ Math.pow(0.1, scale), scale);
			}
		}
	}

	/**
	 * 小数点保留，数字a保留scale位，直接舍去<br>
	 * 
	 * <pre>
	 * scaleRundDown(65.5856, 2) = 65.58
	 * </pre>
	 * 
	 * @param a
	 * @param scale
	 * @return
	 */
	public static double scaleRundDown(Number a, int scale) {
		Double flag = null;
		String text = a.toString();

        BigDecimal bd = new BigDecimal(text).setScale(scale,BigDecimal.ROUND_DOWN);
		flag = bd.doubleValue();
		return flag;
	}

	/**
	 * 去除百分数最后一个“0”
	 *
	 * @return
	 */
	public static String percentageClearLastZero(String percentage) {

		if (percentage == null) {
			return null;
		}

		if (percentage.endsWith("%")) {
			percentage = percentage.substring(0, percentage.length() - 1);
		}
		for (int i = 0; i < 10 && percentage.indexOf(".") > 0 && percentage.endsWith("0"); i++) {
			percentage = percentage.substring(0, percentage.length() - 1);
		}
		if (percentage.endsWith(".")) {
			percentage = percentage.substring(0, percentage.length() - 1);
		}

		return percentage + "%";

	}

	/**
	 * 计算排列个数
	 * 
	 * @param n
	 * @param m
	 * @return
	 */
	public static int permutationCount(int n, int m) {

		if (n <= 0 || m <= 0 || m > n) {
			return 0;
		}

		int result = 1;
		for (int i = n; i > n - m; i--) {
			result *= i;
		}
		return result;

	}

	public static double sin(double a) {
		return Math.sin(a);
	}

	public static double cos(double a) {
		return Math.cos(a);
	}

	public static double tan(double a) {
		return Math.tan(a);
	}

	public static double asin(double a) {
		return Math.asin(a);
	}

	public static double acos(double a) {
		return Math.acos(a);
	}

	public static double atan(double a) {
		return Math.atan(a);
	}

	public static double toRadians(double angdeg) {
		return Math.toRadians(angdeg);
	}

	public static double toDegrees(double angrad) {
		return Math.toDegrees(angrad);
	}

	public static double exp(double a) {
		return Math.exp(a);
	}

	public static double log(double a) {
		return Math.log(a);
	}

	public static double log10(double a) {
		return Math.log10(a);
	}

	public static double sqrt(double a) {
		return Math.sqrt(a);
	}

	public static double cbrt(double a) {
		return Math.cbrt(a);
	}

	public static double IEEEremainder(double f1, double f2) {
		return Math.IEEEremainder(f1, f2);
	}

	public static double ceil(double a) {
		return Math.ceil(a);
	}

	public static double floor(double a) {
		return Math.floor(a);
	}

	public static double rint(double a) {
		return Math.rint(a);
	}

	public static double atan2(double y, double x) {
		return Math.atan2(x, y);
	}

	public static double pow(double a, double b) {
		return Math.pow(a, b);
	}

	public static int round(float a) {
		return Math.round(a);
	}

	public static long round(double a) {
		return Math.round(a);
	}

	public static double random() {
		return Math.random();
	}

	public static int abs(int a) {
		return Math.abs(a);
	}

	public static long abs(long a) {
		return Math.abs(a);
	}

	public static float abs(float a) {
		return Math.abs(a);
	}

	public static double abs(double a) {
		return Math.abs(a);
	}

	public static int max(int a, int b) {
		return Math.max(a, b);
	}

	public static long max(long a, long b) {
		return Math.max(a, b);
	}

	public static float max(float a, float b) {
		return Math.max(a, b);
	}

	public static double max(double a, double b) {
		return Math.max(a, b);
	}

	public static int min(int a, int b) {
		return Math.min(a, b);
	}

	public static long min(long a, long b) {
		return Math.min(a, b);
	}

	public static float min(float a, float b) {
		return Math.min(a, b);
	}

	public static double min(double a, double b) {
		return Math.min(a, b);
	}

	public static double ulp(double d) {
		return Math.ulp(d);
	}

	public static float ulp(float f) {
		return Math.ulp(f);
	}

	public static double signum(double d) {
		return Math.signum(d);
	}

	public static float signum(float f) {
		return Math.signum(f);
	}

	public static double sinh(double x) {
		return Math.sinh(x);
	}

	public static double cosh(double x) {
		return Math.cosh(x);
	}

	public static double tanh(double x) {
		return Math.tanh(x);
	}

	public static double hypot(double x, double y) {
		return Math.hypot(x, y);
	}

	public static double expm1(double x) {
		return Math.expm1(x);
	}

	public static double log1p(double x) {
		return Math.log1p(x);
	}

	public static double copySign(double magnitude, double sign) {
		return Math.copySign(magnitude, sign);
	}

	public static float copySign(float magnitude, float sign) {
		return Math.copySign(magnitude, sign);
	}

	public static int getExponent(float f) {
		return Math.getExponent(f);
	}

	public static int getExponent(double d) {
		return Math.getExponent(d);
	}

	public static double nextAfter(double start, double direction) {
		return Math.nextAfter(start, direction);
	}

	public static float nextAfter(float start, double direction) {
		return Math.nextAfter(start, direction);
	}

	public static double nextUp(double d) {
		return Math.nextUp(d);
	}

	public static float nextUp(float f) {
		return Math.nextUp(f);
	}

	public static double scalb(double d, int scaleFactor) {
		return Math.scalb(d, scaleFactor);
	}

	public static float scalb(float f, int scaleFactor) {
		return Math.scalb(f, scaleFactor);
	}

	/**
	 * 转换成中国折扣值
	 * 
	 * @param discount
	 * @return
	 */
	public static final String toCnDiscount(Number discount, int scale) {
		return MathUtils
				.scaleString(discount.doubleValue() * 10d, scale, false);
	}

	/**
	 * 转换成中国折扣值
	 * 
	 * @param discount
	 * @return
	 */
	public static final String toCnRundDownDiscount(Number discount, int scale) {
		return MathUtils.scaleString(MathUtils.scaleRundDown(
				discount.doubleValue() * 10d, scale + 1), scale);
	}

	/**
	 * 对BigDecimal型的值按指定取整方式取整。
	 * 
	 * @param val
	 *            待取整的BigDecimal值
	 * @param roundingMode
	 *            取整方式
	 * 
	 * @return 取整后的long型值
	 */
	public static long rounding(BigDecimal val, int roundingMode) {
		return val.setScale(0, roundingMode).longValue();
	}

}
