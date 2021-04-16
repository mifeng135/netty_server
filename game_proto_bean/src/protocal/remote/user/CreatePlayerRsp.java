package protocal.remote.user;


import bean.player.PlayerItemBean;
import bean.player.PlayerRoleBean;
import bean.player.PlayerSceneBean;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreatePlayerRsp {
    private boolean hasRole;
    private PlayerSceneBean playerScene;
    private PlayerRoleBean playerRole;
    private List<PlayerItemBean> playerItemList;
}
