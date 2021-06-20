package protocal.local.db.player;


import bean.db.player.PlayerInfoBean;
import bean.db.player.PlayerItemBean;
import bean.db.player.PlayerRoleBean;
import bean.db.player.PlayerSceneBean;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlayerAllInfoDB {
    private PlayerInfoBean playerInfo;
    private PlayerSceneBean playerScene;
    private List<PlayerItemBean> playerItemList;
    private PlayerRoleBean playerRole;
}
