package bean.player;

import com.alibaba.fastjson.JSON;
import core.sql.BaseIntBean;
import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.util.Map;

@Getter
@Setter
@Table("game_player_item")
public class PlayerItemBean extends BaseIntBean {
    @Column("item_info")
    private String itemInfo;

    private transient Map<String, ItemInfoBean> itemInfoBean;

    public void decode() {
        itemInfoBean = JSON.parseObject(itemInfo, Map.class);
    }

    public void encode() {
        itemInfo = JSON.toJSONString(itemInfoBean);
    }

    @Getter
    @Setter
    public static class ItemInfoBean {
        private int itemId;
        private int itemCount;
    }
}
