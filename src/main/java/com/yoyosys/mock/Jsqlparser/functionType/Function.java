package com.yoyosys.mock.Jsqlparser.functionType;

import com.yoyosys.mock.Jsqlparser.dataType.Data;

/**
 * concat 例：concat('20210729',substr(rizhxh,9,17))
 * length 例：length(a) = 10
 * substr 例：SUBSTR(Currency_Cd,1,1) = '@'  ,  substr(Pty_Ast_Id,1,9) in ('ZY0504002')
 * @Author: yjj
 * Data: 2021/10/26
 */
public abstract class Function implements Data {

    private String columnName;

    public  String getColumnName() {
        if (columnName!=null) {
            return columnName;
        } else {
            throw new IllegalArgumentException("函数未能操作一列");
        }
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String inputValue() {
        return null;
    }

    @Override
    public String inputCounterexample() {
        return null;
    }
}
