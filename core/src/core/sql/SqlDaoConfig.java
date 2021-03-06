package core.sql;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.DaoInterceptor;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class SqlDaoConfig {
    private String masterFileName;
    private String sqlKey = "default";
    private String preSqlName = "";
    private List<String> slaveFileList = new ArrayList<>();
    private DaoInterceptor daoInterceptor;
}
