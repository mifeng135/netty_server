package core.sql;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SqlTable {
    private long tableId;
    private String dbName;
    private String tableName;
    private List<String> tableColumn;
}
