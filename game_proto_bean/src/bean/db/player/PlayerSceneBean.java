package bean.db.player;

import core.annotation.redis.Redis;
import core.sql.BaseIntBean;
import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;


@Getter
@Setter
@Table("game_player_scene")
@Redis(name = "game_player_scene")
public class PlayerSceneBean extends BaseIntBean {
    @Column("player_position_x")
    private int playerPositionX;

    @Column("player_position_y")
    private int playerPositionY;

    @Column("scene_id")
    private int sceneId;
}
