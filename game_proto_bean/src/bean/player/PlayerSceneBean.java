package bean.player;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.io.Serializable;

@Getter
@Setter
@Table("game_player_scene")
public class PlayerSceneBean implements Serializable {
    @Column("player_index")
    private int playerIndex;

    @Column("player_position_x")
    private int playerPositionX;

    @Column("player_position_y")
    private int playerPositionY;

    @Column("scene_id")
    private int sceneId;
}
