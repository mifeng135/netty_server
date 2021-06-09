package core.sql;


import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Id;

import java.io.Serializable;

@Getter
@Setter
public class BaseIntBean implements Serializable {
    @Id(auto = false)
    private int id;
}
