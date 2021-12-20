package com.yoyosys.mock.Jsqlparser.util;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: yjj
 * Date: 2021/9/7
 */
public class IsDateFormat {
    /***
     * 判断字符串是否是yyyyMMddHHmmss格式
     * @param mes 字符串
     * @return boolean 是否是日期格式
     */
    public static boolean isRqSjFormat(String mes){
        String format = "([0-9]{4})(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])"
                + "([01][0-9]|2[0-3])[0-5][0-9][0-5][0-9]";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(mes);
        if (matcher.matches()) {
            pattern = Pattern.compile("(\\d{4})(\\d{2})(\\d{2}).*");
            matcher = pattern.matcher(mes);
            if (matcher.matches()) {
                int y = Integer.valueOf(matcher.group(1));
                int m = Integer.valueOf(matcher.group(2));
                int d = Integer.valueOf(matcher.group(3));
                if (d > 28) {
                    Calendar c = Calendar.getInstance();
                    c.set(y, m-1, 1);
                    //每个月的最大天数
                    int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    return (lastDay >= d);
                }
            }
            return true;
        }
        return false;

    }


     /***
     * 判断字符串是否是yyyyMMdd格式
     * @param mes 字符串
     * @return boolean 是否是日期格式
     */
    public static boolean isRqFormat(String mes){
        String format = "([0-9]{4})(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(mes);
        if (matcher.matches()) {
            pattern = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})");
            matcher = pattern.matcher(mes);
            if (matcher.matches()) {
                int y = Integer.valueOf(matcher.group(1));
                int m = Integer.valueOf(matcher.group(2));
                int d = Integer.valueOf(matcher.group(3));
                if (d > 28) {
                    Calendar c = Calendar.getInstance();
                    c.set(y, m-1, 1);
                    //每个月的最大天数
                    int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    return (lastDay >= d);
                }
            }
            return true;
        }
        return false;

    }
}
