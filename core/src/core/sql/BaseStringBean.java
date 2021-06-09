package core.sql;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Name;

import java.io.Serializable;



@Getter
@Setter
public class BaseStringBean implements Serializable {
    @Name
    private String id;
}
