package core.sql;

import core.util.Ins;
import core.util.StringUtil;
import org.nutz.dao.sql.Sql;

import java.util.LinkedList;
import java.util.List;


public class SqlHelper {

    public static List<String> getTableStruct(String tableName, String dbName) {
        Sql sql = Ins.sql().sqls().create("select_table_struct.data");
        sql.setParam("tableName", tableName).setParam("dbName", dbName);
        sql.setCallback((conn, rs, sql1) -> {
            List<String> name = new LinkedList<>();
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                name.add(StringUtil.underlineToCamel(columnName));
            }
            return name;
        });
        return Ins.sql().execute(sql).getList(String.class);
    }

    public static int getTableIncrement(String tableName, String dbName) {
        Sql sql = Ins.sql().sqls().create("table_increment.data");
        sql.setParam("tableName", tableName).setParam("dbName", dbName);
        sql.setCallback((conn, rs, sql1) -> {
            int increment = -1;
            if (rs.next()) {
                String incrementStr = rs.getString("AUTO_INCREMENT");
                if (incrementStr != null) {
                    increment = Integer.valueOf(incrementStr);
                }
            }
            return increment;
        });
        return Ins.sql().execute(sql).getInt();
    }

    public static List<SqlTableInfo> getAllTableInfo(String dbName) {
        Sql sql = Ins.sql().sqls().create("table_info.data");
        sql.setParam("dbName", dbName);
        sql.setCallback((conn, rs, sql1) -> {
            List<SqlTableInfo> list = new LinkedList<>();
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                SqlTableInfo tableInfo = new SqlTableInfo();
                tableInfo.setTableName(tableName);
                list.add(tableInfo);
            }
            return list;
        });
        return Ins.sql().execute(sql).getList(SqlTableInfo.class);
    }
}
