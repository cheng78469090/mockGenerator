package com.yoyosys.mock.Jsqlparser;

import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;

import javax.print.DocFlavor;

public class ColumnFinder extends ExpressionVisitorAdapter {

    String ColumnName = null;

    @Override
    public void visit(net.sf.jsqlparser.schema.Column column) {
        if(containsQuotes(column.getColumnName())){
            ColumnName = column.getColumnName();
        }
    }

    public String getColumnName() {
        return ColumnName;
    }

    public boolean containsQuotes(String s){
        if (
                '\"'==s.charAt(0) && '\"'==s.charAt(s.length()-1) || '\''==s.charAt(0) && '\''==s.charAt(s.length()-1)
        ){
            return false;
        }else {
            return true;
        }

    }
}
