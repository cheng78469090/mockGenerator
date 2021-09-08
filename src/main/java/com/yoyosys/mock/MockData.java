package com.yoyosys.mock;

import com.yoyosys.mock.pojo.Column;
import com.yoyosys.mock.pojo.DataSourceConfig;
import com.yoyosys.mock.pojo.DsConfig;
import com.yoyosys.mock.pojo.DsDlpMockdataConfig;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: wzx
 * Date: 2021/9/3
 */
public class MockData {


    public static void main(String[] args)  throws Exception {
        MockData mockData = new MockData();
        //②　	读取模拟数据配置文件（dlp_yoyo_mockdata.config）：数据库连接信息
        DataSourceConfig dataSourceConfig = mockData.getDataSourceConfig();
        //读取配置表中的配置信息：查询配置表中与操作人匹配且状态为‘0’（未执行）的数据行存放到配置类中
        List<DsDlpMockdataConfig> dsDlpMockDataConfigs = mockData.getDsDlpMockDataConfig(dataSourceConfig.getOperator());

        for (DsDlpMockdataConfig dsDlpMockDataConfig : dsDlpMockDataConfigs) {
            //表名
            String hiveName = dsDlpMockDataConfig.getHive_name();
            //读取表结构：获取配置类中的表名，根据表名去DS_CONFIG中查找数据加载场景(LOAD_SCENE)
            DsConfig dsConfig = mockData.getDsConfig(hiveName);


            //todo: 王震宣  解析模板文件
            //表结构
            List<Column> columnList = mockData.getColumn();
            //模拟数据集
            Map<Column, List> resultMap = new LinkedHashMap<>();

            StringBuilder modeFile = new StringBuilder("/user/bdap/bdap-dataload/template");//增量文件，文件路径
            StringBuilder modeFile1 = new StringBuilder("/user/bdap/bdap-dataload/template");//存量文件，用来确定分区字段
            //根据数据加载场景去对应的目录下查找该表对应的以ext开头的模板文件
            switch (dsConfig.getLoadScene().substring(0,1)) {//数据加载场景
                case "B1":
                    modeFile.append("/B 1/" + hiveName + "/" + "ext_" + hiveName + ".tpl");
                    modeFile1.append("/B1_C/" + hiveName + "/" + "load_" + hiveName + ".tpl");
                    break;
                case "B2":
                    modeFile.append("/B2/" + hiveName + "/" + "ext_" + hiveName + ".tpl");
                    modeFile1.append("/B2_C/" + hiveName + "/" + "load_" + hiveName + ".tpl");
                    break;
                case "B3":
                    modeFile.append("/B3/" + hiveName + "/" + "ext_" + hiveName + ".tpl");
                    break;
                case "B4":
                    modeFile.append("/B4/" + hiveName + "/" + "ext_" + hiveName + ".tpl");
                    break;
            }


            //是否上传数据文件：读取数据文件/mockdata/data/a_pdata_t03_agmt_fea_rela_h_20210709_000_000.dat
            if (getDataFile()){
                //生成模拟数据集
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

                String[] createSql1 = result1.toString().split("\n");
                List<String[]> listb = new ArrayList<>();
                for (String a: createSql1) {
                    String[] b = a.split("\\|\\+\\|");
                    listb.add(b);
                }

                for (int j = 0; j < columnList.size(); j++) {
                    List list = new ArrayList();
                    for (String[] arr: listb) {
                        list.add(arr[j]);
                    }
                    resultMap.put(columnList.get(j),list);
                }

            } else {
                //没有数据文件,获取表主键
                List<String> primaryKeyList = mockData.getPrimayKey();

                //生成模拟数据集


            }

            /*
            * 根据条件修改数据
            * 1.解析where条件
            * 2.修改数据
            * todo:易建军、王燚
            *  return : list<map>
            * */

            /*
            * 输出
              创建输出路径：/result
              创建输出文件：i_pdata_t03_agmt_fea_rela_h_20210709_000_000.dat
              压缩输出文件：i_pdata_t03_agmt_fea_rela_h_20210709_000_000.dat.Z
              创建加载完成的就绪文件：i_pdata_t03_agmt_fea_rela_h_20210709_000_000.xml
              删除输出的dat文件
              修改状态：修改配置类中状态属性为‘1’（成功），写回到对应配置表中。
            *
            * todo:王锦鹏
            * */

        }

    }


    //判断是否上传数据文件
    private static boolean getDataFile() {
        File file = new File("D:\\新建文件夹\\YoudaoNote\\cef.pak");
        if(!file.exists())
        {
            return false;
        }
        return true;
    }


    //获取表结构
    private List<Column> getColumn() {
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
        }

        return columnList;
    }

    /**
     * 将文件中获取到的主键放入list
     * @return
     * @throws Exception
     */
    private  List<String> getPrimayKey() throws Exception {
        String path = this.getClass().getClassLoader().getResource("loadone_pdata_t03_agmt_fea_rela_h.tpl").getFile();
        FileReader fileReader = new FileReader(path);
        BufferedReader bfReader = new BufferedReader(fileReader);
        String temp = null;
        StringBuffer sb = new StringBuffer();
        int startWtih = 0;
        int endWith = 0;
        int line = 1;
        while ((temp = bfReader.readLine()) != null) {
            if (temp != null && temp.length() > 0) {
                sb.append(temp);
            }
            if (sb.toString().contains("on")) {
                startWtih = sb.indexOf("on");
            }
            if(sb.toString().contains("where")){
                endWith = sb.indexOf("where", startWtih);
            }
        }
        String subStr = sb.toString().substring(startWtih, endWith);
        List<String> listStr = new ArrayList<String>();
        String[] arrStr = subStr.split(" ");
        for (int i = 0; i < arrStr.length; i++) {
            if (arrStr[i].contains("<=>")) {
                String str = arrStr[i].substring(arrStr[i].indexOf(".") + 1, arrStr[i].indexOf("<"));
                listStr.add(str);
            }
        }
        bfReader.close();
        fileReader.close();
        return listStr;
    }

    /*
      *todo:宋金城
      *输入：
      *return: DataSourceConfig
     */
    public DataSourceConfig getDataSourceConfig() {
         return null;
    }

    /**
     * todo: 宋金城
     * param: operator
     * return: DsDlpMockDataConfig
     */
    public List<DsDlpMockdataConfig> getDsDlpMockDataConfig(String operator) {
        return null;
    }

    /**
     * todo: 宋金城
     * param:
     * return: DsConfig
     */
    public DsConfig getDsConfig(String table_name) {
        return null;
    }
}
