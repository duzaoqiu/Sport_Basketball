package com.bench.android.core.util;


import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by lingjiu on 2017/3/29.
 */
public class TimeUtils {
    /**
     * 长时间格式
     */
    public static String LONG_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String LONG_FORMAT_NO_LINE = "yyyyMMddHHmmss";

    public static String FORMAT_Y_M_D = "yyyy/MM/dd";

    public static String FORMAT_M_D = "MM/dd";

    public static String DATEFORMAT = "yyyy-MM-dd";
    public static String DATEFORMAT_POINT = "yyyy.MM.dd";

    public static String DATE_FORMAT_NO_LINE = "yyyyMMdd";

    public static String TIMEFORMAT = "HH:mm";

    public static String MATCH_FORMAT = "MM-dd HH:mm";

    public static String COLLECT_FORMAT = "MM月dd日 HH:mm";

    public static String PROGRAM_FORMAT = "MM月dd日";
    public static String CHANNEL_FORMAT = "MM-dd";

    public static String NIAN_YUE_RI = "yyyy-MM-dd";
    public static String SHI_FEN_MIAO = "HH:mm:ss";
    public final static long ONE_DAY_SECONDS = 86400;

    public static boolean isValidFormat(String format, @Nullable String value) {
        if (value == null) {
            return false;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(value);
            return Objects.equals(value, sdf.format(date));
        } catch (Exception ex) {
            return false;
        }
    }

    public static String timeStrFormat(String time) {
        return time.substring(0, 4).concat("-")
                .concat(time.substring(4, 6)).concat("-")
                .concat(time.substring(6));
    }

    public static String StrToYMD(String time) {
        return time.substring(0, 10);
    }

    public static String timeStrFormatSplitDay(String time) {
        return time.substring(0, 4).concat("-")
                .concat(time.substring(4, 6));
    }

    public static int differMinutes(String now, String gmtEnd) {
        DateFormat df = new SimpleDateFormat(LONG_DATEFORMAT, Locale.CHINA);
        int differMinutes = 0;
        try {
            Date d1 = df.parse(now);
            Date d2 = df.parse(gmtEnd);
            long differ = d1.getTime() - d2.getTime();
            long min = (differ) / (1000 * 60);
            differMinutes = (int) min;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return differMinutes;
    }

    /**
     * @param time 传入年份与月份
     * @return 得到一个该月的最后一天
     */
    public static ArrayList<String> getFristLastDay(String time) {
        ArrayList<String> list = new ArrayList<>();
        list.add(timeStrFormatSplitDay(time).concat("-").concat("01"));
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.valueOf(time.substring(0, 4)));
        c.set(Calendar.MONTH, Integer.valueOf(time.substring(4, 6)) - 1);//1月是0,从0开始计算
        list.add(timeStrFormatSplitDay(time).concat("-").concat(String.valueOf(c.getActualMaximum(Calendar.DAY_OF_MONTH))));
        return list;
    }

    public static long timeStrToLong(String time) {
        Date date = stringToDate(time, LONG_DATEFORMAT);
        if (date == null) {
            return System.currentTimeMillis();
        }
        return date.getTime();
    }

    /**
     * 群聊消息列表时间规则
     */
    public static String formatMessageTime(String nowStr, String createStr) {
        if (TextUtils.isEmpty(nowStr) || TextUtils.isEmpty(createStr)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(LONG_DATEFORMAT);
        Date nowDate = sdf.parse(nowStr, new ParsePosition(0));
        Date createDate = sdf.parse(createStr, new ParsePosition(0));
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(nowDate);
        Calendar createCal = Calendar.getInstance();
        createCal.setTime(createDate);

        int nowYear = nowCal.get(Calendar.YEAR);
        int nowMonth = nowCal.get(Calendar.MONTH) + 1;
        int nowDay = nowCal.get(Calendar.DAY_OF_MONTH);

        int createYear = createCal.get(Calendar.YEAR);
        int createMonth = createCal.get(Calendar.MONTH) + 1;
        int createDay = createCal.get(Calendar.DAY_OF_MONTH);

        if (nowYear == createYear && nowMonth == createMonth && nowDay == createDay) {
            return LibAppUtil.numAddFrontZero(createCal.get(Calendar.HOUR_OF_DAY)) + ":" + LibAppUtil.numAddFrontZero(createCal.get(Calendar.MINUTE));
        }
        if (nowYear == createYear && nowMonth == createMonth && nowDay == (createDay + 1)) {
            return "昨天";
        }
        if (nowYear == createYear) {
            if (nowMonth == createMonth && nowDay <= createDay + 6) {
                return "星期" + getDate(createStr, LONG_DATEFORMAT);
            } else if (nowMonth <= createMonth + 1) {
                nowCal.add(Calendar.MONTH, -1);// 6.30   7.5  30
                int maximum = nowCal.getActualMaximum(Calendar.DAY_OF_MONTH);
                if (nowDay + maximum <= createDay + 6) {
                    return "星期" + getDate(createStr, LONG_DATEFORMAT);
                } else {
                    return LibAppUtil.numAddFrontZero(createMonth) + "-" + LibAppUtil.numAddFrontZero(createDay);
                }
            } else {
                return LibAppUtil.numAddFrontZero(createMonth) + "-" + LibAppUtil.numAddFrontZero(createDay);
            }
        }
        return createYear + "-" + LibAppUtil.numAddFrontZero(createMonth) + "-" + LibAppUtil.numAddFrontZero(createDay);
    }

    public static String getDate(String str, String format) {
        Date date;
        try {
            date = new SimpleDateFormat(format, Locale.CHINA).parse(str);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            switch ((cal.get(Calendar.DAY_OF_WEEK) - 1)) {
                case -1:
                    return "日";
                case 1:
                    return "一";
                case 2:
                    return "二";
                case 3:
                    return "三";
                case 4:
                    return "四";
                case 5:
                    return "五";
                case 6:
                    return "六";
                default:
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "日";
    }

    /**
     * 适用于服务器给的禁言时间距离当前时间  几天 几小时 几分
     * <p>
     * 如果剩余时间不足一天但大于1小时，则显示小时 分
     * 如果剩余时间不足1小时 则只显示分钟即可
     * 当剩余时间为0 也即gmtExpired 小于等于当前时间时，解禁。
     *
     * @param gmtExpired
     * @return
     */
    public static String getForbiddenTime(String gmtExpired) {
        long forbiddenTimeMills = getTimeUnix(gmtExpired);
        long nowTimeMills = Calendar.getInstance().getTimeInMillis();
        if (forbiddenTimeMills <= nowTimeMills) {
            return "0";
        }
        long diff = (forbiddenTimeMills - nowTimeMills) / 1000;
        int day = (int) (diff / 24 / 3600);
        int hour = (int) ((diff - day * 24 * 3600) / 3600);
        int min = (int) (diff - (day * 24 * 3600) - (hour * 3600)) / 60;
        String h = hour > 9 ? +hour + "" : ("0" + hour);
        if (day > 0 && hour > 0) {
            return day + "天" + h + "小时" + min + "分";
        }
        if (day > 0) {
            return day + "天" + min + "分";
        }
        if (hour > 0) {
            return h + "小时" + min + "分";
        } else if (min > 0) {
            return min + "分";
        } else if (diff > 0) {
            return "1分";
        } else {
            return "0";
        }
    }

    /**
     * 将当前时间减去开始时间返回的毫秒数
     *
     * @param dateStr
     * @return
     */
    public static long getTimeUnix(String dateStr) {
        DateFormat df = new SimpleDateFormat(LONG_DATEFORMAT, Locale.CHINA);
        try {
            return df.parse(dateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getDiffSecTwoDateStr(String now, String gmtEnd) {
        DateFormat df = new SimpleDateFormat(LONG_DATEFORMAT, Locale.CHINA);
        if (TextUtils.isEmpty(now) || TextUtils.isEmpty(gmtEnd)) {
            return 0;
        }
        try {
            Date d1 = df.parse(now);
            Date d2 = df.parse(gmtEnd);
            long diff = d1.getTime() - d2.getTime();
            return diff / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 1.先判断两个消息创建时间 是否属于同一天，如果是同一天且时间戳相差1分钟------>返回格式：HH:mm（10:20）
     * 2.如果不属于同一天且时间戳相差小于24*3600*1000 毫秒,返回格式：昨天 HH:mm (昨天10:20)
     * 3.如果时间戳相差大于1天，且小于7天24*3600*1000*7 毫秒，返回格式:周一 HH:mm  \周二 HH:mm  \周三 HH:mm (周三10:20)
     * 4.如果时间戳相差大于7天且小于一年24*3600*1000*当年总天数 毫秒 返回 MM-dd HH：mm(04-10 10:20)
     * 5.如果时间戳相差大于一年 返回 yyyy-MM-dd HH：mm(2017-04-10 10:20)
     *
     * @param timeCreated 消息创建时间
     * @return
     */

    public static String getTimeLine(String timeCreated) {
        if (TextUtils.isEmpty(timeCreated)) {
            return "";
        }
        Calendar curInstance = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();

        try {
            SimpleDateFormat format = new SimpleDateFormat(LONG_DATEFORMAT, Locale.CHINA);
            Date date = format.parse(timeCreated);
            calendar.setTime(date);

            int year = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH) + 1;
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);


            String minStr = (min > 9 ? min + "" : "0" + min);
            String hourStr = (h > 9 ? h + "" : "0" + h);
            String monthStr = (m > 9 ? m + "" : "0" + m);
            String dayStr = (d > 9 ? d + "" : "0" + d);

            int yearNow = curInstance.get(Calendar.YEAR);
            int mNow = curInstance.get(Calendar.MONTH) + 1;
            int dNow = curInstance.get(Calendar.DAY_OF_MONTH);

            if (year == yearNow && m == mNow && dNow == d) {
                return hourStr + ":" + minStr;
            }

            if (year == yearNow && m == mNow && mNow == m && (d + 1) == dNow) {//1  8
                return "昨天 " + hourStr + ":" + minStr;

            } else if (year == yearNow) {//&& mNow == m && (dNow + 6) >= d
                if (mNow == m && dNow <= d + 6) {
                    String week = "星期" + getDate(timeCreated, LONG_DATEFORMAT);
                    return week + hourStr + ":" + minStr;
                } else if (mNow <= m + 1) {
                    curInstance.add(Calendar.MONTH, -1);// 6.30   7.5  30
                    int maximum = curInstance.getActualMaximum(Calendar.DAY_OF_MONTH);
                    if (dNow + maximum <= d + 6) {
                        String week = "星期" + getDate(timeCreated, LONG_DATEFORMAT);
                        return week + hourStr + ":" + minStr;
                    } else {
                        return monthStr
                                + "-"
                                + dayStr
                                + " "
                                + hourStr + ":" + minStr;
                    }
                } else {
                    return monthStr
                            + "-"
                            + dayStr
                            + " "
                            + hourStr + ":" + minStr;
                }
            } else {
                return year
                        + "-"
                        + monthStr
                        + "-"
                        + dayStr
                        + " "
                        + hourStr + ":" + minStr;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 今天显示 今日HH:MM
     * 今年显示 M-D HH:MM
     * 非今年显示 Y-M-D HH:MM
     *
     * @param time yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatTime(String time) {
        Calendar now = Calendar.getInstance(Locale.CHINA);
        Calendar end = TimeUtils.stringToCalendar(time, TimeUtils.LONG_DATEFORMAT);
        if (now.get(Calendar.DAY_OF_YEAR) == end.get(Calendar.DAY_OF_YEAR)) {
            //今天
            return "今日" + TimeUtils.calendarToString(end, TimeUtils.TIMEFORMAT);
        } else if (now.get(Calendar.YEAR) == end.get(Calendar.YEAR)) {
            //今年
            return TimeUtils.dateToString(end.getTime(), COLLECT_FORMAT);
        } else {
            //非今年
            return TimeUtils.dateToString(end.getTime(), LONG_DATEFORMAT);
        }
    }

    public static String dateToString(Date date, String formatStr) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr, Locale.CHINA);
        return sdf.format(date);
    }

    public static String calendarToString(Calendar calendar, String formatStr) {
        if (calendar == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr, Locale.CHINA);
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取当前日期前多少天后多少天
     */
    public static ArrayList<String> getTimeList(String currentDate, String formatStr, int pre, int next) {
        ArrayList<String> list = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr, Locale.CHINA); //"yyyy-MM-dd"
            SimpleDateFormat formatter = new SimpleDateFormat(LONG_DATEFORMAT, Locale.CHINA);
            Date dt = formatter.parse(currentDate);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            for (int i = 0; i < pre; i++) {
                rightNow.add(Calendar.DAY_OF_MONTH, -1);// 你要减的日期
                Date dt1 = rightNow.getTime();
                String reStr = sdf.format(dt1);
                list.add(0, reStr);
            }

            list.add(sdf.format(dt));
            rightNow.setTime(dt);
            for (int i = 0; i < next; i++) {
                rightNow.add(Calendar.DAY_OF_MONTH, 1);// 你要加的日期
                Date dt1 = rightNow.getTime();
                String reStr = sdf.format(dt1);
                list.add(reStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据格式把字符转成时间
     *
     * @param strTime
     * @param format
     * @return
     */
    public static Date stringToDate(String strTime, String format) {
        if (strTime == null || "".equals(strTime.trim())) {
            return null;
        }
        if (format == null || "".equals(format.trim())) {
            format = LONG_DATEFORMAT;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param strTime
     * @param format
     * @return
     */
    public static Calendar stringToCalendar(String strTime, String format) {
        Date date = stringToDate(strTime, format);
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        return calendar;
    }

    public static boolean isAfterNow(Date now, String time, String format) {
        if (now == null) {
            return true;
        }
        Date date2 = stringToDate(time, format);
        if (date2 == null) {
            return true;
        }
        return date2.after(now);
    }

    public static String future5MatchesBetweenTime(String startTime, String nowDate) {
        DateFormat df = new SimpleDateFormat(LONG_DATEFORMAT, Locale.CHINA);
        try {
            Date d1 = df.parse(startTime);
            Date d2 = df.parse(nowDate);
            long diff = d2.getTime() - d1.getTime();
            long betweenHour = diff / 1000 / 3600;
            long day = betweenHour / 24;
            long leftDay = betweenHour % 24;
            if (leftDay >= 12) {
                return String.valueOf(day + 1);
            } else {
                return String.valueOf(day);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long parseTime(String time, String format) {
        if (time == null) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String date1SubDate2(Date d1, Date d2) {
        try {
            double diff = d2.getTime() - d1.getTime();// 这样得到的差值是微秒级别
            double days = diff / (86400000);
            double formatDate = Math.ceil(days);
            if (formatDate < 0) {
                return Integer.toString(0);
            } else {
                return Integer.toString((int) formatDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int date1SubDate2M(String now, String gmtEnd) {
        DateFormat df = new SimpleDateFormat(LONG_DATEFORMAT, Locale.CHINA);
        try {
            Date d1 = df.parse(now);
            Date d2 = df.parse(gmtEnd);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60))
                    / (1000 * 60);
            return Integer.valueOf(String.valueOf(minutes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将当前时间减去开始时间返回的毫秒数
     *
     * @param now
     * @param start
     * @return
     */
    public static long date1SubDate2Ms(String now, String start) {
        DateFormat df = new SimpleDateFormat(LONG_DATEFORMAT, Locale.CHINA);
        try {
            Date d1 = df.parse(now);
            Date d2 = df.parse(start);
            long diff = Math.abs(d1.getTime() - d2.getTime());// 这样得到的差值是微秒级别
            return diff;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long date1SubDate2MsWithNoAbs(String now, String start) {
        DateFormat df = new SimpleDateFormat(LONG_DATEFORMAT, Locale.CHINA);
        try {
            Date d1 = df.parse(now);
            Date d2 = df.parse(start);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            return diff;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String formatStringDate(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATEFORMAT, Locale.CHINA);
        try {
            Date tempDate = sdf.parse(date);
            sdf.applyPattern(pattern);
            return sdf.format(tempDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前日期前多少天后多少天
     */
    public static ArrayList<String> getTimeList(String currentDate, int pre, int next) {
        ArrayList<String> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT, Locale.CHINA);
        Date dt = sdf.parse(currentDate, new ParsePosition(0));
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        for (int i = 0; i < next; i++) {
            rightNow.add(Calendar.DAY_OF_MONTH, 1);// 你要加的日期
            Date dt1 = rightNow.getTime();
            String reStr = sdf.format(dt1);
            list.add(reStr);
        }
        list.add(currentDate);
        rightNow.setTime(dt);
        for (int i = 0; i < pre; i++) {
            rightNow.add(Calendar.DAY_OF_MONTH, -1);// 你要减的日期
            Date dt1 = rightNow.getTime();
            String reStr = sdf.format(dt1);
            list.add(reStr);
        }
        return list;
    }

    /**
     * 获取最近一段时间的整点时间(每30分钟)
     *
     * @return
     */
    public static ArrayList<Date> getRecentlyIntegralPointTime(int sendTimeLimit) {
        ArrayList<Date> list = new ArrayList<>();
//        SimpleDateFormat sdf = new SimpleDateFormat("HH时mm分");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        if (System.currentTimeMillis() - c.getTime().getTime() > 30 * 60 * 1000) {
            c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + 1);
        } else {
            c.set(Calendar.MINUTE, 30);
        }
//        String time = sdf.format(c.getTime());
        list.add(c.getTime());
        for (int i = 0; i < sendTimeLimit * 2 - 1; i++) {
            c.add(Calendar.MINUTE, 30);
//            String s = sdf.format(c.getTime());
            list.add(c.getTime());
        }
        return list;
    }

    /**
     * @param currentDate 当前的时间
     * @param changeValue 正数表示"后多少天"，负数表示"前多少天"
     * @return 返回变化后的时间
     */
    public static String getChangeTime(String currentDate, int changeValue) {
        if (changeValue == 0) {
            return currentDate;
        } else if (changeValue > 0) {
            return getTimeList(currentDate, 0, changeValue).get(changeValue - 1);
        } else {
            return getTimeList(currentDate, -changeValue, 0).get(-changeValue);
        }
    }

    /**
     * 当天：HH:MM
     * 昨天：昨天
     * 非跨年：MD-DD
     * 跨年：YYYY-MM-DD
     *
     * @param createTime 时间戳
     * @return
     */
    private static String getSpellingStr(long createTime) {
        long nowL = System.currentTimeMillis();
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTimeInMillis(nowL);

        Calendar createCal = Calendar.getInstance();
        createCal.setTimeInMillis(createTime);

        int nowYear = nowCal.get(Calendar.YEAR);
        int nowMonth = nowCal.get(Calendar.MONTH) + 1;
        int nowDay = nowCal.get(Calendar.DAY_OF_MONTH);

        int createYear = createCal.get(Calendar.YEAR);
        int createMonth = createCal.get(Calendar.MONTH) + 1;
        int createDay = createCal.get(Calendar.DAY_OF_MONTH);
        String hourAndMin = LibAppUtil.numAddFrontZero(createCal.get(Calendar.HOUR_OF_DAY)) + ":" + LibAppUtil.numAddFrontZero(createCal.get(Calendar.MINUTE));
        if (nowYear == createYear && nowMonth == createMonth && nowDay == createDay) {
            return hourAndMin;
        }
        if (nowYear == createYear && nowMonth == createMonth && nowDay == (createDay + 1)) {
            return "昨天";
        }
        if (nowYear == createYear) {
            return LibAppUtil.numAddFrontZero(createMonth) + "-" + LibAppUtil.numAddFrontZero(createDay);
        }
        return createYear + "-" + LibAppUtil.numAddFrontZero(createMonth) + "-" + LibAppUtil.numAddFrontZero(createDay);
    }

    /**
     * 当天：HH:MM(24小时制)
     * 昨天：昨天 HH:MM(24小时制)
     * 非跨年：M月D日 HH:MM(24小时制)
     * 跨年：YYYY年M月D日 HH:MM(24小时制) 2019年3月5日 20：06
     *
     * @param createTime 时间戳
     * @return
     */
    public static String getSpellingStrWithHourMinute(long createTime) {
        long nowL = System.currentTimeMillis();
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTimeInMillis(nowL);

        Calendar createCal = Calendar.getInstance();
        createCal.setTimeInMillis(createTime);

        int nowYear = nowCal.get(Calendar.YEAR);
        int nowMonth = nowCal.get(Calendar.MONTH) + 1;
        int nowDay = nowCal.get(Calendar.DAY_OF_MONTH);

        int createYear = createCal.get(Calendar.YEAR);
        int createMonth = createCal.get(Calendar.MONTH) + 1;
        int createDay = createCal.get(Calendar.DAY_OF_MONTH);
        int createHour = createCal.get(Calendar.HOUR_OF_DAY);
        int createMinute = createCal.get(Calendar.MINUTE);
        String hourAndMin = LibAppUtil.numAddFrontZero(createHour) + ":" + LibAppUtil.numAddFrontZero(createMinute);
        if (nowYear == createYear && nowMonth == createMonth && nowDay == createDay) {
            return hourAndMin;
        }
        if (nowYear == createYear && nowMonth == createMonth && nowDay == (createDay + 1)) {
            return "昨天 " + hourAndMin;
        }
        if (nowYear == createYear) {
            return createMonth + "月" + createDay + "日 " + hourAndMin;
        }
        return createYear + "年" + createMonth + "月" + createDay + "日 " + hourAndMin;
    }

    /**
     * 当天：HH:MM(24小时制) 09:06
     * 昨天：昨天
     * 非跨年：M月D日 3月6日
     * 跨年：YYYY年M月D日 2019年3月5日
     *
     * @param createTime 时间戳
     * @return
     */
    public static String getSpellingStrNoHourMinute(long createTime) {
        long nowL = System.currentTimeMillis();
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTimeInMillis(nowL);

        Calendar createCal = Calendar.getInstance();
        createCal.setTimeInMillis(createTime);

        int nowYear = nowCal.get(Calendar.YEAR);
        int nowMonth = nowCal.get(Calendar.MONTH) + 1;
        int nowDay = nowCal.get(Calendar.DAY_OF_MONTH);

        int createYear = createCal.get(Calendar.YEAR);
        int createMonth = createCal.get(Calendar.MONTH) + 1;
        int createDay = createCal.get(Calendar.DAY_OF_MONTH);
        int createHour = createCal.get(Calendar.HOUR_OF_DAY);
        int createMinute = createCal.get(Calendar.MINUTE);
        String hourAndMin = LibAppUtil.numAddFrontZero(createHour) + ":" + LibAppUtil.numAddFrontZero(createMinute);
        if (nowYear == createYear && nowMonth == createMonth && nowDay == createDay) {
            return hourAndMin;
        }
        if (nowYear == createYear && nowMonth == createMonth && nowDay == (createDay + 1)) {
            return "昨天";
        }
        if (nowYear == createYear) {
            return createMonth + "月" + createDay + "日";
        }
        return createYear + "年" + createMonth + "月" + createDay + "日";
    }

    //动态通用时间规则
    @Deprecated
    public static String formatTime(String nowStr, String createStr) {
        return getSpellingStr(nowStr, createStr);
    }

    //动态通用时间规则
    public static String getSpellingStr(String nowStr, String createStr) {
        if (TextUtils.isEmpty(nowStr) || TextUtils.isEmpty(createStr)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(LONG_DATEFORMAT);
        Date nowDate = sdf.parse(nowStr, new ParsePosition(0));
        Date createDate = sdf.parse(createStr, new ParsePosition(0));
        long nowL = nowDate.getTime();
        long createL = createDate.getTime();
        long temp = (nowL - createL) / 1000;
        if (temp <= 0) {
            return "刚刚";
        }
        if (temp <= 60) {
            return temp + "秒前";
        }
        if (temp < 60 * 60) {
            return temp / 60 + "分钟前";
        }
        if (temp < 60 * 60 * 24) {
            return temp / 60 / 60 + "小时前";
        }
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(nowDate);

        Calendar createCal = Calendar.getInstance();
        createCal.setTime(createDate);

        int nowYear = nowCal.get(Calendar.YEAR);
        int nowMonth = nowCal.get(Calendar.MONTH) + 1;
        int nowDay = nowCal.get(Calendar.DAY_OF_MONTH);

        int createYear = createCal.get(Calendar.YEAR);
        int createMonth = createCal.get(Calendar.MONTH) + 1;
        int createDay = createCal.get(Calendar.DAY_OF_MONTH);
        String hourAndMin = LibAppUtil.numAddFrontZero(createCal.get(Calendar.HOUR_OF_DAY)) + ":" + LibAppUtil.numAddFrontZero(createCal.get(Calendar.MINUTE));
        if (nowYear == createYear && nowMonth == createMonth && nowDay == createDay) {
            return "今天  " + hourAndMin;
        }
        if (nowYear == createYear && nowMonth == createMonth && nowDay == (createDay + 1)) {
            return "昨天  " + hourAndMin;
        }

        /**
         * x >=2D，且在本周内 	星期几+时分 	星期二 9：30
         */
        long diff = nowL - createL;
        // 当前日期的周日（本周第一天）
        Date sunDay = addDays(getWeekFirstDay(nowDate), -1);
        if (diff >= 2 * 24 * 60 * 60 && createDate.getTime() > sunDay.getTime()) {
            return "星期" + getWeekOfDate(createDate) + " " + getHourMinute(createDate);
        }

        if (nowYear == createYear) {
            return LibAppUtil.numAddFrontZero(createMonth) + "-" + LibAppUtil.numAddFrontZero(createDay) + "  " + hourAndMin;
        }
        return createYear + "-" + LibAppUtil.numAddFrontZero(createMonth) + "-" + LibAppUtil.numAddFrontZero(createDay) + "  " + hourAndMin;
    }

    public static String getHourMinute(Date date) {
        DateFormat df = getNewDateFormat(TIMEFORMAT);
        return df.format(date);
    }

    public static String getDate(Date date) {
        DateFormat df = getNewDateFormat(FORMAT_M_D);
        return df.format(date);
    }

    public static DateFormat getNewDateFormat(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        df.setLenient(false);
        return df;
    }

    public static Date addDays(Date date1, long days) {
        return addSeconds(date1, days * ONE_DAY_SECONDS);
    }

    public static Date addSeconds(Date date1, long secs) {
        return new Date(date1.getTime() + (secs * 1000));
    }

    public static Date getWeekFirstDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date firstDay = addDays(date, c.get(Calendar.DAY_OF_WEEK) == 1 ? -6 : -1 * c.get(Calendar.DAY_OF_WEEK) + 2);

        return firstDay;
    }

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String updateTimeFormat(String updateTime) {
        String result = "";
        String cutedTime = "";
        Date date;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        try {
            df.parse(updateTime);
            date = df.parse(updateTime);
            cutedTime = df.format(date);
            int index = cutedTime.indexOf("-");
            result = cutedTime.substring(index + 1);
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将毫秒格式化:00:00
     *
     * @param time
     * @return
     */
    public static String timeFormat(long time) {
        String sBuffer = "";
        int shi = (int) (time / 1000 / 60 / 60);
        int fen = (int) (time / 1000 / 60 % 60);
        int miao = (int) (time / 1000 % 60);
        if (shi < 10) {
            sBuffer += "0" + shi;
        } else {
            sBuffer += shi;
        }
        if (fen < 10) {
            sBuffer += ":0" + fen;
        } else {
            sBuffer += ":" + fen;
        }
        if (miao < 10) {
            sBuffer += ":0" + miao;
        } else {
            sBuffer += ":" + miao;
        }
        return sBuffer;
    }

    public static long caculateMinsDiff(String now, String gmtEnd) {
        DateFormat df = new SimpleDateFormat(LONG_DATEFORMAT, Locale.CHINA);
        try {
            Date d1 = df.parse(now);
            Date d2 = df.parse(gmtEnd);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            return diff / 1000 / 60;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String format(String formatDate, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATEFORMAT);
        String format = formatDate;
        try {
            Date parse = sdf.parse(formatDate);
            sdf.applyPattern(pattern);
            format = sdf.format(parse);
        } catch (Exception e) {
            //Log.e("format: ", e.toString());
        }
        return format;
    }

    /**
     * 可以格式化成任何格式
     *
     * @param defaultPattern 传入的字符时间格式
     * @param targetPattern  目标时间格式
     * @param formatTime     需要格式化的时间字符串
     * @return
     */
    public static String format(String defaultPattern, String targetPattern, String formatTime) {
        if (TextUtils.isEmpty(formatTime)) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(defaultPattern, Locale.CHINA);
        try {
            Date parse = format.parse(formatTime);
            format.applyPattern(targetPattern);
            return format.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatTime;
    }

    public static long caculateMnisDiff(String now, String gmtEnd) {
        DateFormat df = new SimpleDateFormat(LONG_DATEFORMAT, Locale.CHINA);
        try {
            Date d1 = df.parse(now);
            Date d2 = df.parse(gmtEnd);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            return diff / 1000 / 60;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATEFORMAT);
        return sdf.format(date);
    }

    /**
     * 服务器yyyy:mm:dd hh:mm:ss 转换为时间戳
     *
     * @param time
     * @return
     */
    public static long serviceTimeToTimeStamp(String time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 时间戳转换为服务器yyyy:mm:dd hh:mm:ss
     *
     * @param time
     * @return
     */
    public static String timeStampToServiceTime(long time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        date.setTime(time);

        String format = simpleDateFormat.format(date);

        return format;
    }

    /**
     * 获取每个月最大天数
     * @param date
     * @return
     */
    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

}
