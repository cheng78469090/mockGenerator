package com.yoyosys.mock.Jsqlparser.functionType;


import com.yoyosys.mock.Jsqlparser.dataType.Data;
import com.yoyosys.mock.pojo.Column;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcatFunction extends Function implements Data {
    private ExpressionList parameters;

    private Data data;

    private List<Column> columns;

    public ConcatFunction(ExpressionList parameters, Data data, List<Column> columns) {
        this.parameters = parameters;
        this.data = data;
        this.columns = columns;
    }

    public ExpressionList getParameters() {
        return parameters;
    }

    public void setParameters(ExpressionList parameters) {
        this.parameters = parameters;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    @Override
    public String inputValue() {
        return createData(true);
    }

    @Override
    public String inputCounterexample() {
        return createData(false);
    }

    public String createData(boolean flag){
        // todo 根据长度创建
        String s = data.inputValue();
        int length = s.length();
        int x = 0;
        int start = 0;
        int end = 0;
        AtomicInteger index = new AtomicInteger();
        ArrayList<Integer> integers = new ArrayList<>();
        parameters.getExpressions().stream().forEach(expression -> {
            if (expression instanceof net.sf.jsqlparser.schema.Column || expression instanceof Function){
                if (expression instanceof net.sf.jsqlparser.schema.Column) {
                    setColumnName(((net.sf.jsqlparser.schema.Column) expression).getColumnName());
                }
                index.set(integers.size() + 1);
                integers.add(x);
            }else {
                integers.add(expression.toString().length());
            }
        });
        for (int i = 0; i < index.get(); i++) {
            start = end+1;
            end = start + integers.get(0);
        }
        if (flag) {
            return data.inputValue().substring(start,end);
        } else {
            return data.inputCounterexample().substring(start,end);
        }
    }
}
