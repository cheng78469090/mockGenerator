package com.yoyosys.mock.Jsqlparser;

import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;

public class ColumnFinder extends ExpressionVisitorAdapter {

    String ColumnName = null;

    @Override
    public void visit(net.sf.jsqlparser.schema.Column column) {
        ColumnName = column.getColumnName();
    }

    public String getColumnName() {
        return ColumnName;
    }
}
