package com.game.server.proto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2020/7/4.
 */
@Getter
@Setter
public class ProtoLinkSynR {
    private String ip;
    private int connectCount;
}
