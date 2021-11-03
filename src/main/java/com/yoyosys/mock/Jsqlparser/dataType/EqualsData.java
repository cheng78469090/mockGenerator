package com.yoyosys.mock.Jsqlparser.dataType;

import com.apifan.common.random.source.DateTimeSource;
import com.yoyosys.mock.Jsqlparser.util.IsDateFormat;
import com.yoyosys.mock.Jsqlparser.util.JudgeString;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

/**
 * @Author: yjj
 * Date: 2021/10/27
 */
public class EqualsData implements Data{

    private String equalsValue;

    /**
     * = 为 true  ！=为 false
     */
    private Boolean equalsFlag;

    public String getEqualsValue() {
        return equalsValue;
    }

    public void setEqualsValue(String equalsValue) {
        this.equalsValue = equalsValue;
    }

    public Boolean getEqualsFlag() {
        return equalsFlag;
    }

    public void setEqualsFlag(Boolean equalsFlag) {
        this.equalsFlag = equalsFlag;
    }

    /**
     * data类，构建完成后调用方法返回一个值
     *
     * @return
     */
    @Override
    public String inputValue() {
        if (equalsFlag) {
            return equalsValue;
        } else {
            if ("".equals(equalsValue)){
                return null;
            }
            if (IsDateFormat.isRqFormat(equalsValue)){
                String substring = equalsValue.substring(0, 4);
                while(true){
                    String yyyyMMdd = DateTimeSource.getInstance().randomDate(Integer.parseInt(substring), "yyyyMMdd");
                    if (!equalsValue.equals(yyyyMMdd)){
                        return yyyyMMdd;
                    }
                }
            }
            if (JudgeString.isInteger(equalsValue)){
                long l = Long.parseLong(equalsValue);
                return String.valueOf((Long) RandomUtils.nextLong(l-l,l+l));
            }
            if (JudgeString.isDouble(equalsValue)){
                Double aDouble = Double.valueOf(equalsValue);
                return String.valueOf(RandomUtils.nextDouble(aDouble-aDouble,aDouble+aDouble));
            }
            return RandomStringUtils.randomAlphanumeric(equalsValue.length());
        }
    }

    /**
     * 返回反例
     *
     * @return
     */
    @Override
    public String inputCounterexample() {
        equalsFlag = !equalsFlag;
        return inputValue();
    }
}
