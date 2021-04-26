package protocal.local.db.player;


import bean.player.PlayerInfoBean;
import bean.player.PlayerItemBean;
import bean.player.PlayerRoleBean;
import bean.player.PlayerSceneBean;
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
