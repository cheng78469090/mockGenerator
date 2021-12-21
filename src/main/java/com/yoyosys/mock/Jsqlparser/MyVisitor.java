package com.yoyosys.mock.Jsqlparser;

import com.yoyosys.mock.Jsqlparser.dataType.*;
import com.yoyosys.mock.Jsqlparser.functionType.FunctionFactory;
import com.yoyosys.mock.pojo.Column;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;


import java.util.*;

/**
 * @Author: yjj
 * Data: 2021/10/25
 */
public class MyVisitor extends ExpressionVisitorAdapter {

    private static Random random = new Random();

    private Map<String, Data> dataModifyMap = new HashMap<String, Data>();;

    private List<Column> Columns;

    public MyVisitor(List<Column> columns) {
        Columns = columns;
    }

    public Map<String, Data> getDataModifyMap() {
        return dataModifyMap;
    }

    public void cleanDataModifyMap(){
        dataModifyMap.clear();
    }


    @Override
    public void visit(net.sf.jsqlparser.expression.Function function) {

    }

    @Override
    public void visit(AndExpression andExpression) {
        andExpression.getLeftExpression().accept(this);

        andExpression.getRightExpression().accept(this);
    }

    @Override
    public void visit(OrExpression orExpression) {
        if (random.nextInt(2)==0) {
            orExpression.getLeftExpression().accept(this);
        }else {
            orExpression.getRightExpression().accept(this);
        }
    }

    @Override
    public void visit(EqualsTo equalsTo) {
        if (equalsTo.getLeftExpression() instanceof Function || equalsTo.getRightExpression() instanceof Function) {
            //todo 左右两边有function的情况
            Function leftExpression = (Function) equalsTo.getLeftExpression();
            EqualsData equalsData = new EqualsData();
            equalsData.setEqualsFlag(true);
            equalsData.setEqualsValue(equalsTo.getRightExpression().toString().replace("\"", "").replace("\'", ""));
            com.yoyosys.mock.Jsqlparser.functionType.Function function = FunctionFactory.create(leftExpression.getName(), leftExpression.getParameters(), equalsData, Columns);
            function.setColumnName(getColumnName(leftExpression));
            dataModifyMap.put(function.getColumnName(),function);
        }else {
            EqualsData equalsData = new EqualsData();
            equalsData.setEqualsFlag(true);
            equalsData.setEqualsValue(equalsTo.getRightExpression().toString().replace("\"", "").replace("\'", ""));
            dataModifyMap.put(equalsTo.getLeftExpression().toString(),equalsData);
        }
    }

    @Override
    public void visit(Between between) {
        //todo
        if (between.getBetweenExpressionEnd() instanceof Function && between.getBetweenExpressionStart() instanceof Function) {
            //todo 左右两边有function的情况
        } else {
            CompareData compareData = new CompareData();
            compareData.setGreaterThanEquals(between.getBetweenExpressionStart().toString().replace("\"", "").replace("\'", ""));
            compareData.setMirrorThanEquals(between.getBetweenExpressionEnd().toString().replace("\"", "").replace("\'", ""));
            dataModifyMap.put(between.getLeftExpression().toString(),compareData);
        }
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        if (greaterThan.getLeftExpression() instanceof Function && greaterThan.getRightExpression() instanceof Function) {

        }else {
            CompareData existData = (CompareData)dataModifyMap.get(greaterThan.getLeftExpression().toString());
            if(existData == null){
                CompareData compareData = new CompareData();
                compareData.setGreaterThan(greaterThan.getRightExpression().toString().replace("'","").replace("\"", ""));
                dataModifyMap.put(greaterThan.getLeftExpression().toString(),compareData);
            }else {
                existData.setGreaterThan(greaterThan.getRightExpression().toString().replace("'","").replace("\"", ""));
                dataModifyMap.put(greaterThan.getLeftExpression().toString(),existData);
            }
        }
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        CompareData existData = null;
        if (greaterThanEquals.getLeftExpression() instanceof Function && greaterThanEquals.getRightExpression() instanceof Function) {

        }else {
            try{
                existData = (CompareData)dataModifyMap.get(greaterThanEquals.getLeftExpression().toString());
            }catch (Exception e){
                System.out.println(dataModifyMap.toString());
                System.out.println(greaterThanEquals.toString());
                e.printStackTrace();
            }
            if(existData == null){
                CompareData compareData = new CompareData();
                compareData.setGreaterThanEquals(greaterThanEquals.getRightExpression().toString().replace("'","").replace("\"", ""));
                dataModifyMap.put(greaterThanEquals.getLeftExpression().toString(),compareData);
            }else {
                existData.setGreaterThanEquals(greaterThanEquals.getRightExpression().toString().replace("'","").replace("\"", ""));
                dataModifyMap.put(greaterThanEquals.getLeftExpression().toString(),existData);
            }
        }
    }

    @Override
    public void visit(InExpression inExpression) {
        //todo
        ExpressionList rightItemsList = (ExpressionList) inExpression.getRightItemsList();
        List<Expression> expressions = rightItemsList.getExpressions();
        List<String> stringList = new ArrayList<>();
        for (Expression expression:expressions ) {
            stringList.add(expression.toString().replace("'","").replace("\"", ""));
        }

        if (inExpression.isNot()){
            if(inExpression.getLeftExpression() instanceof Function){
                Function leftExpression = (Function) inExpression.getLeftExpression();
                InData inData =new InData();
                inData.setInFlag(false);
                inData.setInValue(stringList);
                com.yoyosys.mock.Jsqlparser.functionType.Function function = FunctionFactory.create(leftExpression.getName(), leftExpression.getParameters(), inData, Columns);
                function.setColumnName(getColumnName(leftExpression));
                dataModifyMap.put(function.getColumnName(),function);
            }else{
                InData inData =new InData();
                inData.setInFlag(false);
                inData.setInValue(stringList);
                dataModifyMap.put(inExpression.getLeftExpression().toString(),inData);
            }
        }else{
            if(inExpression.getLeftExpression() instanceof  Function){
                Function leftExpression = (Function) inExpression.getLeftExpression();
                InData inData =new InData();
                inData.setInFlag(true);
                inData.setInValue(stringList);
                com.yoyosys.mock.Jsqlparser.functionType.Function function = FunctionFactory.create(leftExpression.getName(), leftExpression.getParameters(), inData, Columns);
                function.setColumnName(getColumnName(leftExpression));
                dataModifyMap.put(function.getColumnName(),function);
            }else{
                InData inData =new InData();
                inData.setInFlag(true);
                inData.setInValue(stringList);
                dataModifyMap.put(inExpression.getLeftExpression().toString(),inData);
            }
        }
    }

    @Override
    public void visit(IsNullExpression isNullExpression) {
        //todo 王震宣
        IsNullData isNullData = new IsNullData();
        if (isNullExpression.getLeftExpression() instanceof Function) {

        }else {
            isNullData.setNullFlag(!isNullExpression.isNot());
            dataModifyMap.put(isNullExpression.getLeftExpression().toString(),isNullData);
        }
    }

    @Override
    public void visit(LikeExpression likeExpression) {
        //todo 王震宣
        if (likeExpression.getLeftExpression() instanceof net.sf.jsqlparser.expression.Function && likeExpression.getRightExpression() instanceof net.sf.jsqlparser.expression.Function){

        } else {
            LikeData likeData = new LikeData();
            likeData.setLikeFlag(!likeExpression.isNot());
            likeData.setLikeValue(likeExpression.getRightExpression().toString().replace("\"", "").replace("\'", ""));
            dataModifyMap.put(likeExpression.getLeftExpression().toString(),likeData);
        }
    }

    @Override
    public void visit(MinorThan minorThan) {
        //todo
        if (minorThan.getLeftExpression() instanceof Function && minorThan.getRightExpression() instanceof Function) {

        } else {
            CompareData existData = (CompareData) dataModifyMap.get(minorThan.getLeftExpression().toString());
            if (existData == null) {
                CompareData compareData = new CompareData();
                compareData.setMirrorThan(minorThan.getRightExpression().toString().replace("'","").replace("\"", ""));
                dataModifyMap.put(minorThan.getLeftExpression().toString(), compareData);
            } else {
                existData.setMirrorThan(minorThan.getRightExpression().toString().replace("'","").replace("\"", ""));
                dataModifyMap.put(minorThan.getLeftExpression().toString(), existData);
            }
        }
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        //todo
        if (minorThanEquals.getLeftExpression() instanceof Function && minorThanEquals.getRightExpression() instanceof Function) {

        } else {
            CompareData existData = (CompareData) dataModifyMap.get(minorThanEquals.getLeftExpression().toString());
            if (existData == null) {
                CompareData compareData = new CompareData();
                compareData.setMirrorThanEquals(minorThanEquals.getRightExpression().toString().replace("'","").replace("\"", ""));
                dataModifyMap.put(minorThanEquals.getLeftExpression().toString(), compareData);
            } else {
                existData.setMirrorThanEquals(minorThanEquals.getRightExpression().toString().replace("'","").replace("\"", ""));
                dataModifyMap.put(minorThanEquals.getLeftExpression().toString(), existData);
            }
        }
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        if (notEqualsTo.getLeftExpression() instanceof Function && notEqualsTo.getRightExpression() instanceof Function) {
            //todo 左右两边有function的情况
        }else {
            EqualsData equalsData = new EqualsData();
            equalsData.setEqualsFlag(false);
            equalsData.setEqualsValue(notEqualsTo.getRightExpression().toString().replace("\"", "").replace("\'", ""));
            dataModifyMap.put(notEqualsTo.getLeftExpression().toString(),equalsData);
        }
    }

    public String getColumnName(Expression expression){
        ColumnFinder columnFinder = new ColumnFinder();

        expression.accept(columnFinder);

        if (columnFinder.getColumnName()==null){
            throw new IllegalArgumentException("未发现列名");
        }

        return columnFinder.getColumnName();

    }

}
