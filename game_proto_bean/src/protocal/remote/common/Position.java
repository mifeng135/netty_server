package protocal.remote.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Position {
    private int x;
    private int y;

    public Position(int playerPositionX, int playerPositionY) {
        x = playerPositionX;
        y = playerPositionY;
    }
}
