package com.yoyosys.mock;

import com.yoyosys.mock.pojo.Column;
import com.yoyosys.mock.pojo.DataSourceConfig;
import com.yoyosys.mock.pojo.DsConfig;
import com.yoyosys.mock.pojo.DsDlpMockdataConfig;
import com.yoyosys.mock.util.JsqlparserUtil;
import com.yoyosys.mock.util.ModifyDataUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: yjj
 * Date: 2021/9/3
 */
public class MockData {


    public static void main(String[] args) {
        MockData mockData = new MockData();
        //②　	读取模拟数据配置文件（dlp_yoyo_mockdata.config）：数据库连接信息
        DataSourceConfig dataSourceConfig = mockData.getDataSourceConfig();
        //读取配置表中的配置信息：查询配置表中与操作人匹配且状态为‘0’（未执行）的数据行存放到配置类中
        List<DsDlpMockdataConfig> dsDlpMockDataConfigs = mockData.getDsDlpMockDataConfig(dataSourceConfig.getOperator());

        ModifyDataUtil modifyDataUtil = new ModifyDataUtil();
        for (DsDlpMockdataConfig dsDlpMockDataConfig : dsDlpMockDataConfigs) {
            //读取表结构：获取配置类中的表名，根据表名去DS_CONFIG中查找数据加载场景(LOAD_SCENE)
            DsConfig dsConfig = mockData.getDsConfig(dsDlpMockDataConfig.getHive_name());

            //todo: 王震宣  解析模板文件
            String modeFile = "";
            //根据数据加载场景去对应的目录下查找该表对应的以ext开头的模板文件
            switch (dsConfig.getLoadScene()) {
                case "b1":
                    modeFile="b1";
                    break;
                case "b2":
                    modeFile="b2";
                    break;
                case "b3":
                    modeFile="b3";
                    break;
                case "b4":
                    modeFile="b4";
                    break;
            }
            List<Column> columns = mockData.getColumn();
            LinkedHashMap<Column,List<String>> recordList = null;
            //是否上传数据文件：读取数据文件/mockdata/data/a_pdata_t03_agmt_fea_rela_h_20210709_000_000.dat
            if (getDataFile()){
                //生成模拟数据集


            } else {
                //没有数据文件,获取表主键


                //生成模拟数据集


            }

            /*
            * 根据条件修改数据
            * 1.解析sql
            * 2.修改数据
            * todo:易建军、王燚
            *  return : list<map>
            * */

            try {
                List<Expression> sqlParser = JsqlparserUtil.getSQLParser(dsDlpMockDataConfig.getConditions());
                LinkedHashMap<Column, List<String>> columnListMap = modifyDataUtil.modifyData(recordList, columns, sqlParser);
            } catch (JSQLParserException e) {
                e.printStackTrace();
            }

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

    private static boolean getDataFile() {
        return true;
    }

    private List<Column> getColumn() {
        return null;
    }

    /**
     *  获取where条件
     * @param sql
     * @return
     * @throws JSQLParserException
     */
    public List<Expression> getSQLParser(String sql) throws JSQLParserException {
        List<Expression> sqlParser = JsqlparserUtil.getSQLParser(sql);
        return sqlParser;
    }

    /**
     * 修改数据
     * @param conditions
     * @param data
     * @return
     */
    public List<List<String>> modifyData(List<Expression> conditions,List<List<String>> data){
        return null;
    }

    /**
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
