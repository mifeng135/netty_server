package protocal.remote.user;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePlayerReq {
    private String name;
    private String openId;
    private int sex;
    private int job;
}
