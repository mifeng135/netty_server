package protocal.remote.user;


import core.annotation.proto.Proto;
import lombok.Getter;
import lombok.Setter;
import protocal.local.db.player.PlayerAllInfoDB;


@Getter
@Setter
@Proto
public class CreatePlayerRsp {
    private boolean success;
    private PlayerAllInfoDB playerAllInfoDB;
}
