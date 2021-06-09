package core.sql;


import org.nutz.dao.entity.annotation.Name;

import java.io.Serializable;


public class BaseStringBean extends BaseBean<String> implements Serializable {
    @Name
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String value) {
        id = value;
    }
}
