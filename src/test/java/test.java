import br.com.six2six.bfgex.RegexGen;
import com.apifan.common.random.source.DateTimeSource;
import com.mifmif.common.regex.Generex;
import com.yoyosys.mock.util.JsqlparserUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.*;
import nl.flotsam.xeger.Xeger;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.net.URL;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * @Author: yjj
 * Date: 2021/9/5
 */
public class test {
    private Map<String,List<String>> map;

    @Test
    public void test1() throws JSQLParserException {
//        List<Expression> sqlParser = JsqlparserUtil.getSQLParser("select * from tt where ss between 1 and 2 or ss in (1,2,3,4,5) and xx > 2 and b=5 and xx<=5 and cc like 'mei*'");
        List<Expression> sqlParser = JsqlparserUtil.getSQLParser("select * from tt where ss between '20200801' and '20200809' and ss in (1.1,2.2,3,4,5) and ss not in (1.1,2.2,3,4,5) and rr not in (1,2,3,4,5) and xx > 2 and xx >=2 and xx < 3 and xx <=3 and a!=5 and b=5 and cc like 'mei*' and cc not like 'mei*'");
//        List<Expression> sqlParser = JsqlparserUtil.getSQLParser("select * from t1 where b >= 2 and b < 8 and c > 1 and d != 4 and e != 'a'");
//        List<Expression> sqlParser = JsqlparserUtil.getSQLParser("select * from tt where b is null and c is not null");

        for (Expression expression : sqlParser) {
            System.out.println(expression.toString());
        }

    }

    @Test
    public void test2() throws ParseException {
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate start = yyyyMMdd.parse("20200801").toInstant().atZone(zoneId).toLocalDate();
        LocalDate end = yyyyMMdd.parse("20200901").toInstant().atZone(zoneId).toLocalDate();
        System.out.println(DateTimeSource.getInstance().randomDate(start,end,"yyyyMMdd"));
    }

    @Test
    public void test3() throws ParseException {
        String regex = ".*1.*";
        Xeger generator = new Xeger(regex);
        String result = generator.generate();
        System.out.println(result);
    }

    @Test
    public void test4() throws ParseException {
        System.out.println(RegexGen.of("[a-z0-9]{0,10}00"));
    }

    @Test
    public void test6() throws ParseException {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (j==2) {
                    break;
                }
                System.out.print("a");
            }
            System.out.println("b");
        }
    }

    @Test
    public void test5() throws ParseException {
//        Generex generex = new Generex("[a-z0-9]{10,10}");
//        Generex generex = new Generex("[0-9]{" + "12341234".toString().length() + "}");
        Generex generex =new Generex("[0-9]{5}\\.[0-9]{5}");
        // Generate random String
        String randomStr = generex.random();
        System.out.println(randomStr);// a random value from the previous String list

        // generate the second String in lexicographical order that match the given Regex.
//        String secondString = generex.getMatchedString(4);
//        System.out.println(secondString);// it print '0b'
//
//        // Generate all String that matches the given Regex.
//        List<String> matchedStrs = generex.getAllMatchedStrings();
//
//        // Using Generex iterator
//        com.mifmif.common.regex.util.Iterator iterator = generex.iterator();
//        while (iterator.hasNext()) {
//            System.out.print(iterator.next() + " ");
//        }
        // it prints:
        // 0a 0b 0c 0e 0ee 0ef 0eg 0f 0fe 0ff 0fg 0g 0ge 0gf 0gg
        // 1a 1b 1c 1e 1ee 1ef 1eg 1f 1fe 1ff 1fg 1g 1ge 1gf 1gg
        // 2a 2b 2c 2e 2ee 2ef 2eg 2f 2fe 2ff 2fg 2g 2ge 2gf 2gg
        // 3a 3b 3c 3e 3ee 3ef 3eg 3f 3fe 3ff 3fg 3g 3ge 3gf 3gg
    }
    @Test
    public void test8(){
        URL resource = this.getClass().getClassLoader().getResource("D:\\work_space\\mock_data\\data\\loadone_pdata_t03_agmt_fea_rela_h.tpl");
        System.out.println(resource.toString());
    }
}
