package com.yoyosys.mock.Jsqlparser.dataType;

import com.mifmif.common.regex.Generex;

/**
 * @Author: yjj
 * Date: 2021/10/27
 */
public class LikeData implements Data{

    private String likeValue;

    private boolean likeFlag;

    public String getLikeValue() {
        return likeValue;
    }

    public void setLikeValue(String likeValue) {
        this.likeValue = likeValue;
    }

    public boolean getLikeFlag() {
        return likeFlag;
    }

    public void setLikeFlag(boolean likeFlag) {
        this.likeFlag = likeFlag;
    }

    /**
     * data类，构建完成后调用方法返回一个值
     *
     * @return
     */
    @Override
    public String inputValue() {
        String[] split = likeValue.toString().split("");
        Generex generex1 = new Generex("[0-9A-Za-z]");
        Generex generex2 = new Generex("[0-9A-Za-z]{1,5}");
        StringBuilder stringBuilder = new StringBuilder();
        if (!likeFlag){//isNotLike
            for (String s : split) {
                if ("_".equals(s)){
                    stringBuilder.append(generex1.random());
                } else if ("%".equals(s)){
                    stringBuilder.append(generex2.random());
                } else {
                    while (true) {
                        String random = generex1.random();
                        if (random != s) {
                            stringBuilder.append(random);
                            break;
                        }
                    }
                }
            }
            return stringBuilder.toString();
        }else{//isLike
            for (String s : split) {
                if ("_".equals(s)){
                    stringBuilder.append(generex1.random());
                } else if ("%".equals(s)){
                    stringBuilder.append(generex2.random());
                } else {
                    stringBuilder.append(s);
                }
            }
            return stringBuilder.toString();
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
