package com.yoyosys.mock.util;

import com.apifan.common.random.source.DateTimeSource;
import com.apifan.common.random.source.NumberSource;
import com.mifmif.common.regex.Generex;
import com.yoyosys.mock.pojo.Column;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDate;
import java.util.*;

public class MakeDataUtil {

    public static String  modiDataForFunction(Column column, String loadSence){

        String result = null;
        String ctype = column.getcType().toUpperCase();
        switch (ctype) {
            case "VARCHAR"://可变长度的字符串
            case "NCHAR"://根据字符集而定的固定长度字符串
            case "NVARCHAR2 "://根据字符集而定的可变长度字符串
            case "LONG"://超长字符串
            case "STRING"://超长字符串
                result = makeStringLenData(column);
                break;
            case "INTEGER":
            case "INT":
                result = makeIntLenData(column);
                break;
        }
        return result;
    }


    //随机生成char类型
    public static char makeCharData(Column column, Collection list){
        Random random = new Random();
        int number = 0;
        do{
            number = random.nextInt(Constants.STR1.length());
        }while(list.contains(number + ""));
        return (char) Constants.STR1.charAt(number);
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
    public static String makeStringData(Column column, Collection list){
        if (StringUtils.isBlank(column.getcLength()) || Integer.parseInt(column.getcLength()) > 255){
            column.setcLength("32");
        }
        Random random1 = new Random();
        StringBuffer sb1 = new StringBuffer();
        do {
            int length=random1.nextInt(Integer.parseInt(column.getcLength())/2);
            if (length == 0 ){
                length = 1;
            }
            for (int j = 0; j < length; j++) {
                int number1 = random1.nextInt(Constants.STR.length());
                sb1.append(Constants.STR.charAt(number1));
            }
        }while (list.contains(sb1.toString()));
        return sb1.toString();
    }



    //随机生成整数
    public static String makeIntData(Column column, Collection list){
        if (StringUtils.isBlank(column.getcLength())){
            column.setcLength("8");
        }
        String result = null;
        do{
            result = NumberSource.getInstance().randomInt(0,
                    (int) Math.pow(10, Integer.parseInt(column.getcLength()))) + "";
        }while(list.contains(result));
        return result;
    }

    //随机生成小数类型
    public static String makeNumData(Column column, Collection list){
        if (StringUtils.isBlank(column.getcLength())){
            column.setcLength("8,8");
        }
        String[] length = column.getcLength().split(",");
        String a = null;
        String b = null;
        String result = null;
        do{
            a = NumberSource.getInstance().randomInt(0,
                    (int) Math.pow(10, Integer.parseInt(length[0]))) + "";
            if (Integer.parseInt(length[1]) != 0){
                b = NumberSource.getInstance().randomInt(0,
                        (int) Math.pow(10, Integer.parseInt(length[1]))) + "";
            }

            if (b == null){
                result = a;
            }else{
                result = a + "." + b;
            }
        }while(list.contains(result));
        return result;
    }


    //随机生成定长字符串
    public static String makeStringLenData(Column column){
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0; i < Integer.parseInt(column.getcLength()); i++){
            int number=random.nextInt(62);
            sb.append(Constants.STR.charAt(number));
        }
        return sb.toString();
    }

    //随机生成定长整数
    public static String makeIntLenData(Column column){
        int length = Integer.parseInt(column.getcLength());
        String result  = NumberSource.getInstance().randomInt(
                     (int) Math.pow(10, Integer.parseInt(column.getcLength()) - 1),
                    (int) Math.pow(10, Integer.parseInt(column.getcLength())) - 1) + "";

        return result;
    }

}
