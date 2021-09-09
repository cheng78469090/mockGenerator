package com.yoyosys.mock.util;

import com.apifan.common.random.source.DateTimeSource;
import com.apifan.common.random.source.NumberSource;
import com.mifmif.common.regex.Generex;
import com.yoyosys.mock.pojo.Column;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.BufferedReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;
import java.util.*;
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
        getParser();
        return this.data;
    }

    private void getParser() {
        for (Expression expression : expressions) {
            List<String> strings = equalsColumn(expression);
            if (expression instanceof InExpression) {
                parserInExpression(expression,strings);
            } else if (expression instanceof IsNullExpression) {
                parserIsNullExpression(expression,strings);
            } else if (expression instanceof Between) {
                parserBetweenExpression(expression,strings);
            } else if (expression instanceof EqualsTo) {
                parserEqualsToExpression(expression,strings);
            } else if (expression instanceof LikeExpression) {
                parserLikeExpression(expression,strings);
            } else if (expression instanceof NotEqualsTo) {
                parserNotEqualsToExpression(expression,strings);
            } else if (expression instanceof GreaterThan) {
                parserGreaterThanToExpression(expression,strings);
            } else if (expression instanceof GreaterThanEquals) {
                parserGreaterThanEqualsToExpression(expression,strings);
            } else if (expression instanceof MinorThan) {
                parserMinorThanToExpression(expression,strings);
            } else if (expression instanceof MinorThanEquals) {
                parserMinorThanEqualsToExpression(expression,strings);
            }
        }
    }

    /**
     *  寻找对应的数据列
     * @param expression
     * @return
     */
    public List<String> equalsColumn(Expression expression){
        Expression leftExpression = ((IsNullExpression) expression).getLeftExpression();
        List<String> strings = null;
        if (leftExpression instanceof net.sf.jsqlparser.schema.Column) {
            String columnName = ((net.sf.jsqlparser.schema.Column) leftExpression).getColumnName();
            for (Column column : columns) {
                if (columnName.equals(column.getFieldName())){
                    strings = data.get(column);
                }
            }
        }
        return strings;
    }

    /**
     * 解析in关键字左边的条件
     *
     * @param expression
     */
    public void parserInExpression(Expression expression,List<String> strings) {
        ItemsList rightItemsList = ((InExpression) expression).getRightItemsList();
        List<Expression> expressions = ((ExpressionList) rightItemsList).getExpressions();
        boolean isNot = ((InExpression) expression).isNot();
        if (!isNot){
            for (int i=0;i<strings.size();i++){
                strings.set(i,expressions.get(i%expressions.size()).toString());
            }
        } else {
            //todo
            if (expressions.get(0) instanceof LongValue){
                Generex generex = new Generex("[1-9]{" + expressions.get(0).toString().length() + "}");
                for (int i=0;i<strings.size();i++){
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
                for (int i=0;i<strings.size();i++){
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
            }else if (IsDateFormat.isRqFormat(expressions.get(0).toString())) {
                SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
                ZoneId zoneId = ZoneId.systemDefault();
                for (int i=0;i<strings.size();i++){
                    boolean flag = true;
                    String random = "";
                    while(true){
                        try {
                            random = DateTimeSource.getInstance().randomDate(yyyyMMdd.parse(expressions.get(0).toString()).getYear(),"yyyyMMdd");
                        } catch (ParseException e) {
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
                Generex generex = new Generex("[a-z0-9A-Z]{"+expressions.get(0)+"}");
                for (int i=0;i<strings.size();i++){
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
     * 解析is null 和 is not null关键字左边的条件
     * is null 为 false，is not null 为 true
     * @param expression
     */
    public void parserIsNullExpression(Expression expression,List<String> strings) {
        Expression leftExpression = ((IsNullExpression) expression).getLeftExpression();
        boolean not = ((IsNullExpression) expression).isNot();
        if (!not) {
            for (int i=0;i<strings.size();i++){
                strings.set(i,null);
            }
        }
    }

    /**
     * between and
     * @param expression
     */
    public void parserBetweenExpression(Expression expression,List<String> strings) {
        Expression leftExpression = ((Between) expression).getLeftExpression();
        Expression betweenExpressionStart = ((Between) expression).getBetweenExpressionStart();
        Expression betweenExpressionEnd = ((Between) expression).getBetweenExpressionEnd();
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        ZoneId zoneId = ZoneId.systemDefault();
        if (betweenExpressionStart instanceof LongValue && betweenExpressionEnd instanceof LongValue) {
            Long start = Long.parseLong(betweenExpressionStart.toString());
            Long end = Long.parseLong(betweenExpressionEnd.toString());
            for (int i=0;i<strings.size();i++){
                strings.set(i,String.valueOf(NumberSource.getInstance().randomLong(start, end)));
            }
        }else if (betweenExpressionStart instanceof DoubleValue || betweenExpressionEnd instanceof DoubleValue) {
            Double start = Double.valueOf(betweenExpressionStart.toString());
            Double end = Double.valueOf(betweenExpressionEnd.toString());
            for (int i=0;i<strings.size();i++){
                strings.set(i,String.valueOf(NumberSource.getInstance().randomDouble(start, end)));
            }
        }else if (IsDateFormat.isRqFormat(betweenExpressionStart.toString())) {
            try {
                LocalDate start = yyyyMMdd.parse(betweenExpressionStart.toString()).toInstant().atZone(zoneId).toLocalDate();
                LocalDate end = yyyyMMdd.parse(betweenExpressionEnd.toString()).toInstant().atZone(zoneId).toLocalDate();
                for (int i=0;i<strings.size();i++){
                    strings.set(i, DateTimeSource.getInstance().randomDate(start,end,"yyyyMMdd"));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * =
     * @param expression
     */
    public void parserEqualsToExpression(Expression expression,List<String> strings) {
        Expression rightExpression = ((EqualsTo) expression).getRightExpression();
        for (int i=0;i<strings.size();i++){
            strings.set(i,rightExpression.toString());
        }
    }

    /**
     * like not like
     * @param expression
     */
    public void parserLikeExpression(Expression expression,List<String> strings) {
        Expression rightExpression = ((LikeExpression) expression).getRightExpression();
        String[] split = rightExpression.toString().split("");
        Generex generex1 = new Generex("[0-9A-Za-z]");
        Generex generex2 = new Generex("[0-9A-Za-z]{1,5}");
        if (((LikeExpression) expression).isNot()){
            for (int i=0;i<strings.size();i++){
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
            for (int i=0;i<strings.size();i++){
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
     * !=
     * @param expression
     */
    public void parserNotEqualsToExpression(Expression expression,List<String> strings) {
        Expression rightExpression = ((NotEqualsTo) expression).getRightExpression();
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        ZoneId zoneId = ZoneId.systemDefault();
        if (IsDateFormat.isRqFormat(rightExpression.toString())) {
            try {
                for (int i=0;i<strings.size();i++){
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
            for (int i=0;i<strings.size();i++){
                Long l = NumberSource.getInstance().randomLong(a - strings.size() * 10, a + strings.size() * 10);
                if (!a.equals(l)){
                    strings.set(i,l.toString());
                }
            }
        } else if (rightExpression instanceof DoubleValue) {
            Double a = ((DoubleValue) rightExpression).getValue();
            for (int i=0;i<strings.size();i++){
                Double l = NumberSource.getInstance().randomDouble(a - strings.size() * 10, a + strings.size() * 10);
                if (!a.equals(l)){
                    strings.set(i,l.toString());
                } else {
                    i--;
                }
            }
        } else if (rightExpression instanceof StringValue) {
            String value = ((StringValue) rightExpression).getValue();
            int length = value.length();
            for (int i=0;i<strings.size();i++){
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
    public void parserGreaterThanToExpression(Expression expression,List<String> strings) {
        Expression rightExpression = ((GreaterThan) expression).getRightExpression();
        if (rightExpression instanceof LongValue) {
            Long l = ((LongValue) rightExpression).getValue();
            for (int i=0;i<strings.size();i++){
                Long l1 = NumberSource.getInstance().randomLong(l+1, l + strings.size() * 10);
                strings.set(i,l1.toString());
            }
        } else if (rightExpression instanceof DoubleValue) {
            Double value = ((DoubleValue) rightExpression).getValue();
            for (int i=0;i<strings.size();i++){
                Double l1 = NumberSource.getInstance().randomDouble(value+1, value + strings.size() * 10);
                strings.set(i,l1.toString());
            }
        } else if (IsDateFormat.isRqFormat(rightExpression.toString())) {
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
            ZoneId zoneId = ZoneId.systemDefault();
            try {
                LocalDate localDate = yyyyMMdd.parse(rightExpression.toString()).toInstant().atZone(zoneId).toLocalDate();
                for (int i=0;i<strings.size();i++) {
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
    public void parserGreaterThanEqualsToExpression(Expression expression,List<String> strings) {
        Expression rightExpression = ((GreaterThanEquals) expression).getRightExpression();
        if (rightExpression instanceof LongValue) {
            Long l = ((LongValue) rightExpression).getValue();
            for (int i=0;i<strings.size();i++){
                Long l1 = NumberSource.getInstance().randomLong(l, l + strings.size() * 10);
                strings.set(i,l1.toString());
            }
        } else if (rightExpression instanceof DoubleValue) {
            Double value = ((DoubleValue) rightExpression).getValue();
            for (int i=0;i<strings.size();i++){
                Double l1 = NumberSource.getInstance().randomDouble(value, value + strings.size() * 10);
                strings.set(i,l1.toString());
            }
        } else if (IsDateFormat.isRqFormat(rightExpression.toString())) {
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
            ZoneId zoneId = ZoneId.systemDefault();
            try {
                LocalDate localDate = yyyyMMdd.parse(rightExpression.toString()).toInstant().atZone(zoneId).toLocalDate();
                for (int i=0;i<strings.size();i++) {
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
    public void parserMinorThanToExpression(Expression expression,List<String> strings) {
        Expression rightExpression = ((MinorThan) expression).getRightExpression();
        if (rightExpression instanceof LongValue) {
            Long l = ((LongValue) rightExpression).getValue();
            if ((l-strings.size() * 10)<0){
                for (int i=0;i<strings.size();i++){
                    Long l1 = NumberSource.getInstance().randomLong(0, l - 1);
                    strings.set(i,l1.toString());
                }
            } else {
                for (int i=0;i<strings.size();i++){
                    Long l1 = NumberSource.getInstance().randomLong(l - strings.size() * 10, l - 1);
                    strings.set(i,l1.toString());
                }
            }
        } else if (rightExpression instanceof DoubleValue) {
            Double l = ((DoubleValue) rightExpression).getValue();
            if ((l-strings.size() * 10)<0){
                for (int i=0;i<strings.size();i++){
                    Double l1 = NumberSource.getInstance().randomDouble(0, l - 1);
                    strings.set(i,l1.toString());
                }
            } else {
                for (int i=0;i<strings.size();i++){
                    Double l1 = NumberSource.getInstance().randomDouble(l - strings.size() * 10, l - 1);
                    strings.set(i,l1.toString());
                }
            }
        } else if (IsDateFormat.isRqFormat(rightExpression.toString())) {
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
            ZoneId zoneId = ZoneId.systemDefault();
            try {
                LocalDate localDate = yyyyMMdd.parse(rightExpression.toString()).toInstant().atZone(zoneId).toLocalDate();
                for (int i=0;i<strings.size();i++) {
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
    public void parserMinorThanEqualsToExpression(Expression expression,List<String> strings) {
        Expression rightExpression = ((MinorThanEquals) expression).getRightExpression();
        if (rightExpression instanceof LongValue) {
            Long l = ((LongValue) rightExpression).getValue();
            if ((l-strings.size() * 10)<0){
                for (int i=0;i<strings.size();i++){
                    Long l1 = NumberSource.getInstance().randomLong(0, l);
                    strings.set(i,l1.toString());
                }
            } else {
                for (int i=0;i<strings.size();i++){
                    Long l1 = NumberSource.getInstance().randomLong(l - strings.size() * 10, l);
                    strings.set(i,l1.toString());
                }
            }
        } else if (rightExpression instanceof DoubleValue) {
            Double l = ((DoubleValue) rightExpression).getValue();
            if ((l-strings.size() * 10)<0){
                for (int i=0;i<strings.size();i++){
                    Double l1 = NumberSource.getInstance().randomDouble(0, l);
                    strings.set(i,l1.toString());
                }
            } else {
                for (int i=0;i<strings.size();i++){
                    Double l1 = NumberSource.getInstance().randomDouble(l - strings.size() * 10, l);
                    strings.set(i,l1.toString());
                }
            }
        } else if (IsDateFormat.isRqFormat(rightExpression.toString())) {
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
            ZoneId zoneId = ZoneId.systemDefault();
            try {
                LocalDate localDate = yyyyMMdd.parse(rightExpression.toString()).toInstant().atZone(zoneId).toLocalDate();
                for (int i=0;i<strings.size();i++) {
                    String yyyyMMdd1 = DateTimeSource.getInstance().randomPastDate(localDate, "yyyyMMdd");
                    strings.set(i,yyyyMMdd1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


}
