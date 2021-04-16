package protocal.remote.user;


import bean.player.PlayerBean;
import bean.player.PlayerItemBean;
import bean.player.PlayerRoleBean;
import bean.player.PlayerSceneBean;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EnterGameRsp {
    private boolean hasRole;
    private PlayerBean playerInfo;
    private PlayerSceneBean playerScene;
    private PlayerRoleBean playerRole;
    private List<PlayerItemBean> playerItemList;
}
