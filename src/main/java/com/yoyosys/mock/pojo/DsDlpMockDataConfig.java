package com.yoyosys.mock.pojo;

import java.io.Serializable;
import java.util.Date;

public class DsDlpMockDataConfig implements Serializable {


    private static final long serialVersionUID = 4107959305620703728L;

    private int id;
    private String hive_name;
    private String conditions;
    private int records;
    private Date start_date;
    private Date end_date;
    private String input_dir;
    private Date online_date;
    private String operator;
    private int state;
    private Date create_time;
    private String ds_name;
    private String name_en;

    @Override
    public String toString() {
        return "DsDlpMockdataConfig{" +
                "id=" + id +
                ", hive_name='" + hive_name + '\'' +
                ", conditions='" + conditions + '\'' +
                ", records=" + records +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", input_dir='" + input_dir + '\'' +
                ", online_date='" + online_date + '\'' +
                ", operator='" + operator + '\'' +
                ", state=" + state +
                ", create_time='" + create_time + '\'' +
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

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
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
