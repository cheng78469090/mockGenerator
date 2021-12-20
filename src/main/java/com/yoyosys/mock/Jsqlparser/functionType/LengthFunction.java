package com.yoyosys.mock.Jsqlparser.functionType;

import com.yoyosys.mock.Jsqlparser.dataType.Data;
import com.yoyosys.mock.pojo.Column;
import com.yoyosys.mock.util.MakeDataUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class LengthFunction extends Function implements Data {
    private ExpressionList parameters;

    private Data data;

    private List<Column> columns;

    public LengthFunction(ExpressionList parameters, Data data, List<Column> columns) {
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
        Expression expression = parameters.getExpressions().get(0);
        setColumnName(expression.toString());
        if (flag) {
           return RandomStringUtils.randomAlphanumeric(Integer.parseInt(data.inputValue()));
        }
        return RandomStringUtils.randomAlphanumeric(Integer.parseInt(data.inputCounterexample()));
    }

}
