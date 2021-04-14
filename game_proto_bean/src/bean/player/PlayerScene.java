package bean.player;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PlayerScene implements Serializable {
    private int id;
    private int playerIndex;
    private float playerPositionX;
    private float playerPositionY;
    private int sceneId;
}
