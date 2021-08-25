package com.yoyosys.mock;

import com.apifan.common.random.source.InternetSource;
import com.apifan.common.random.source.NumberSource;
import com.apifan.common.random.source.PersonInfoSource;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 *@Author: yjj
 *Date: 2021/8/24
 */
public class ComprehensiveGenerator {

    private static String Delimiter = "|+|";
    private static Long Number_Of_Generations=10L;
    private static String File_Name= "a_pdata_t03_agmt_fea_rela_h_20210709_000_000.dat";

    public static void main(String[] args) throws IOException {
        File file = new File(File_Name);
        ComprehensiveGenerator comprehensiveGenerator = new ComprehensiveGenerator();
        for (int i = 0; i < Number_Of_Generations; i++) {
            Files.append(comprehensiveGenerator.formatContent(
                    Arrays.asList(NumberSource.getInstance().randomLong(10000000000L, 20000000001L),
                    InternetSource.getInstance().randomAppVersionCode(),
                    NumberSource.getInstance().randomDouble(10000000D, 20000000D),
                    PersonInfoSource.getInstance().randomChineseMobile())
                    )
                    ,file
                    , Charset.forName("utf-8"));
        }
    }

    private String formatContent(List list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object o : list) {
            stringBuilder.append(o.toString()).append(Delimiter);
        }
        return stringBuilder.append("\n").toString();
    }
}
