package bean.player;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PlayerItem implements Serializable {
    private int id;
    private int playerIndex;
    private int itemId;
    private int itemCount;
}
