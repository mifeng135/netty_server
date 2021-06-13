package core.proto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProtoProperty {
    private String type; // proto 类型
    private String className; // 如果为 map repeated 则此类型为 容器类型
    private String name; //proto 名称
    private String mapKey; //如果type为map 则mapKey = map 中的key
}
