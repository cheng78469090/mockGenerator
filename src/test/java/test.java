import com.yoyosys.mock.OutPutFile;
import com.yoyosys.mock.pojo.Column;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.collections4.map.LinkedMap;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @Author: yjj
 * Date: 2021/9/5
 */
public class test {

    @Test
    public void test1() throws JSQLParserException {
        Select stmt = (Select) CCJSqlParserUtil.parse("SELECT col1 AS a, col2 AS b, col3 AS c FROM table WHERE col1 = 10 AND col2 = 20 AND col3 = 30");

        Map<String, Expression> map = new HashMap<>();
        for (SelectItem selectItem : ((PlainSelect)stmt.getSelectBody()).getSelectItems()) {
            selectItem.accept(new SelectItemVisitorAdapter() {
                @Override
                public void visit(SelectExpressionItem item) {
                    map.put(item.getAlias().getName(), item.getExpression());
                }
            });
        }

        System.out.println("map " + map);

    }

    @Test
    public void test2(){
        //字段类集合
        List<Column> columnList = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        try {
//          BufferedReader bfr = new BufferedReader(new FileReader(new File(filePath)));
            BufferedReader bfr = new BufferedReader(
                new InputStreamReader(new FileInputStream(
                    new File(
                    "C:\\Users\\xiaoyaoxiaodi\\Desktop\\work\\lq\\template\\pdata_t03_agmt_fea_rela_h\\ext_pdata_t03_agmt_fea_rela_h.tpl")), "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = bfr.readLine()) != null) {
                result.append(lineTxt).append("\n");
            }
            bfr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
        System.out.println("*************************************************************************************");
        String[] createSql = result.toString().split("\n");
        int startAndEnd[] = new int[2];
        int index = 0;
        for (int i = 0; i < createSql.length; i++) {
            if (createSql[i].substring(0,1).equals("(") || createSql[i].substring(1,2).equals(")")){
                startAndEnd[index] = i;
                index++;
                System.out.println(createSql[i]);
            }
        }
        System.out.println("*************************************************************************************");

        String[] cTypes = {"a",""};
        for(int i = startAndEnd[0]+1; i < startAndEnd[1]; i++){
            Column column = new Column();
            String[] sqlStructure = createSql[i].split(" ");
            column.setFieldName(sqlStructure[0]);  //获取字段名
            int n = sqlStructure[1].indexOf("(");
            int m = sqlStructure[1].indexOf(")");
            String cType = "";   //字段类型
            String cLength = ""; //字段长度
            if (n == -1){
                cType = sqlStructure[1];
            }else{
                cType = sqlStructure[1].substring(0,n);
                cLength = sqlStructure[1].substring(n+1,m);
            }
            column.setcType(cType);
            column.setcLength(cLength);
            columnList.add(column);

            System.out.println(column.getcType());
            System.out.println(column.getcLength());
            System.out.println(createSql[i]);
        }

        System.out.println(columnList);//表结构
//        System.out.println(startAndEnd[1]);

        System.out.println("*************************************************************************************");


        StringBuilder result1 = new StringBuilder();
        try {
//          BufferedReader bfr = new BufferedReader(new FileReader(new File(filePath)));
            BufferedReader bfr1 = new BufferedReader(
                new InputStreamReader(new FileInputStream(new File(
            "C:\\Users\\xiaoyaoxiaodi\\Desktop\\work\\lq\\data\\a_pdata_t03_agmt_fea_rela_h_20210721_000_000.dat")), "UTF-8"));
            String lineTxt1 = null;
            while ((lineTxt1 = bfr1.readLine()) != null) {
                result1.append(lineTxt1).append("\n");
            }
            bfr1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result1);
        System.out.println("*************************************************************************************");
        String[] createSql1 = result1.toString().split("\n");
        List<String[]> listb = new ArrayList<>();
        for (String a: createSql1) {
            String[] b = a.split("\\|\\+\\|");
            listb.add(b);
        }


        Map<Column, List> resultMap = new LinkedHashMap<>();
        for (int j = 0; j < columnList.size(); j++) {
            List list = new ArrayList();
            for (String[] arr: listb) {
                list.add(arr[j]);
            }
            resultMap.put(columnList.get(j),list);
        }

        for (Map.Entry<Column, List> entry : resultMap.entrySet()) {
            System.out.println("key=" + entry.getKey() + ", value=" + entry.getValue());
        }

    }




    @Test
    public void fileExist(){
        File file = new File("D:\\新建文件夹\\YoudaoNote\\cef.pak");
        if(!file.exists())
        {

        }
    }

    @Test
    public void getData(){
        StringBuilder result1 = new StringBuilder();
        try {
//          BufferedReader bfr = new BufferedReader(new FileReader(new File(filePath)));
            BufferedReader bfr1 = new BufferedReader(
                new InputStreamReader(new FileInputStream(
                    new File(
                    "C:\\Users\\xiaoyaoxiaodi\\Desktop\\work\\lq\\data\\a_pdata_t03_agmt_fea_rela_h_20210721_000_000.dat")), "UTF-8"));
            String lineTxt1 = null;
            while ((lineTxt1 = bfr1.readLine()) != null) {
                result1.append(lineTxt1).append("\n");
            }
            bfr1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result1);
        System.out.println("*************************************************************************************");
        String[] createSql1 = result1.toString().split("\n");
        for (String a: createSql1) {
            String[] b = a.split("\\|\\+\\|");
            System.out.println(b.toString());
        }
    }


    @Test
    public void createData() {
        // B1、B2通过模板可以知道分区字段是那个，
        // B3没有分区字段，
        // B4 start_dt、end_dt是日期，这些不管之前是什么类型，都定义日期类型，其他的不用管
    }

     @Test
          public void test10(){
         Map<Integer, List<Object>> recordList=new LinkedHashMap<Integer, List<Object>>();
         List<Object> list = new ArrayList<>();
         List<Object> list1 = new ArrayList<>();
         List<Object> list2 = new ArrayList<>();
         list.add("a");
         list.add("b");
         list.add("c");
         list.add("d");
         list1.add("e");
         list1.add("f");
         list1.add("g");
         list1.add("h");
         list2.add("i");
         list2.add("g");
         list2.add(null);
         list2.add("");
         recordList.put(1,list);
         recordList.put(2,list1);
         recordList.put(3,list2);
         System.out.println(recordList);
         String file="C:\\Users\\wjp50\\Desktop\\新建文件夹\\abc.dat";
         //OutPutFile.generateDatFile(file,recordList);

        }
        @Test
    public void pinjie(){

            LinkedHashMap<String, List<String>> lhp = new LinkedHashMap<>();
            List<String> list_a  = Arrays.asList("1", "2", "3","4");
            List<String> list_b  = Arrays.asList("a", "b", "c","d");
            List<String> list_c  = Arrays.asList("q", "w", "e","r");
            lhp.put("columns_a",list_a);
            lhp.put("columns_b",list_b);
            lhp.put("columns_c",list_c);

            Collection<List<String>> values1 = lhp.values();
            ArrayList<List<String>> values = new ArrayList<>();
            for (List<String> strings : values1) {
                values.add(strings);
            }
            ArrayList<List<String >> raws = new ArrayList<>();

            for (int i = 0; i < values.get(1).size(); i++) {
                ArrayList<String> raw = new ArrayList<>();
                for (int j = 0; j < values.size() ; j++) {
                    raw.add(values.get(j).get(i));
                }
                raws.add(raw);
            }
            raws.forEach(a-> System.out.println(a));


        }
        }























































