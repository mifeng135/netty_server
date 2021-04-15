package protocal.remote.user;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePlayerRsp {
    private String name;
    private int playerPositionX;
    private int playerPositionY;
    private int sceneId;
    private int job;
    private int sex;
}
