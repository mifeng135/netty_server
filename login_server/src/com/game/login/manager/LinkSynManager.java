package com.game.login.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2020/6/24.
 */
public class LinkSynManager {

    private ConcurrentHashMap<String, Integer> mLinkSyn = new ConcurrentHashMap<>();

    private static class DefaultInstance {
        static final LinkSynManager INSTANCE = new LinkSynManager();
    }

    public static LinkSynManager getInstance() {
        return DefaultInstance.INSTANCE;
    }

    public void putLinkInfo(String ip, int count) {
        if (mLinkSyn.get(ip) == null) {
            mLinkSyn.putIfAbsent(ip, count);
        } else {
            int currentCount = mLinkSyn.get(ip);
            if (count == currentCount) {
                return;
            }
            mLinkSyn.replace(ip, count);
        }
    }

    /***
     * 获取链接数最小的gate ip
     * @return string
     */
    public String getMinLinkCountIp() {
        int firstCount = getFirstValue();
        String ip = "";
        for (String key : mLinkSyn.keySet()) {
            int tempCount = mLinkSyn.get(key);
            if (tempCount <= firstCount) {
                ip = key;
            }
        }
        return ip;
    }

    /**
     * 获取map中的第一个元素 如果长度为0 返回 0
     * @return
     */
    private int getFirstValue() {
        int obj = 0;
        for (Map.Entry<String, Integer> entry : mLinkSyn.entrySet()) {
            obj = entry.getValue();
            break;
        }
        return obj;
    }

}
