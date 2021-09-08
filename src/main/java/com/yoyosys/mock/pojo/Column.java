package com.yoyosys.mock.pojo;

/**
 * @Author: yjj
 * Date: 2021/9/3
 */

public class Column {
    private String fieldName;
    private String cType;
    private String cLength;
    private boolean primaryKey;

    @Override
    public String toString() {
        return "Column{" +
                "fieldName='" + fieldName + '\'' +
                ", cType='" + cType + '\'' +
                ", cLength='" + cLength + '\'' +
                ", primaryKey=" + primaryKey +
                '}';
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getcType() {
        return cType;
    }

    public void setcType(String cType) {
        this.cType = cType;
    }

    public String getcLength() {
        return cLength;
    }

    public void setcLength(String cLength) {
        this.cLength = cLength;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
}
