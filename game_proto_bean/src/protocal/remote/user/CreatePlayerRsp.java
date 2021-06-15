package protocal.remote.user;


import lombok.Getter;
import lombok.Setter;
import protocal.local.db.player.PlayerAllInfoDB;


@Getter
@Setter
public class CreatePlayerRsp {
    private boolean success;
    private PlayerAllInfoDB playerAllInfoDB;
}
