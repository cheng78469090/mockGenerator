package com.yoyosys.mock.util;

import com.apifan.common.random.source.DateTimeSource;
import com.apifan.common.random.source.NumberSource;
import com.mifmif.common.regex.Generex;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.yoyosys.mock.pojo.Column;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import javax.xml.transform.Result;
import java.io.BufferedReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yoyosys.mock.util.IsDateFormat.isRqFormat;

/**
 * @Author: yjj
 * Date: 2021/9/6
 */
public class ModifyDataUtil {

    private LinkedHashMap<Column,List> data = null;

    private List<Column> columns = null;

    private List<Expression> expressions = null;

    private JsqlparserUtil jsqlparserUtil = new JsqlparserUtil();

    private Long counterExample = 3L;


    /**
     * 主方法
     * @param data
     * @param columns
     * @param expressions
     * @return
     */
    public LinkedHashMap<Column,List> modifyData(LinkedHashMap<Column,List> data, List<Column> columns,List<Expression> expressions){
        this.data = data;
        this.columns = columns;
        this.expressions = expressions;
        if (data.get(0).size()<counterExample){
            System.out.println("生成的数据不能小于反例数据"+counterExample);
        } else {
            for (Expression expression : expressions) {
                getParser(expression);
            }
        }
        return this.data;
    }

    private void getParser(Expression expression) {
        if (expression instanceof InExpression) {
            List<String> strings = equalsColumn(expression);
            boolean not = ((InExpression) expression).isNot();
            parserInExpression(expression,strings,((Integer)strings.size()).longValue());
            ((InExpression) expression).setNot(!not);
            parserInExpression(expression,strings,counterExample);

        } else if (expression instanceof IsNullExpression) {
            List<String> strings = equalsColumn(expression);
            parserIsNullExpression(expression,strings,counterExample);

        } else if (expression instanceof Between) {
            List<String> strings = equalsColumn(expression);
            parserBetweenExpression((Between) expression,strings,((Integer)strings.size()).longValue());
            ((Between) expression).setNot(!((Between) expression).isNot());
            parserBetweenExpression((Between) expression,strings,counterExample);

        } else if (expression instanceof EqualsTo) {
            List<String> strings = equalsColumn(expression);
            parserEqualsToExpression(expression,strings,((Integer)strings.size()).longValue());
            parserNotEqualsToExpression(expression,strings,counterExample);

        } else if (expression instanceof LikeExpression) {
            List<String> strings = equalsColumn(expression);
            parserLikeExpression(expression,strings,((Integer)strings.size()).longValue());
            ((LikeExpression) expression).setNot(!((LikeExpression) expression).isNot());
            parserLikeExpression(expression,strings,counterExample);

        } else if (expression instanceof NotEqualsTo) {
            List<String> strings = equalsColumn(expression);
            parserNotEqualsToExpression(expression,strings,((Integer)strings.size()).longValue());
            parserEqualsToExpression(expression,strings,counterExample);

        } else if (expression instanceof GreaterThan) {
            List<String> strings = equalsColumn(expression);
            parserGreaterThanToExpression(expression,strings,((Integer)strings.size()).longValue());
            parserMinorThanEqualsToExpression(expression,strings,counterExample);

        } else if (expression instanceof GreaterThanEquals) {
            List<String> strings = equalsColumn(expression);
            parserGreaterThanEqualsToExpression(expression,strings,((Integer)strings.size()).longValue());
            parserMinorThanToExpression(expression,strings,counterExample);

        } else if (expression instanceof MinorThan) {
            List<String> strings = equalsColumn(expression);
            parserMinorThanToExpression(expression,strings,((Integer)strings.size()).longValue());
            parserGreaterThanEqualsToExpression(expression,strings,counterExample);

        } else if (expression instanceof MinorThanEquals) {
            List<String> strings = equalsColumn(expression);
            parserMinorThanEqualsToExpression(expression,strings,((Integer)strings.size()).longValue());
            parserGreaterThanToExpression(expression,strings,counterExample);

        }else if (expression instanceof AndExpression) {
            Expression leftExpression = ((AndExpression) expression).getLeftExpression();
            Expression rightExpression = ((AndExpression) expression).getRightExpression();
            List<String> strings = equalsColumn(leftExpression);
            parserParenthesisExpression(leftExpression,rightExpression,false,strings,((Integer)strings.size()).longValue());
            parserParenthesisExpression(leftExpression,rightExpression,true,strings,counterExample);
        }else if (expression instanceof OrExpression) {
            Expression leftExpression = ((OrExpression) expression).getLeftExpression();
            Expression rightExpression = ((OrExpression) expression).getRightExpression();
            List<String> strings = equalsColumn(leftExpression);
            parserParenthesisExpression(leftExpression,rightExpression,true,strings,((Integer)strings.size()).longValue());
            parserParenthesisExpression(leftExpression,rightExpression,false,strings,counterExample);
        }
    }

    /**
     *  寻找对应的数据列
     * @param expression
     * @return
     */
    public List<String> equalsColumn(Expression expression){
        Expression leftExpression = null;
        if (expression instanceof BinaryExpression) {
            leftExpression = ((BinaryExpression)expression).getLeftExpression();
        }else if (expression instanceof Between) {
            leftExpression = ((Between) expression).getLeftExpression();
        }else if (expression instanceof IsNullExpression) {
            leftExpression = ((IsNullExpression) expression).getLeftExpression();
        }else if (expression instanceof InExpression) {
            leftExpression = ((InExpression) expression).getLeftExpression();
        }

        List<String> strings = null;
        if (leftExpression instanceof net.sf.jsqlparser.schema.Column) {
            String columnName = ((net.sf.jsqlparser.schema.Column) leftExpression).getColumnName();
            for (Column column : columns) {
                if (columnName.equals(column.getFieldName())){
                    strings = data.get(column);
                    break;
                }
            }
        }else {
            //todo 抛出左侧为表达式异常
        }
        return strings;
    }

    /**
     * 解析in关键字左边的条件
     *
     * @param expression
     */
    public void parserInExpression(Expression expression,List<String> strings,Long size) {
        ItemsList rightItemsList = ((InExpression) expression).getRightItemsList();
        List<Expression> expressions = ((ExpressionList) rightItemsList).getExpressions();
        boolean isNot = ((InExpression) expression).isNot();
        if (!isNot){
            for (int i=0;i<size;i++){
                strings.set(i,expressions.get(i%expressions.size()).toString().replace("\"","").replace("\'",""));
            }
        } else {
            //todo
            if (expressions.get(0) instanceof LongValue){
                Generex generex = new Generex("[1-9]{" + expressions.get(0).toString().length() + "}");
                for (int i=0;i<size;i++){
                    String s = "";
                    boolean flag = true;
                    while(true){
                        s = generex.random();
                        for (Expression expression1 : expressions) {
                            flag = true;
                            if (s==expression1.toString()){
                                flag = false;
                                break;
                            }
                        }
                        if (flag){
                            strings.set(i, s);
                            break;
                        }
                    }
                }
            }else if (expressions.get(0) instanceof DoubleValue) {
                String[] split = expressions.get(0).toString().split(".");
                Generex generex =new Generex("[1-9]{"+split[0].length()+"}\\.[1-9]{"+split[1].length()+"}");
                for (int i=0;i<size;i++){
                    String s = "";
                    boolean flag = true;
                    while(true){
                        s = generex.random();
                        for (Expression expression1 : expressions) {
                            flag = true;
                            if (s==expression1.toString()){
                                flag = false;
                                break;
                            }
                        }
                        if (flag){
                            strings.set(i, s);
                            break;
                        }
                    }
                }
            }else if (IsDateFormat.isRqFormat(expressions.get(0).toString().replace("\"", "").replace("\'", ""))) {
                SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
                ZoneId zoneId = ZoneId.systemDefault();
                for (int i=0;i<size;i++){
                    boolean flag = true;
                    String random = "";
                    while(true){
                        try {
                            random = DateTimeSource.getInstance().randomFutureDate("yyyyMMdd");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        for (Expression expression1 : expressions) {
                            flag = true;
                            if (random==expression1.toString()){
                                flag = false;
                                break;
                            }
                        }
                        if (flag){
                            strings.set(i, random);
                            break;
                        }
                    }
                }
            } else {
                Generex generex = new Generex("[a-z0-9A-Z]{"+expressions.get(0).toString().replace("\"", "").replace("\'", "").length()+"}");
                for (int i=0;i<size;i++){
                    String s = "";
                    boolean flag = true;
                    while(true){
                        s = generex.random();
                        for (Expression expression1 : expressions) {
                            flag = true;
                            if (s==expression1.toString()){
                                flag = false;
                                break;
                            }
                        }
                        if (flag){
                            strings.set(i, s);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @param leftExpression
     * @param rightExpression
     * @param isNot  false 为 and  ||  true 为 or
     * @param strings
     */
    public void parserParenthesisExpression(Expression leftExpression,Expression rightExpression,Boolean isNot,List<String> strings,Long size) {
        //最小值
        Expression min = null;

        //最大值
        Expression max = null;

        //小于等于
        boolean minEquals = true;
        //大于等于
        boolean maxEquals = true;
        if (leftExpression instanceof MinorThanEquals ){
            minEquals = true;
            max = ((MinorThanEquals)leftExpression).getRightExpression();
            if (rightExpression instanceof GreaterThanEquals) {
                min = ((GreaterThanEquals) rightExpression).getRightExpression();
                maxEquals = true;
            } else if (rightExpression instanceof GreaterThan) {
                min = ((GreaterThan) rightExpression).getRightExpression();
                maxEquals = false;
            }
        } else if (leftExpression instanceof MinorThan) {
            minEquals = false;
            max = ((MinorThanEquals)leftExpression).getRightExpression();
            if (rightExpression instanceof GreaterThanEquals) {
                min = ((GreaterThanEquals) rightExpression).getRightExpression();
                maxEquals = true;
            } else if (rightExpression instanceof GreaterThan) {
                min = ((GreaterThan) rightExpression).getRightExpression();
                maxEquals = false;
            }
        } else if (leftExpression instanceof GreaterThanEquals){
            maxEquals = true;
            min = ((GreaterThanEquals) leftExpression).getRightExpression();
            if (rightExpression instanceof MinorThanEquals) {
                max = ((MinorThanEquals) rightExpression).getRightExpression();
                minEquals = true;
            } else {
                max = ((MinorThanEquals) rightExpression).getRightExpression();
                minEquals = false;
            }
        } else {
            maxEquals = false;
            min = ((GreaterThanEquals) leftExpression).getRightExpression();
            if (rightExpression instanceof MinorThanEquals) {
                max = ((MinorThanEquals) rightExpression).getRightExpression();
                minEquals = true;
            } else {
                max = ((MinorThanEquals) rightExpression).getRightExpression();
                minEquals = false;
            }
        }
        if (min.toString().compareTo(max.toString())>0){
            Expression expression = max;
            max = min;
            min = expression;
        }
        String minReplace = min.toString().replace("\"", "").replace("\'", "");
        String maxReplace = max.toString().replace("\"", "").replace("\'", "");
        if (!isNot){
            if(max instanceof LongValue){
                if (minEquals==false && maxEquals==false){
                    for (int i=0;i<size;i++){
                        Long l = ThreadLocalRandom.current().nextLong(Long.parseLong(min.toString())-1, Long.parseLong(max.toString())-1);
                        strings.set(i,l.toString());
                    }
                } else if (minEquals==true && maxEquals == false) {
                    for (int i=0;i<size;i++){
                        Long l = ThreadLocalRandom.current().nextLong(Long.parseLong(min.toString())-1, Long.parseLong(max.toString()));
                        strings.set(i,l.toString());
                    }
                } else if (minEquals==false && maxEquals == true) {
                    for (int i=0;i<size;i++){
                        Long l = ThreadLocalRandom.current().nextLong(Long.parseLong(min.toString()), Long.parseLong(max.toString())-1);
                        strings.set(i,l.toString());
                    }
                } else if (minEquals==true && maxEquals == true) {
                    for (int i=0;i<size;i++){
                        Long l = ThreadLocalRandom.current().nextLong(Long.parseLong(min.toString()), Long.parseLong(max.toString()));
                        strings.set(i,l.toString());
                    }
                }
            } else if (max instanceof DoubleValue) {
                if (minEquals==false && maxEquals==false){
                    for (int i=0;i<size;i++){
                        Double l = ThreadLocalRandom.current().nextDouble(Double.parseDouble(min.toString())-1, Double.parseDouble(max.toString())-1);
                        strings.set(i,l.toString());
                    }
                } else if (minEquals==true && maxEquals == false) {
                    for (int i=0;i<size;i++){
                        Double l = ThreadLocalRandom.current().nextDouble(Double.parseDouble(min.toString())-1, Double.parseDouble(max.toString()));
                        strings.set(i,l.toString());
                    }
                } else if (minEquals==false && maxEquals == true) {
                    for (int i=0;i<size;i++){
                        Double l = ThreadLocalRandom.current().nextDouble(Double.parseDouble(min.toString()), Double.parseDouble(max.toString())-1);
                        strings.set(i,l.toString());
                    }
                } else if (minEquals==true && maxEquals == true) {
                    for (int i=0;i<size;i++){
                        Double l = ThreadLocalRandom.current().nextDouble(Double.parseDouble(min.toString()), Double.parseDouble(max.toString()));
                        strings.set(i,l.toString());
                    }
                }
            } else if (IsDateFormat.isRqFormat(maxReplace)){
                SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDate maxDate = null;
                LocalDate minDate = null;
                try {
                    maxDate = yyyyMMdd.parse(maxReplace).toInstant().atZone(zoneId).toLocalDate();
                    minDate = yyyyMMdd.parse(minReplace).toInstant().atZone(zoneId).toLocalDate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (minEquals==false && maxEquals==false){
                    for (int i=0;i<size;i++){
                        String yyyyMMdd1 = DateTimeSource.getInstance().randomDate(minDate.plusDays(1), maxDate.minusDays(1), "yyyyMMdd");
                        strings.set(i,yyyyMMdd1.toString());
                    }
                } else if (minEquals==true && maxEquals == false) {
                    for (int i=0;i<size;i++){
                        String yyyyMMdd1 = DateTimeSource.getInstance().randomDate(minDate.plusDays(1), maxDate, "yyyyMMdd");
                        strings.set(i,yyyyMMdd1.toString());
                    }
                } else if (minEquals==false && maxEquals == true) {
                    for (int i=0;i<size;i++){
                        String yyyyMMdd1 = DateTimeSource.getInstance().randomDate(minDate, maxDate.minusDays(1), "yyyyMMdd");
                        strings.set(i,yyyyMMdd1.toString());
                    }
                } else if (minEquals==true && maxEquals == true) {
                    for (int i=0;i<size;i++){
                        String yyyyMMdd1 = DateTimeSource.getInstance().randomDate(minDate, maxDate, "yyyyMMdd");
                        strings.set(i,yyyyMMdd1.toString());
                    }
                }
            }
        } else {
            if(max instanceof LongValue){
                if (minEquals==false && maxEquals==false){
                    for (int i=0;i<size;i++){
                        Long l = ThreadLocalRandom.current().nextLong(Long.parseLong(min.toString())-10*strings.size(),Long.parseLong(min.toString())-1);
                        Long s = ThreadLocalRandom.current().nextLong(Long.parseLong(max.toString())+1,Long.parseLong(max.toString())+10*strings.size());
                        if(i%2==0){
                            strings.set(i,l.toString());
                        }else {
                            strings.set(i,s.toString());
                        }
                    }
                } else if (minEquals==true && maxEquals == false) {
                    for (int i=0;i<size;i++){
                        Long l = ThreadLocalRandom.current().nextLong(Long.parseLong(min.toString())-10*strings.size(),Long.parseLong(min.toString()));
                        Long s = ThreadLocalRandom.current().nextLong(Long.parseLong(max.toString())+1,Long.parseLong(max.toString())+10*strings.size());
                        if(i%2==0){
                            strings.set(i,l.toString());
                        }else {
                            strings.set(i,s.toString());
                        }
                    }
                } else if (minEquals==false && maxEquals == true) {
                    for (int i=0;i<size;i++){
                        Long l = ThreadLocalRandom.current().nextLong(Long.parseLong(min.toString())-10*strings.size(),Long.parseLong(min.toString())-1);
                        Long s = ThreadLocalRandom.current().nextLong(Long.parseLong(max.toString()),Long.parseLong(max.toString())+10*strings.size());
                        if(i%2==0){
                            strings.set(i,l.toString());
                        }else {
                            strings.set(i,s.toString());
                        }
                    }
                } else if (minEquals==true && maxEquals == true) {
                    for (int i=0;i<size;i++){
                        Long l = ThreadLocalRandom.current().nextLong(Long.parseLong(min.toString())-10*strings.size(),Long.parseLong(min.toString()));
                        Long s = ThreadLocalRandom.current().nextLong(Long.parseLong(max.toString()),Long.parseLong(max.toString())+10*strings.size());
                        if(i%2==0){
                            strings.set(i,l.toString());
                        }else {
                            strings.set(i,s.toString());
                        }
                    }
                }
            } else if (max instanceof DoubleValue) {
                if (minEquals==false && maxEquals==false){
                    for (int i=0;i<size;i++){
                        Double l = ThreadLocalRandom.current().nextDouble(Double.parseDouble(min.toString())-10*strings.size(), Double.parseDouble(min.toString())-1);
                        Double s = ThreadLocalRandom.current().nextDouble(Double.parseDouble(min.toString())+1, Double.parseDouble(min.toString())+10*strings.size());
                        if(i%2==0){
                            strings.set(i,l.toString());
                        }else {
                            strings.set(i,s.toString());
                        }
                    }
                } else if (minEquals==true && maxEquals == false) {
                    for (int i=0;i<size;i++){
                        Double l = ThreadLocalRandom.current().nextDouble(Double.parseDouble(min.toString())-10*strings.size(), Double.parseDouble(min.toString()));
                        Double s = ThreadLocalRandom.current().nextDouble(Double.parseDouble(min.toString())+1, Double.parseDouble(min.toString())+10*strings.size());
                        if(i%2==0){
                            strings.set(i,l.toString());
                        }else {
                            strings.set(i,s.toString());
                        }
                    }
                } else if (minEquals==false && maxEquals == true) {
                    for (int i=0;i<size;i++){
                        Double l = ThreadLocalRandom.current().nextDouble(Double.parseDouble(min.toString())-10*strings.size(), Double.parseDouble(min.toString())-1);
                        Double s = ThreadLocalRandom.current().nextDouble(Double.parseDouble(min.toString()), Double.parseDouble(min.toString())+10*strings.size());
                        if(i%2==0){
                            strings.set(i,l.toString());
                        }else {
                            strings.set(i,s.toString());
                        }
                    }
                } else if (minEquals==true && maxEquals == true) {
                    for (int i=0;i<size;i++){
                        Double l = ThreadLocalRandom.current().nextDouble(Double.parseDouble(min.toString())-10*strings.size(), Double.parseDouble(min.toString()));
                        Double s = ThreadLocalRandom.current().nextDouble(Double.parseDouble(min.toString()), Double.parseDouble(min.toString())+10*strings.size());
                        if(i%2==0){
                            strings.set(i,l.toString());
                        }else {
                            strings.set(i,s.toString());
                        }
                    }
                }
            } else if (IsDateFormat.isRqFormat(maxReplace)){
                SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDate maxDate = null;
                LocalDate minDate = null;
                try {
                    maxDate = yyyyMMdd.parse(maxReplace).toInstant().atZone(zoneId).toLocalDate();
                    minDate = yyyyMMdd.parse(minReplace).toInstant().atZone(zoneId).toLocalDate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (minEquals==false && maxEquals==false){
                    for (int i=0;i<size;i++){
                        String yyyyMMdd1 = DateTimeSource.getInstance().randomPastDate(minDate.minusDays(1), "yyyyMMdd");
                        String yyyyMMdd2 = DateTimeSource.getInstance().randomFutureDate(maxDate.plusDays(1), "yyyyMMdd");
                        if(i%2==0){
                            strings.set(i,yyyyMMdd1);
                        }else {
                            strings.set(i,yyyyMMdd2);
                        }
                    }
                } else if (minEquals==true && maxEquals == false) {
                    for (int i=0;i<size;i++){
                        String yyyyMMdd1 = DateTimeSource.getInstance().randomPastDate(minDate, "yyyyMMdd");
                        String yyyyMMdd2 = DateTimeSource.getInstance().randomFutureDate(maxDate.plusDays(1), "yyyyMMdd");
                        if(i%2==0){
                            strings.set(i,yyyyMMdd1);
                        }else {
                            strings.set(i,yyyyMMdd2);
                        }
                    }
                } else if (minEquals==false && maxEquals == true) {
                    for (int i=0;i<size;i++){
                        String yyyyMMdd1 = DateTimeSource.getInstance().randomPastDate(minDate.minusDays(1), "yyyyMMdd");
                        String yyyyMMdd2 = DateTimeSource.getInstance().randomFutureDate(maxDate, "yyyyMMdd");
                        if(i%2==0){
                            strings.set(i,yyyyMMdd1);
                        }else {
                            strings.set(i,yyyyMMdd2);
                        }
                    }
                } else if (minEquals==true && maxEquals == true) {
                    for (int i=0;i<size;i++){
                        String yyyyMMdd1 = DateTimeSource.getInstance().randomPastDate(minDate, "yyyyMMdd");
                        String yyyyMMdd2 = DateTimeSource.getInstance().randomFutureDate(maxDate, "yyyyMMdd");
                        if(i%2==0){
                            strings.set(i,yyyyMMdd1);
                        }else {
                            strings.set(i,yyyyMMdd2);
                        }
                    }
                }
            }
        }
    }
    /**
     * 解析is null 和 is not null关键字左边的条件
     * is null 为 false，is not null 为 true
     * @param expression
     */
    public void parserIsNullExpression(Expression expression,List<String> strings,Long size) {
        Expression leftExpression = ((IsNullExpression) expression).getLeftExpression();
        boolean not = ((IsNullExpression) expression).isNot();
        if (!not) {
            for (int i=Integer.valueOf(size.toString());i<strings.size();i++){
                strings.set(i,null);
            }
        } else {
            for (int i=0;i<size;i++){
                strings.set(i,null);
            }
        }
    }

    /**
     * between and
     * @param expression
     */
    public void parserBetweenExpression(Between expression,List<String> strings,Long size) {
        Expression leftExpression = expression.getLeftExpression();
        Expression betweenExpressionStart = expression.getBetweenExpressionStart();
        Expression betweenExpressionEnd = expression.getBetweenExpressionEnd();
        if (betweenExpressionStart instanceof SignedExpression) {
            betweenExpressionStart = ((SignedExpression) betweenExpressionStart).getExpression();
        }
        if (betweenExpressionEnd instanceof SignedExpression) {
            betweenExpressionEnd = ((SignedExpression) betweenExpressionEnd).getExpression();
        }
        String replaceStart = betweenExpressionStart.toString().replace("\"", "").replace("\'", "");
        String replaceEnd = betweenExpressionEnd.toString().replace("\"", "").replace("\'", "");
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        ZoneId zoneId = ZoneId.systemDefault();
        boolean not = expression.isNot();
        if (!not){
            if (betweenExpressionStart instanceof LongValue && betweenExpressionEnd instanceof LongValue) {
                Long start = Long.parseLong(betweenExpressionStart.toString());
                Long end = Long.parseLong(betweenExpressionEnd.toString());
                for (int i=0;i<size;i++){
                    strings.set(i,String.valueOf(ThreadLocalRandom.current().nextLong(start, end)));
                }
            }else if (betweenExpressionStart instanceof DoubleValue || betweenExpressionEnd instanceof DoubleValue) {
                Double start = Double.valueOf(betweenExpressionStart.toString());
                Double end = Double.valueOf(betweenExpressionEnd.toString());
                for (int i=0;i<size;i++){
                    strings.set(i,String.valueOf(ThreadLocalRandom.current().nextDouble(start, end)));
                }
            }else if (IsDateFormat.isRqFormat(replaceStart)) {
                try {
                    LocalDate start = yyyyMMdd.parse(replaceStart).toInstant().atZone(zoneId).toLocalDate();
                    LocalDate end = yyyyMMdd.parse(replaceEnd).toInstant().atZone(zoneId).toLocalDate();
                    for (int i=0;i<size;i++){
                        strings.set(i, DateTimeSource.getInstance().randomDate(start,end,"yyyyMMdd"));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (betweenExpressionStart instanceof LongValue && betweenExpressionEnd instanceof LongValue) {
                Long start = Long.parseLong(betweenExpressionStart.toString());
                Long end = Long.parseLong(betweenExpressionEnd.toString());
                for (int i=0;i<size;i++){
                    if (i%2==0){
                        strings.set(i,String.valueOf(ThreadLocalRandom.current().nextLong(start-10*size,start-1)));
                    }else {
                        strings.set(i,String.valueOf(ThreadLocalRandom.current().nextLong(end+1,end+10*size)));
                    }
                }
            }else if (betweenExpressionStart instanceof DoubleValue || betweenExpressionEnd instanceof DoubleValue) {
                Double start = Double.valueOf(betweenExpressionStart.toString());
                Double end = Double.valueOf(betweenExpressionEnd.toString());
                for (int i=0;i<size;i++){
                    if (i%2==0){
                        strings.set(i,String.valueOf(ThreadLocalRandom.current().nextDouble(start-10*size,start-1)));
                    }else {
                        strings.set(i,String.valueOf(ThreadLocalRandom.current().nextDouble(end+1,end+10*size)));
                    }
                }
            }else if (IsDateFormat.isRqFormat(replaceStart)) {
                try {
                    LocalDate start = yyyyMMdd.parse(replaceStart).toInstant().atZone(zoneId).toLocalDate();
                    LocalDate end = yyyyMMdd.parse(replaceEnd).toInstant().atZone(zoneId).toLocalDate();
                    for (int i=0;i<size;i++){
                        if (i%2==0){
                            strings.set(i,String.valueOf(DateTimeSource.getInstance().randomDate(start.minusDays(10*size),start.minusDays(1),"yyyyMMdd")));
                        }else {
                            strings.set(i,String.valueOf(DateTimeSource.getInstance().randomDate(end.plusDays(1),end.plusDays(10*size),"yyyyMMdd")));
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * like not like
     * @param expression
     */
    public void parserLikeExpression(Expression expression,List<String> strings,Long size) {
        Expression rightExpression = ((BinaryExpression) expression).getRightExpression();
        String[] split = rightExpression.toString().split("");
        Generex generex1 = new Generex("[0-9A-Za-z]");
        Generex generex2 = new Generex("[0-9A-Za-z]{1,5}");
        if (((LikeExpression) expression).isNot()){
            for (int i=0;i<size;i++){
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : split) {
                    if ("_".equals(s)){
                        stringBuilder.append(generex1.random());
                    } else if ("%".equals(s)){
                        stringBuilder.append(generex2.random());
                    } else {
                        while (true){
                            String random = generex1.random();
                            if (random!=s){
                                stringBuilder.append(random);
                                break;
                            }
                        }
                    }
                }
                strings.set(i,stringBuilder.toString());
            }
        } else {
            for (int i=0;i<size;i++){
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : split) {
                    if ("_".equals(s)){
                        stringBuilder.append(generex1.random());
                    } else if ("%".equals(s)){
                        stringBuilder.append(generex2.random());
                    } else {
                        stringBuilder.append(s);
                    }
                }
                strings.set(i,stringBuilder.toString());
            }
        }
    }


    /**
     * =
     * @param expression
     */
    public void parserEqualsToExpression(Expression expression,List<String> strings,Long size) {
        Expression rightExpression = ((BinaryExpression) expression).getRightExpression();
        for (int i=0;i<size;i++){
            strings.set(i,rightExpression.toString().replace("\"","").replace("\'",""));
        }
    }

    /**
     * !=
     * @param expression
     */
    public void parserNotEqualsToExpression(Expression expression,List<String> strings,Long size) {
        Expression rightExpression = ((BinaryExpression) expression).getRightExpression();
        if (rightExpression instanceof SignedExpression) {
            rightExpression = ((SignedExpression) rightExpression).getExpression();
        }
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        ZoneId zoneId = ZoneId.systemDefault();
        if (IsDateFormat.isRqFormat(rightExpression.toString())) {
            try {
                for (int i=0;i<size;i++){
                    int year = yyyyMMdd.parse(rightExpression.toString()).getYear();
                    String yyyyMMdd1 = DateTimeSource.getInstance().randomDate(year, "yyyyMMdd");
                    if (!yyyyMMdd1.equals(rightExpression.toString())){
                        strings.set(i,DateTimeSource.getInstance().randomDate(year, "yyyyMMdd"));
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (rightExpression instanceof LongValue) {
            Long a = ((LongValue) rightExpression).getValue();
            for (int i=0;i<size;i++){
                Long l = ThreadLocalRandom.current().nextLong(a - strings.size() * 10, a + strings.size() * 10);
                if (!a.equals(l)){
                    strings.set(i,l.toString());
                }
            }
        } else if (rightExpression instanceof DoubleValue) {
            Double a = ((DoubleValue) rightExpression).getValue();
            for (int i=0;i<size;i++){
                Double l = ThreadLocalRandom.current().nextDouble(a - strings.size() * 10, a + strings.size() * 10);
                if (!a.equals(l)){
                    strings.set(i,l.toString());
                } else {
                    i--;
                }
            }
        } else if (rightExpression instanceof StringValue) {
            String value = ((StringValue) rightExpression).getValue().replace("\"", "").replace("\'", "");;
            int length = value.length();
            for (int i=0;i<size;i++){
                String s = RandomStringUtils.randomAlphanumeric(length);
                if (!s.equals(value)) {
                    strings.set(i, RandomStringUtils.randomAlphanumeric(length));
                } else {
                    i--;
                }
            }
        }
    }

    /**
     * >
     * @param expression
     */
    public void parserGreaterThanToExpression(Expression expression,List<String> strings,Long size) {
        Expression rightExpression = ((BinaryExpression) expression).getRightExpression();
        if (rightExpression instanceof SignedExpression) {
            rightExpression = ((SignedExpression) rightExpression).getExpression();
        }
        String replace = rightExpression.toString().replace("\"", "").replace("\'", "");
        if (rightExpression instanceof LongValue) {
            Long l = ((LongValue) rightExpression).getValue();
            for (int i=0;i<size;i++){
                Long l1 = ThreadLocalRandom.current().nextLong(l+1, l + strings.size() * 10);
                strings.set(i,l1.toString());
            }
        } else if (rightExpression instanceof DoubleValue) {
            Double value = ((DoubleValue) rightExpression).getValue();
            for (int i=0;i<size;i++){
                Double l1 = ThreadLocalRandom.current().nextDouble(value+1, value + strings.size() * 10);
                strings.set(i,l1.toString());
            }
        } else if (IsDateFormat.isRqFormat(replace)) {
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
            ZoneId zoneId = ZoneId.systemDefault();
            try {
                LocalDate localDate = yyyyMMdd.parse(replace).toInstant().atZone(zoneId).toLocalDate();
                for (int i=0;i<size;i++) {
                    String yyyyMMdd1 = DateTimeSource.getInstance().randomFutureDate(localDate.plusDays(1), "yyyyMMdd");
                    strings.set(i,yyyyMMdd1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * >=
     * @param expression
     */
    public void parserGreaterThanEqualsToExpression(Expression expression,List<String> strings,Long size) {
        Expression rightExpression = ((BinaryExpression) expression).getRightExpression();
        if (rightExpression instanceof SignedExpression) {
            rightExpression = ((SignedExpression) rightExpression).getExpression();
        }
        String replace = rightExpression.toString().replace("\"", "").replace("\'", "");
        if (rightExpression instanceof LongValue) {
            Long l = ((LongValue) rightExpression).getValue();
            for (int i=0;i<size;i++){
                Long l1 = ThreadLocalRandom.current().nextLong(l, l + strings.size() * 10);
                strings.set(i,l1.toString());
            }
        } else if (rightExpression instanceof DoubleValue) {
            Double value = ((DoubleValue) rightExpression).getValue();
            for (int i=0;i<size;i++){
                Double l1 = ThreadLocalRandom.current().nextDouble(value, value + strings.size() * 10);
                strings.set(i,l1.toString());
            }
        } else if (IsDateFormat.isRqFormat(replace)) {
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
            ZoneId zoneId = ZoneId.systemDefault();
            try {
                LocalDate localDate = yyyyMMdd.parse(replace).toInstant().atZone(zoneId).toLocalDate();
                for (int i=0;i<size;i++) {
                    String yyyyMMdd1 = DateTimeSource.getInstance().randomFutureDate(localDate, "yyyyMMdd");
                    strings.set(i,yyyyMMdd1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * <
     * @param expression
     */
    public void parserMinorThanToExpression(Expression expression,List<String> strings,Long size) {
        Expression rightExpression = ((BinaryExpression) expression).getRightExpression();
        if (rightExpression instanceof SignedExpression) {
            rightExpression = ((SignedExpression) rightExpression).getExpression();
        }
        String replace = rightExpression.toString().replace("\"", "").replace("\'", "");
        if (rightExpression instanceof LongValue) {
            Long l = ((LongValue) rightExpression).getValue();
            if ((l-strings.size() * 10)<0){
                for (int i=0;i<size;i++){
                    Long l1 = ThreadLocalRandom.current().nextLong(0, l - 1);
                    strings.set(i,l1.toString());
                }
            } else {
                for (int i=0;i<size;i++){
                    Long l1 = ThreadLocalRandom.current().nextLong(l - strings.size() * 10, l - 1);
                    strings.set(i,l1.toString());
                }
            }
        } else if (rightExpression instanceof DoubleValue) {
            Double l = ((DoubleValue) rightExpression).getValue();
            if ((l-strings.size() * 10)<0){
                for (int i=0;i<size;i++){
                    Double l1 = ThreadLocalRandom.current().nextDouble(0, l - 1);
                    strings.set(i,l1.toString());
                }
            } else {
                for (int i=0;i<size;i++){
                    Double l1 = ThreadLocalRandom.current().nextDouble(l - strings.size() * 10, l - 1);
                    strings.set(i,l1.toString());
                }
            }
        } else if (IsDateFormat.isRqFormat(replace)) {
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
            ZoneId zoneId = ZoneId.systemDefault();
            try {
                LocalDate localDate = yyyyMMdd.parse(replace).toInstant().atZone(zoneId).toLocalDate();
                for (int i=0;i<size;i++) {
                    String yyyyMMdd1 = DateTimeSource.getInstance().randomPastDate(localDate.minusDays(1), "yyyyMMdd");
                    strings.set(i,yyyyMMdd1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <=
     * @param expression
     */
    public void parserMinorThanEqualsToExpression(Expression expression,List<String> strings,Long size) {
        Expression rightExpression = ((BinaryExpression) expression).getRightExpression();
        if (rightExpression instanceof SignedExpression) {
            rightExpression = ((SignedExpression) rightExpression).getExpression();
        }
        String replace = rightExpression.toString().replace("\"", "").replace("\'", "");
        if (rightExpression instanceof LongValue) {
            Long l = ((LongValue) rightExpression).getValue();
            if ((l-strings.size() * 10)<0){
                for (int i=0;i<size;i++){
                    Long l1 = ThreadLocalRandom.current().nextLong(0, l);
                    strings.set(i,l1.toString());
                }
            } else {
                for (int i=0;i<size;i++){
                    Long l1 = ThreadLocalRandom.current().nextLong(l - strings.size() * 10, l);
                    strings.set(i,l1.toString());
                }
            }
        } else if (rightExpression instanceof DoubleValue) {
            Double l = ((DoubleValue) rightExpression).getValue();
            if ((l-strings.size() * 10)<0){
                for (int i=0;i<size;i++){
                    Double l1 = ThreadLocalRandom.current().nextDouble(0, l);
                    strings.set(i,l1.toString());
                }
            } else {
                for (int i=0;i<size;i++){
                    Double l1 = ThreadLocalRandom.current().nextDouble(l - strings.size() * 10, l);
                    strings.set(i,l1.toString());
                }
            }
        } else if (IsDateFormat.isRqFormat(replace)) {
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
            ZoneId zoneId = ZoneId.systemDefault();
            try {
                LocalDate localDate = yyyyMMdd.parse(replace).toInstant().atZone(zoneId).toLocalDate();
                for (int i=0;i<size;i++) {
                    String yyyyMMdd1 = DateTimeSource.getInstance().randomPastDate(localDate, "yyyyMMdd");
                    strings.set(i,yyyyMMdd1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


}
