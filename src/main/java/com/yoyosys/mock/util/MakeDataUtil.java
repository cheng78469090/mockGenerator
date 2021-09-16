package com.yoyosys.mock.util;

import com.apifan.common.random.source.DateTimeSource;
import com.apifan.common.random.source.NumberSource;
import com.mifmif.common.regex.Generex;
import com.yoyosys.mock.pojo.Column;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

public class MakeDataUtil {

    //随机生成char类型
    public static char makeCharData(){
        Random random = new Random();
        int number = random.nextInt(Constants.STR.length());
        return (char) Constants.STR.charAt(number);
    }

    //随机生成日期类型
    public static String makeDateData(String startDate, String endDate){

        LocalDate startDate1 = LocalDate.of(Integer.parseInt(startDate.substring(0,4)),
                                            Integer.parseInt(startDate.substring(4,6)),
                                            Integer.parseInt(startDate.substring(6,8)));
        LocalDate endDate1 = LocalDate.of(Integer.parseInt(endDate.substring(0,4)),
                                          Integer.parseInt(endDate.substring(4,6)),
                                          Integer.parseInt(endDate.substring(6,8)));
        return DateTimeSource.getInstance().randomDate(startDate1, endDate1, "yyyyMMdd");
    }

    //随机生成字符串
    public static String makeStringData(Column column, LinkedHashSet list){
        Random random1 = new Random();
        StringBuffer sb1 = new StringBuffer();
        {
            int length=random1.nextInt(Integer.parseInt(column.getcLength())/2);
            for (int j = 0; j < length; j++) {
                int number1 = random1.nextInt(Constants.STR.length());
                sb1.append(Constants.STR.charAt(number1));
            }
        }while (!list.contains(sb1.toString()));
        return sb1.toString();
    }

    //随机生成整数
    public static String makeIntData(Column column, LinkedHashSet list){
        String result = null;
        {
            result = NumberSource.getInstance().randomInt(0,
                    (int) Math.pow(10, Integer.parseInt(column.getcLength()))) + "";
        }while(!list.contains(result));
        return result;
    }

    //随机生成小数类型
    public static String makeNumData(Column column, LinkedHashSet list){
        String[] length = column.getcLength().split(",");
        String a = null;
        String b = null;
        String result = null;
        {
            a = NumberSource.getInstance().randomInt(0,
                    (int) Math.pow(10, Integer.parseInt(length[0]))) + "";
            b = NumberSource.getInstance().randomInt(0,
                    (int) Math.pow(10, Integer.parseInt(length[1]))) + "";
            result = a + "." + b;
        }while(list.contains(result));
        return result;
    }


}
