package com.yoyosys.mock;

import com.yoyosys.mock.common.GlobalConstants;
import com.yoyosys.mock.pojo.Column;
import com.yoyosys.mock.util.ShellUntil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.color.ColorSpace;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;


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

     private static final Logger logger = LoggerFactory.getLogger(OutPutFile.class);

    /**
     * 生成dat数据文件
     *
     * @param fileName
     * @param recordList
     */
    public static void generateDatFile(String fileName, String charsetName, Map<Column, List> recordList) {

        File file = new File(fileName);
        String line = System.getProperty("line.separator");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charsetName));

            Collection<List> value = recordList.values();
            ArrayList<List> values = new ArrayList<>();

            for (List s : value) {
                values.add(s);
            }
            String[] arr = new String[values.get(1).size()];
            for (int j = 0; j < values.get(1).size(); j++) {
                for (int i = 0; i < values.size(); i++) {
                    if (arr[j] == null) {
                        arr[j] = "";
                    }
                    arr[j] += values.get(i).get(j) + "|+|";
                }
            }
            for (Object a : arr
            ) {
                /*if (file.length()>=1024*1024*2){
                    bw1.write(a+line);
                }*/
                bw.write((String) a + line);
            }
            bw.close();
            logger.info(GlobalConstants.LOG_PREFIX+file.getName()+"文件生成成功");
        } catch (Exception e) {
            logger.error(GlobalConstants.LOG_PREFIX +file.getName()+"文件生成失败"+e);
            file.delete();
            return;
        }
    }

    /**
     * 压缩输出文件：i_pdata_t03_agmt_fea_rela_h_20210709_000_000.dat.Z
     *
     * @param srcPath
     * @param outPath
     * @throws IOException
     *//*

    public static void compressFile(String srcPath, String fileFormat,String outPath) throws IOException {
        File srcFile = new File(srcPath);
        outPath += File.separator + srcFile.getName().split("\\.")[0] +fileFormat+ ".Z";

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
    }*/

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
    public static void update(int ID) throws SQLException {
        //连接对象
        Connection conn = MockData.getConnection(new MockData().getDataSourceConfig());
        PreparedStatement ps = null;
        String sql = "update DS_DLP_MOCKDATA_CONFIG set STATE = 1 where ID=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, ID);
            ps.executeUpdate();
            logger.info(GlobalConstants.LOG_PREFIX+"数据库更新成功"+"ID为"+ID);
        } catch (SQLException e) {
            logger.error(GlobalConstants.LOG_PREFIX +"数据库更新失败"+"ID为"+ID+e);
        } finally {
            ps.close();
            conn.close();
        }
    }

    /**
     * createXMl
     */
    public static void createXml(String fileName, Long fileSize, String charsetName, String readyFileFormat, String readyFileName)  {
        File file = new File(readyFileName);
        if (readyFileFormat.equals(".ok")) {
            try {
                file.createNewFile();
                logger.info(GlobalConstants.LOG_PREFIX+file.getName()+"就绪文件生成成功");
            } catch (IOException e) {
                logger.error(GlobalConstants.LOG_PREFIX+file.getName()+"就绪文件生成失败"+e);
            }
        } else {
            // 创建XML文档树
            Document document = DocumentHelper.createDocument();
            // 创建根节点transmit-content
            Element itemsElement = document.addElement("transmit-content");
            // 创建根节点下的file子节点
            Element itemElement = itemsElement.addElement("file");
            // file节点有两个子节点
            Element idElement = itemElement.addElement("filename");
            idElement.setText(fileName);
            Element nameElement = itemElement.addElement("filesize");
            nameElement.setText(String.valueOf(fileSize));

            // 设置XML文档格式
            OutputFormat outputFormat = OutputFormat.createPrettyPrint();
            // 设置XML编码方式,即是用指定的编码方式保存XML文档到字符串(String),这里也可以指定为GBK或是ISO8859-1
            outputFormat.setEncoding(charsetName);
            outputFormat.setNewLineAfterDeclaration(false);
            outputFormat.setIndent(true); //设置是否缩进
            outputFormat.setIndent("    "); //以四个空格方式实现缩进
            outputFormat.setNewlines(true); //设置是否换行

            if (file.exists()) {
                file.delete();
            }
            try {
                // xmlWriter是用来把XML文档写入字符串的(工具)
                XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(file), outputFormat);
                //设置不对特殊字符进行转义
                xmlWriter.setEscapeText(false);
                // 把创建好的XML文档写入字符串
                xmlWriter.write(document);
                xmlWriter.close();


                BufferedReader br = null;
                PrintWriter pw = null;
                StringBuffer buff = new StringBuffer();
                String line = System.getProperty("line.separator");//平台换行!
                String str;

                br = new BufferedReader(new FileReader(readyFileName));
                while ((str = br.readLine()) != null) {
                    if (str.equals("<transmit-content>"))
                        str = str.replaceAll("<transmit-content>", "    <transmit-content>\n");
                    buff.append(str + line);
                }
                pw = new PrintWriter(new FileWriter(readyFileName), true);
                pw.println(buff);
                if (br != null)
                        br.close();
                if (pw != null)
                    pw.close();
                logger.info(GlobalConstants.LOG_PREFIX+file.getName()+"文件生成成功");
            } catch (IOException e) {
                logger.error(GlobalConstants.LOG_PREFIX +file.getName()+"文件生成失败"+e);
            }

        }
    }

    /**
     * 生成压缩文件
     * @param FileName
     * @throws Exception
     */
    public static void compressFile(String FileName,String compressFile){
        String shellCmd = "compress " + FileName;
        //String shellCmd = "pwd";
        //调用shell命名生成.Z文件
        String s = ShellUntil.execShell(shellCmd);
        logger.info(GlobalConstants.LOG_PREFIX+compressFile+"文件生成成功"+s);
    }
}

