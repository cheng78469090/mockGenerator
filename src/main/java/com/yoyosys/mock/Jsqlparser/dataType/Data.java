package com.yoyosys.mock.Jsqlparser.dataType;

/**
 * @Author: yjj
 * Data: 2021/10/27
 */
public interface Data {
    /**
     * data类，构建完成后调用方法返回一个值
     * @return
     */
    String inputValue();

    /**
     * 返回反例
     * @return
     */
    String inputCounterexample();

}
