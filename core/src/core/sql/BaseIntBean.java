package core.sql;


import org.nutz.dao.entity.annotation.Id;

import java.io.Serializable;


public class BaseIntBean extends BaseBean<Integer> implements Serializable {
    @Id(auto = false)
    private int id;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer value) {
        id = value;
    }
}
