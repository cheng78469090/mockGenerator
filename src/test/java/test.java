import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.*;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

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
}
