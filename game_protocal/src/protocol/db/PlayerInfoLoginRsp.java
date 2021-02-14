package protocol.db;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerInfoLoginRsp extends BaseDB {
    private int playerIndex;
    private String name;
    private int result;
}
