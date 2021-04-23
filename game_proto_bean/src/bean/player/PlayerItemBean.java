package bean.player;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.io.Serializable;

@Getter
@Setter
@Table("game_player_item")
public class PlayerItemBean implements Serializable {

    @Column("player_index")
    private int playerIndex;

    @Column
    private int itemId;

    @Column("item_count")
    private int itemCount;
}
