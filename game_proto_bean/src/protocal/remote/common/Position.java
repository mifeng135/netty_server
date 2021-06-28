package protocal.remote.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Position {
    private int x;
    private int y;

    public void sub(int subX, int subY) {
        x = x - subX;
        y = y - subY;
    }
}
