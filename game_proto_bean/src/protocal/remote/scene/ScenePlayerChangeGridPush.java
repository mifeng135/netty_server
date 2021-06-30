package protocal.remote.scene;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScenePlayerChangeGridPush {
    private List<PlayerEnterBean> playerEnterBeanList;
    private List<Integer> playerLeaveList;
}
