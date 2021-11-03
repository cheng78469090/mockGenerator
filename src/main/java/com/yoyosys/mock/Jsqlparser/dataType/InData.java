package com.yoyosys.mock.Jsqlparser.dataType;

import com.yoyosys.mock.Jsqlparser.util.IsDateFormat;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: yjj
 * Date: 2021/10/27
 */
public class InData implements Data{

    private List<String> inValue;

    private boolean inFlag;

    public List<String> getInValue() {
        return inValue;
    }

    public void setInValue(List<String> inValue) {
        this.inValue = inValue;
    }

    public boolean isInFlag() {
        return inFlag;
    }

    public boolean getInFlag() {
        return inFlag;
    }

    public void setInFlag(boolean inFlag) {
        this.inFlag = inFlag;
    }

    /**
     * data类，构建完成后调用方法返回一个值
     *
     * @return
     */
    @Override
    public String inputValue() {
        int random = (int)(Math.random()*(this.getInValue().size()));
        if (inFlag){
            return this.getInValue().get(random);
        }else{
            if (IsDateFormat.isRqFormat(this.getInValue().get(random))){
                return getContains();
            }else {
                //判断是否为数字或者字母，获取长度，返回相对应长度的数字和字符
                String character = this.getInValue().get(random);
                if(isNumeric(character)){
                    //是数字
                    return   getRandomNumber(character.length());
                }else {
                    //是字符
                    return   getRandomString(character.length());
                }
            }
        }
    }

    /**
     * 返回反例
     *
     * @return
     */
    @Override
    public String inputCounterexample() {
        //
        int random = (int)(Math.random()*(this.getInValue().size()));
        if (IsDateFormat.isRqFormat(this.getInValue().get(random))){
            return getContains();
        }else {
            //判断是否为数字或者字母，获取长度，返回相对应长度的数字和字符
            String character = this.getInValue().get(random);
            if(isNumeric(character)){
                //是数字
                return   getRandomNumber(character.length());
            }else {
                //是字符
                return   getRandomString(character.length());
            }
        }
    }

    public String getContains(){
        String date = getDate();
        while (this.getInValue().contains(date)){
            date =  getDate();
        }
        return date;
    }

    /**
     * 获取随机时间
     * @return
     */
    public String getDate(){
        Random rand = new Random();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.set(1900, 0, 1);
        long start = cal.getTimeInMillis();
        cal.set(2008, 0, 1);
        long end = cal.getTimeInMillis();
        Date d = new Date(start + (long) (rand.nextDouble() * (end - start)));
        String dateRes = format.format(d);
        return dateRes;

    }

    /**
     * 根据长度获取随机字符
     * @param length
     * @return
     */

    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(52);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
    /**
     * 根据长度获取随机数字
     */
    public static String getRandomNumber(int length){
        String str="0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(10);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
    /**
     * 匹配是否为数字
     * @param str 可能为中文，也可能是-19162431.1254，不使用BigDecimal的话，变成-1.91624311254E7
     * @return boolean
     */
    public static boolean isNumeric(String str) {
        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
