package com.game.server.proto;

import java.util.List;

/**
 * Created by Administrator on 2020/7/15.
 */
public class ProtoBombExplodeS {

    private List<Integer> deadList;
    public List<Integer> getDeadList() {
        return deadList;
    }

    public void setDeadList(List<Integer> deadList) {
        this.deadList = deadList;
    }
}
