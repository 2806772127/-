package com.fb.goldencudgel.auditDecisionSystem.utils;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class DateTimeUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Calendar startDate = Calendar.getInstance();
    private static Calendar endDate = Calendar.getInstance();
    private static DateFormat df = DateFormat.getDateInstance();
    private static Date earlydate = new Date();
    private static Date latedate = new Date();

    /**计算两个日期相差多少年*/
    public static int yearsBetween(String start, String end) throws ParseException {
        startDate.setTime(sdf.parse(start));
        endDate.setTime(sdf.parse(end));
        return (endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR));
    }
    /**计算日期至今多少年*/
    public static int yearsBetweenNow(String start) throws ParseException {
        startDate.setTime(sdf.parse(start));
        String end = sdf.format(earlydate);
        endDate.setTime(sdf.parse(end));
        return (endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR));
    }

    /**获取当前日期**/
    public static String getNowDate(){
       String nowDate = startDate.get(Calendar.YEAR)+"/"+startDate.get(Calendar.MONTH)+"/"+startDate.get(Calendar.DATE);
        return nowDate;
    }
    /**获取当前日期 格式:例20190219**/
    public static String getStringDate(){
        String nowDate = startDate.get(Calendar.YEAR)+""+startDate.get(Calendar.MONTH)+""+startDate.get(Calendar.DATE);
        return nowDate;
    }

    public static Date stringToDate(String date){
        if(date == null || "".equals(date)){
            return null;
        }else{
            try{
                Date reDate = sdf2.parse(date);
                return reDate;
            }catch (Exception e){
                return null;
            }
        }
    }

    public static String dateToString(Date date){
        if(date == null){
            return "";
        }else{
            return sdf2.format(date);
        }
    }
    
    public static String formatDateToStr(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
}
