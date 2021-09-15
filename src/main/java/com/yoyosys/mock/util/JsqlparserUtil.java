package com.yoyosys.mock.util;

/**
 * @Author: yjj
 * Date: 2021/9/5
 */

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.collections4.CollectionUtils;

import javax.sound.sampled.Line;
import java.io.StringReader;
import java.util.*;

/**
 * 基于Jsqlparser的sql解析功能，并获取表名和where后面的条件
 */
public class JsqlparserUtil {

    //装载where后面的字段名称并去重
    private List<Expression> list = new ArrayList<>();
    //解析出来的单个条件名称
    private String columnName = null;

    /**
     * 获取SQL中的全部表名
     *
     * @param sql
     * @return
     */
    public static String getTableName(String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            List<String> tableNameList = tablesNamesFinder.getTableList(statement);
            if (!CollectionUtils.isEmpty(tableNameList)) {
                StringBuffer allTableNames = new StringBuffer();
                tableNameList.forEach(tableName -> {
                    allTableNames.append(tableName + ",");
                });
                String allTableName = allTableNames.toString().substring(0, allTableNames.toString().length() - 1);
                return allTableName;
            }
        } catch (JSQLParserException e) {

        }
        return null;
    }

    /**
     * 获取SQL中的where后面的条件名称
     *
     * @param sql
     * @return
     * @throws JSQLParserException
     */
    public List<Expression> getCloumnNames(String sql) throws JSQLParserException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(sql);
        List<Expression> expressionList = new ArrayList<>();
        Expression where = CCJSqlParserUtil.parseCondExpression(sql,false);
        if (null != where) {
            try {
                List<Expression> list = getParser(where);
                expressionList.addAll(list);
            }catch (Exception e){

            }finally {
                list.clear();
            }
        }
        return expressionList;
    }

    public List<Expression> getParser(Expression expression) {
        //初始化接受获得的字段信息
        if (expression instanceof BinaryExpression) {
            //获得左边表达式
            Expression leftExpression = ((BinaryExpression) expression).getLeftExpression();
            //获得左边表达式为Column对象，则直接获得列名
            if (leftExpression instanceof Column) {
                columnName = ((Column) leftExpression).getColumnName();
                list.add(expression);
            } else if (leftExpression instanceof Between) {
                list.add(leftExpression);
            } else if (leftExpression instanceof InExpression) {
                this.parserInExpression(leftExpression);
            } else if (leftExpression instanceof IsNullExpression) {
                this.parserIsNullExpression(leftExpression);
            } else if (leftExpression instanceof BinaryExpression) {//递归调用
                getParser(leftExpression);
            } else if (leftExpression instanceof Parenthesis) {//递归调用
                Expression expression1 = ((Parenthesis) leftExpression).getExpression();
                if (expression1 instanceof OrExpression || expression1 instanceof AndExpression){
                    list.add(expression1);
                }else {
                    getParser(expression1);
                }
            }

            //获得右边表达式，并分解
            Expression rightExpression = ((BinaryExpression) expression).getRightExpression();
            if (rightExpression instanceof BinaryExpression) {
                getParser(rightExpression);
            } else if (rightExpression instanceof InExpression) {
                this.parserInExpression(rightExpression);
            } else if (rightExpression instanceof IsNullExpression) {
                this.parserIsNullExpression(rightExpression);
            } else if (rightExpression instanceof Parenthesis) {//递归调用
                Expression expression1 = ((Parenthesis) rightExpression).getExpression();
                if (expression1 instanceof OrExpression || expression1 instanceof AndExpression){
                    list.add(expression1);
                }else {
                    getParser(expression1);
                }
            }
        } else if (expression instanceof Between) {
            list.add(expression);
        } else if (expression instanceof InExpression) {
            this.parserInExpression(expression);
        } else if (expression instanceof IsNullExpression) {
            this.parserIsNullExpression(expression);
        } else if (expression instanceof Parenthesis) {//递归调用
            Expression expression1 = ((Parenthesis) expression).getExpression();
            if (expression1 instanceof OrExpression || expression1 instanceof AndExpression){
                list.add(expression1);
            }else {
                getParser(expression1);
            }
        }
        return list;
    }

    /**
     * 解析in关键字左边的条件
     *
     * @param expression
     */
    public void parserInExpression(Expression expression) {
        Expression leftExpression = ((InExpression) expression).getLeftExpression();
        if (leftExpression instanceof Column) {
            list.add(expression);
        }
    }

    /**
     * 解析is null 和 is not null关键字左边的条件
     *
     * @param expression
     */
    public void parserIsNullExpression(Expression expression) {
        Expression leftExpression = ((IsNullExpression) expression).getLeftExpression();
        if (leftExpression instanceof Column) {
            list.add(expression);
        }
    }

    public void parserBinaryExpression(Expression expression) {
        Expression leftExpression = ((BinaryExpression) expression).getLeftExpression();
        if (leftExpression instanceof Column) {
            list.add(expression);
        }
    }

    /**
     * 测试类
     * @throws JSQLParserException
     */
    public static void main(String[] args) throws JSQLParserException {
        String sql = "select * from tt where ss between 1 and 2";
        String tableName = getTableName(sql);
        System.out.println("tableName:" + tableName);
        if (null != tableName) {
            JsqlparserUtil jsqlparserUtil = new JsqlparserUtil();
            List<Expression> cloumnNames = jsqlparserUtil.getCloumnNames(sql);
            System.out.println(cloumnNames);
        }
    }

    /**
     * @param sql
     * @return
     * @throws JSQLParserException
     */
    public static List<Expression> getSQLParser(String sql) throws JSQLParserException {
        JsqlparserUtil jsqlparserUtil = new JsqlparserUtil();
        return jsqlparserUtil.getCloumnNames(sql);
    }
}
