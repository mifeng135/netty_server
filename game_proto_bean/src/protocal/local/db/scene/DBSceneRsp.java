package protocal.local.db.scene;

import bean.player.PlayerSceneBean;
import lombok.Getter;
import lombok.Setter;
import protocal.local.common.PlayerSceneProto;

@Setter
@Getter
public class DBSceneRsp {
    private PlayerSceneBean playerScene;
}
