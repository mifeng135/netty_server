package bean.player;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/6/5.
 */

@Getter
@Setter
public class PlayerBean implements Serializable {
    private int playerIndex;
    private String name;
    private int registerTime;
    private String loginIp;
    private int lastLoginTime;
    private String header;
}
