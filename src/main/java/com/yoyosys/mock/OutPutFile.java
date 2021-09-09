package com.yoyosys.mock;

import com.yoyosys.mock.pojo.Column;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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


public class OutPutFile {


    /**
     * 生成dat数据文件
     *  @param fileName
     * @param recordList*/
    public static void generateDatFile(String fileName, Map<Column, List> recordList) {
        //创建输出文件：i_pdata_t03_agmt_fea_rela_h_20210709_000_000.dat
       /* String filePath = PathHelper.getRootPath()+"\\result";
        String start_date = new DsDlpMockDataConfig().getStart_date();
        String hive_name = new DsDlpMockDataConfig().getHive_name();
        String fileName=filePath+"/"+"i_pdata"+hive_name+"_"+start_date+"000_000.dat";*/

        File file = new File(fileName);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            Collection<List> value = recordList.values();
            ArrayList<List> values = new ArrayList<>();

            for (List s : value) {
                values.add(s);
            }
            String[] arr = new String[values.get(1).size()];
            System.out.println(arr.length);
            for (int j = 0; j < values.get(1).size(); j++) {
                for (int i = 0; i < values.size(); i++) {
                    if (arr[j] == null) {
                        arr[j] = "";
                    }
                    arr[j] += values.get(i).get(j) + "|+|";
                }
            }
            for (Object a :arr
            ) {
               bw.write((String) a+"\n");
            }
            bw.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩输出文件：i_pdata_t03_agmt_fea_rela_h_20210709_000_000.dat.Z
     *
     * @param srcPath
     * @param outPath
     * @throws IOException
     */

    public static void compressFile(String srcPath, String outPath) throws IOException {
        File srcFile = new File(srcPath);
        outPath += File.separator + srcFile.getName().split("\\.")[0] + ".Z";

        FileOutputStream fileOutputStream = new FileOutputStream(outPath);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

        compressFile(srcFile, srcFile.getName(), zipOutputStream);
        zipOutputStream.close();
        fileOutputStream.close();

    }

    private static void compressFile(File file, String fileName, final ZipOutputStream outputStream) throws IOException {

        outputStream.putNextEntry(new ZipEntry(fileName));
        //读取文件并写出
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        byte[] bytes = new byte[1024];
        int n;
        while ((n = bufferedInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, n);
        }
        //关闭流
        fileInputStream.close();
        bufferedInputStream.close();
    }

    /**
     * 生成就绪文件
     *
     * @param fileName
     * @param outPath
     */
    public static void generateReadyFile(String fileName, String outPath) {
        File file1 = new File(fileName);
        String readyFileName = outPath + File.separator + file1.getName().split("\\.")[0] + ".xml";

        try {
            // 创建解析器工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document document = db.newDocument();
            // 不显示standalone="no"
            document.setXmlStandalone(true);
            Element transmit = document.createElement("transmit-content");
            // 向transmit-content根节点中添加子节点file
            Element file = document.createElement("file");
            Element filename = document.createElement("filename");
            //设置filename的内容
            filename.setTextContent(file1.getName());
            //将filenanme加入到file的子节点中
            file.appendChild(filename);
            // 将file节点添加到transmit-content根节点中
            transmit.appendChild(file);
            // 将transmit-content节点（已包含file）添加到dom树中
            document.appendChild(transmit);
            // 创建TransformerFactory对象
            TransformerFactory tff = TransformerFactory.newInstance();
            // 创建 Transformer对象
            Transformer tf = tff.newTransformer();
            // 输出内容是否使用换行
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            // 创建xml文件并写入内容
            tf.transform(new DOMSource(document), new StreamResult(new File(readyFileName)));
            System.out.println("就绪文件生成成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("就绪文件生成失败");
        }
    }

    /**
     * 实现删除文件
     *
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String fileName) {
        boolean flag = false;
        File file = new File(fileName);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 实现更新操作
     *
     * @throws SQLException
     */
    public static void update() throws SQLException {
        //连接对象
//        Connection conn = MockData.getConnection(new OutPutFile().getDataSourceConfig());
//        PreparedStatement ps = null;
//        String OPERATOR = new DsDlpMockDataConfig().getOperator();
//        String HIVE_NAME = new DsDlpMockDataConfig().getHive_name();
//        String sql = "update DS_DLP_MOCKDATA_CONFIG set STATE = 1 where STATE = 0 and OPERATOR= ? and HIVE_NAME=?";
//        try {
//            ps = conn.prepareStatement(sql);
//            ps.executeUpdate();
//            System.out.println("数据更新成功");
//             ps.setString(1,OPERATOR);
//             ps.setString(2,HIVE_NAME);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("更新失败");
//        } finally {
//            ps.close();
//            conn.close();
//        }
    }


}

