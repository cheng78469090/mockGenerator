package com.yoyosys.mock.Jsqlparser.functionType;


import com.yoyosys.mock.Jsqlparser.dataType.Data;
import com.yoyosys.mock.pojo.Column;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

import java.util.List;

public class SubstrFunction extends Function implements Data {
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
        return createData(true);
    }

    @Override
    public String inputCounterexample() {
        return createData(false);
    }

    public String createData(boolean flag) {
        //设置列名
        setColumnName(parameters.getExpressions().get(1).toString());

        Expression start = parameters.getExpressions().get(1);
        Expression end = parameters.getExpressions().get(2);
        if (start instanceof net.sf.jsqlparser.expression.Function) {
            throw new IllegalArgumentException("暂时不支持参数中的函数");
        }
        if (end instanceof net.sf.jsqlparser.expression.Function) {
            throw new IllegalArgumentException("暂时不支持参数中的函数");
        }

        //todo 根据column生成一个值
        String s = null;
        String dataValue = null;
        if (flag == true) {
            dataValue = data.inputValue();
        } else {
            dataValue = data.inputCounterexample();
        }
        String result = s.replaceFirst(s.substring(Integer.parseInt(start.toString()), Integer.parseInt(end.toString())), dataValue);

        return result;
    }

}
