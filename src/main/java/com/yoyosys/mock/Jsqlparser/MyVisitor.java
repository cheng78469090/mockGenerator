package com.yoyosys.mock.Jsqlparser;

import com.yoyosys.mock.Jsqlparser.dataType.*;
import com.yoyosys.mock.Jsqlparser.functionType.FunctionFactory;
import com.yoyosys.mock.MockData;
import com.yoyosys.mock.common.GlobalConstants;
import com.yoyosys.mock.pojo.Column;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

    private static Logger logger = LoggerFactory.getLogger(MockData.class);

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
        try {
            if (equalsTo.getLeftExpression() instanceof Function || equalsTo.getRightExpression() instanceof Function) {
                //todo ???????????????function?????????
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
        } catch (Exception e) {
            logger.error(GlobalConstants.LOG_PREFIX+equalsTo.toString(),e);
        }
    }

    @Override
    public void visit(Between between) {
        //todo
        try {
            if (between.getBetweenExpressionEnd() instanceof Function && between.getBetweenExpressionStart() instanceof Function) {
                //todo ???????????????function?????????
            } else {
                CompareData compareData = new CompareData();
                compareData.setGreaterThanEquals(between.getBetweenExpressionStart().toString().replace("\"", "").replace("\'", ""));
                compareData.setMirrorThanEquals(between.getBetweenExpressionEnd().toString().replace("\"", "").replace("\'", ""));
                dataModifyMap.put(between.getLeftExpression().toString(),compareData);
            }
        } catch (Exception e) {
            logger.error(GlobalConstants.LOG_PREFIX+between.toString(),e);
        }
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        try {
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
        } catch (Exception e) {
            logger.error(GlobalConstants.LOG_PREFIX+greaterThan.toString(),e);
        }
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        CompareData existData = null;
        try {
            if (greaterThanEquals.getLeftExpression() instanceof Function && greaterThanEquals.getRightExpression() instanceof Function) {

            }else {
                existData = (CompareData)dataModifyMap.get(greaterThanEquals.getLeftExpression().toString());
                if(existData == null){
                    CompareData compareData = new CompareData();
                    compareData.setGreaterThanEquals(greaterThanEquals.getRightExpression().toString().replace("'","").replace("\"", ""));
                    dataModifyMap.put(greaterThanEquals.getLeftExpression().toString(),compareData);
                }else {
                    existData.setGreaterThanEquals(greaterThanEquals.getRightExpression().toString().replace("'","").replace("\"", ""));
                    dataModifyMap.put(greaterThanEquals.getLeftExpression().toString(),existData);
                }
            }
        } catch (Exception e) {
            logger.error(GlobalConstants.LOG_PREFIX+greaterThanEquals.toString(),e);
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

        try {
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
        } catch (Exception e) {
            logger.error(GlobalConstants.LOG_PREFIX+inExpression.toString(),e);
        }
    }

    @Override
    public void visit(IsNullExpression isNullExpression) {
        //todo ?????????
        IsNullData isNullData = new IsNullData();
        try {
            if (isNullExpression.getLeftExpression() instanceof Function) {

            }else {
                isNullData.setNullFlag(!isNullExpression.isNot());
                dataModifyMap.put(isNullExpression.getLeftExpression().toString(),isNullData);
            }
        } catch (Exception e) {
            logger.error(GlobalConstants.LOG_PREFIX+isNullExpression.toString(),e);
        }
    }

    @Override
    public void visit(LikeExpression likeExpression) {
        //todo ?????????
        try {
            if (likeExpression.getLeftExpression() instanceof Function && likeExpression.getRightExpression() instanceof Function){

            } else {
                LikeData likeData = new LikeData();
                likeData.setLikeFlag(!likeExpression.isNot());
                likeData.setLikeValue(likeExpression.getRightExpression().toString().replace("\"", "").replace("\'", ""));
                dataModifyMap.put(likeExpression.getLeftExpression().toString(),likeData);
            }
        } catch (Exception e) {
            logger.error(GlobalConstants.LOG_PREFIX+likeExpression.toString(),e);
        }
    }

    @Override
    public void visit(MinorThan minorThan) {
        //todo
        try {
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
        } catch (Exception e) {
            logger.error(GlobalConstants.LOG_PREFIX+minorThan.toString(),e);
        }
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        //todo
        try {
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
        } catch (Exception e) {
            logger.error(GlobalConstants.LOG_PREFIX+minorThanEquals.toString(),e);
        }
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        try {
            if (notEqualsTo.getLeftExpression() instanceof Function && notEqualsTo.getRightExpression() instanceof Function) {
                //todo ???????????????function?????????
            }else {
                EqualsData equalsData = new EqualsData();
                equalsData.setEqualsFlag(false);
                equalsData.setEqualsValue(notEqualsTo.getRightExpression().toString().replace("\"", "").replace("\'", ""));
                dataModifyMap.put(notEqualsTo.getLeftExpression().toString(),equalsData);
            }
        } catch (Exception e) {
            logger.error(GlobalConstants.LOG_PREFIX+notEqualsTo.toString(),e);
        }
    }

    public String getColumnName(Expression expression){
        ColumnFinder columnFinder = new ColumnFinder();

        expression.accept(columnFinder);

        if (columnFinder.getColumnName()==null){
            throw new IllegalArgumentException("???????????????");
        }

        return columnFinder.getColumnName();

    }

}
