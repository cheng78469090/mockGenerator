package com.yoyosys.mock.Jsqlparser.functionType;


import com.yoyosys.mock.Jsqlparser.dataType.Data;
import com.yoyosys.mock.pojo.Column;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

import java.util.List;

public class SubstrFunction implements Function , Data {
    private ExpressionList parameters;

    private Data data;

    private List<Column> columns;

    public SubstrFunction(ExpressionList parameters, Data data, List<Column> columns) {
        this.parameters = parameters;
        this.data = data;
        this.columns = columns;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
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

    @Override
    public String inputValue() {
        return null;
    }

    @Override
    public String inputCounterexample() {
        return null;
    }
}
