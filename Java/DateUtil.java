package com.globalegrow.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @program: gitHdfsFile
 * @description: This is the util class for date
 * @author: Luyupeng
 * @create: 2019-04-26 16:54
 **/
public class DateUtil {
    /**
     * 获取输入时间前N天的日期时间
     *
     * @param dateTime 时间。
     * @param day N天前。
     * @return N天前的日期
     */
    public static String getDateBefore(String dateTime, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = sdf.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return sdf.format(now.getTime());
    }

    public static String getDateBeforeNow(String format, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return sdf.format(now.getTime());
    }

    public static String formatDate(String format, Long date){
        if(date == null){
            return "19700101";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = new Date(date);
        return sdf.format(d);
    }

    public static long parseDateStr(String format, String date){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date parse = null;
        try {
            parse = sdf.parse(date);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }
}