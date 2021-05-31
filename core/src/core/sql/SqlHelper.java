package core.sql;

import core.util.Instance;
import core.util.StringUtil;
import org.nutz.dao.sql.Sql;

import java.util.LinkedList;
import java.util.List;

public class SqlHelper {

    public static List<String> getTableStruct(String tableName, String dbName) {
        Sql sql = Instance.sql().sqls().create("select_table_struct.data");
        sql.setParam("tableName", tableName).setParam("dbName", dbName);
        sql.setCallback((conn, rs, sql1) -> {
            List<String> name = new LinkedList<>();
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                name.add(StringUtil.underlineToCamel(columnName));
            }
            return name;
        });
        List<String> value = Instance.sql().execute(sql).getList(String.class);
        return value;
    }
}
