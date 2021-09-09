package com.yoyosys.mock.pojo;

/**
 * @Author: yjj
 * Date: 2021/9/3
 */

public class Column {
    //字段名
    private String fieldName;
    //字段类型
    private String cType;
    //字段长度
    private String cLength;
    //主键标识
    private boolean primaryKey;
    //是否分区字段
    private boolean isPartition;

    @Override
    public String toString() {
        return "Column{" +
                "fieldName='" + fieldName + '\'' +
                ", cType='" + cType + '\'' +
                ", cLength='" + cLength + '\'' +
                ", primaryKey=" + primaryKey +
                ", isPartition=" + isPartition +
                '}';
    }

    public boolean getIsPartition() {
        return isPartition;
    }

    public void setPartition(boolean partition) {
        isPartition = partition;
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

    public Column() {
    }

    public Column(String fieldName, String cType, String cLength, boolean primaryKey) {
        this.fieldName = fieldName;
        this.cType = cType;
        this.cLength = cLength;
        this.primaryKey = primaryKey;
    }
}

