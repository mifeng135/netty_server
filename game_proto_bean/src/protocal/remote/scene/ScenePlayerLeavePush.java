package protocal.remote.scene;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScenePlayerLeavePush {
    private List<Integer> leaveList;
}
