package core.sql;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlSyncInfo {
    private String dbName;
    private BaseBean bean;
    private String tableKey;
}
