package com.yoyosys.mock.util;

import com.apifan.common.random.source.DateTimeSource;
import com.apifan.common.random.source.NumberSource;
import com.mifmif.common.regex.Generex;
import com.yoyosys.mock.pojo.Column;

import java.time.LocalDate;
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
    public static String makeStringData(Column column){
        Random random1 = new Random();
        StringBuffer sb1 = new StringBuffer();
        int length=random1.nextInt(Integer.parseInt(column.getcLength())/2);
        for (int j = 0; j < length; j++) {
            int number1 = random1.nextInt(Constants.STR.length());
            sb1.append(Constants.STR.charAt(number1));
        }
        return sb1.toString();
    }

    //随机生成整数
    public static int makeIntData(Column column){

        return NumberSource.getInstance().randomInt(0,
            (int) Math.pow(10, Integer.parseInt(column.getcLength())));
    }

    //随机生成小数类型
    public static String makeNumData(Column column){
        String[] length = column.getcLength().split(",");
        Generex generex = new Generex("[1-9]{" + length[0] + "}\\.[1-9]{" + length[1] + "}");
        return generex.random();
    }


}
