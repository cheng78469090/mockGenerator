

import org.testng.annotations.Test;

import java.io.File;


public class test {
    @Test
    public void test() {
        //String FileName = "D:\\work_space\\work_data\\result\\i_pdata_t03_agmt_fea_rela_h_20211013_000_001.dat";
        String FileName = "i_pdata_t03_agmt_fea_rela_h_20211013_000_001.dat";
        String str = FileName.split("\\.")[0];
        String s = str.substring(str.length() - 1);
        System.out.println(s);
        //System.out.println(Arrays.toString(str));
    }
}
