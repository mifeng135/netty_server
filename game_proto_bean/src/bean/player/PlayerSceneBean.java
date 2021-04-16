package bean.player;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PlayerSceneBean implements Serializable {
    private int playerIndex;
    private int playerPositionX;
    private int playerPositionY;
    private int sceneId;
}
