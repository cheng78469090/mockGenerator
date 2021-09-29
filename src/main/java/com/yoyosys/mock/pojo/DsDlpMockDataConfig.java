package com.yoyosys.mock.pojo;

import java.io.Serializable;
import java.util.Date;

public class DsDlpMockDataConfig implements Serializable {

    private static final long serialVersionUID = 4107959305620703728L;

    private int id;
    private String hive_name;
    private String conditions;
    private int records;
    private String startDate;
    private String endDate;
    private String input_dir;
    private Date online_date;
    private String operator;
    private int state;
    private Date create_time;
    private String ds_name;
    private String name_en;
    private int isCounterexample;

    public int getIs_counter_example() {
        return isCounterexample;
    }

    public void setIs_counter_example(int is_counter_example) {
        this.isCounterexample = is_counter_example;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "DsDlpMockDataConfig{" +
                "id=" + id +
                ", hive_name='" + hive_name + '\'' +
                ", conditions='" + conditions + '\'' +
                ", records=" + records +
                ", start_date='" + startDate + '\'' +
                ", end_date='" + endDate + '\'' +
                ", input_dir='" + input_dir + '\'' +
                ", online_date=" + online_date +
                ", operator='" + operator + '\'' +
                ", state=" + state +
                ", create_time=" + create_time +
                ", ds_name='" + ds_name + '\'' +
                ", name_en='" + name_en + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHive_name() {
        return hive_name;
    }

    public void setHive_name(String hive_name) {
        this.hive_name = hive_name;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }


    public String getInput_dir() {
        return input_dir;
    }

    public void setInput_dir(String input_dir) {
        this.input_dir = input_dir;
    }

    public Date getOnline_date() {
        return online_date;
    }

    public void setOnline_date(Date online_date) {
        this.online_date = online_date;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getDs_name() {
        return ds_name;
    }

    public void setDs_name(String ds_name) {
        this.ds_name = ds_name;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }
}
