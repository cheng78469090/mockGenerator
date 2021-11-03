package com.yoyosys.mock.Jsqlparser.functionType;


import com.yoyosys.mock.Jsqlparser.dataType.Data;
import com.yoyosys.mock.pojo.Column;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

import java.util.List;

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
        return null;
    }
}
