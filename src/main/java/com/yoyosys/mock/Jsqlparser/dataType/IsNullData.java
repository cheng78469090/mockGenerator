package com.yoyosys.mock.Jsqlparser.dataType;

public class IsNullData implements Data{
    private String isNullValue;

    private boolean isNullFlag;

    public String getIsNullValue() {
        return isNullValue;
    }

    public void setIsNullValue(String isNullValue) {
        this.isNullValue = isNullValue;
    }

    public boolean getIsNullFlag() {
        return isNullFlag;
    }

    public void setNullFlag(boolean nullFlag) {
        isNullFlag = nullFlag;
    }

    @Override
    public String inputValue() {
        if (!isNullFlag){
            return null;
        }else{
            return "";
        }
    }

    /**
     * 返回反例
     *
     * @return
     */
    @Override
    public String inputCounterexample() {
        return null;
    }
}
