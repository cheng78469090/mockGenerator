package com.yoyosys.mock.pojo;

import java.util.PrimitiveIterator;

/**
 * @Author: yjj
 * Date: 2021/9/3
 */
public class DataSourceConfig {

    private String Oracle_url;
    private String Oracle_driver;
    private String Oracle_user;
    private String Oracle_password;
    private String Operator;
    private String Timestamp;
    private String dataFile;//数据文件
    private String ModeFilePath;//模板文件路径
    private String DataFilePath;//数据文件路径
    private String ResultFilePath;//输出文件路径


    public String getModeFilePath() {
        return ModeFilePath;
    }

    public void setModeFilePath(String modeFilePath) {
        ModeFilePath = modeFilePath;
    }

    public String getDataFilePath() {
        return DataFilePath;
    }

    public void setDataFilePath(String dataFilePath) {
        DataFilePath = dataFilePath;
    }

    public String getResultFilePath() {
        return ResultFilePath;
    }

    public void setResultFilePath(String resultFilePath) {
        ResultFilePath = resultFilePath;
    }

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    public String getOracle_url() {
        return Oracle_url;
    }

    public void setOracle_url(String oracle_url) {
        Oracle_url = oracle_url;
    }

    public String getOracle_driver() {
        return Oracle_driver;
    }

    public void setOracle_driver(String oracle_driver) {
        Oracle_driver = oracle_driver;
    }

    public String getOracle_user() {
        return Oracle_user;
    }

    public void setOracle_user(String oracle_user) {
        Oracle_user = oracle_user;
    }

    public String getOracle_password() {
        return Oracle_password;
    }

    public void setOracle_password(String oracle_password) {
        Oracle_password = oracle_password;
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }
}
