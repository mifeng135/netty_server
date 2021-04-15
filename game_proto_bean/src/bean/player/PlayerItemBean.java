package bean.player;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PlayerItemBean implements Serializable {
    private int playerIndex;
    private int itemId;
    private int itemCount;
}
