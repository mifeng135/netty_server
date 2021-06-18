package core.msg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientExceptionMsg {
    private String exception;
    private transient long time;
}
