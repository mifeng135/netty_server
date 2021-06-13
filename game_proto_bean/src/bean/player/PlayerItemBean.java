package bean.player;

import bean.sub.SubItemInfoBean;
import com.alibaba.fastjson.JSON;
import core.sql.BaseIntBean;
import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@Table("game_player_item")
public class PlayerItemBean extends BaseIntBean {
    @Column("item_info")
    private String itemInfo;

    public Map decode() {
        if (itemInfo == null) {
            return null;
        }
        return JSON.parseObject(itemInfo, ConcurrentHashMap.class);
    }

    public void encode(Map<Integer, SubItemInfoBean> data) {
        itemInfo = JSON.toJSONString(data);
    }
}
