package bean.player;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PlayerRoleBean implements Serializable {
    private int playerIndex;
    private int job;
    private int sex;
}
