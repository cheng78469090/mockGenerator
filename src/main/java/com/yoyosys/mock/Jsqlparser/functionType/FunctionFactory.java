package com.yoyosys.mock.Jsqlparser.functionType;

import com.yoyosys.mock.Jsqlparser.dataType.Data;
import com.yoyosys.mock.pojo.Column;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

import java.util.List;
import java.util.Locale;

/**
 * @Author: yjj
 * Date: 2021/10/29
 */
public class FunctionFactory extends Factory {
    public static Function create(String type, ExpressionList expressionList, Data data, List<Column> columns) {

        switch (type.toLowerCase(Locale.ROOT)) {
            case "concat":
                return new ConcatFunction(expressionList,data,columns);
            case "length":
                return new LengthFunction(expressionList,data,columns);
            case "substr":
                return new SubstrFunction(expressionList,data,columns);
            default:
                throw new IllegalArgumentException("暂时不支持这中方法");
        }
    }
}
