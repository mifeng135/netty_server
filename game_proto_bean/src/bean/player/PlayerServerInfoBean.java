package bean.player;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Getter
@Setter
@Table("game_player_server_info")
public class PlayerServerInfoBean {
    @Id
    @Column("player_index")
    private int playerIndex;

    @Column("server_id")
    private int serverId;
}
