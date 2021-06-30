package protocal.remote.user;


import bean.db.player.PlayerItemBean;
import bean.db.player.PlayerRoleBean;
import bean.db.player.PlayerSceneBean;
import lombok.Getter;
import lombok.Setter;
import protocal.local.db.player.PlayerAllInfoDB;

import java.util.List;


@Getter
@Setter
public class EnterGameRsp {
    private boolean hasRole;
    private PlayerSceneBean playerSceneBean;
    private PlayerRoleBean playerRoleBean;
    private List<PlayerItemBean> playerItemList;
}
