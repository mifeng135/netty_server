package core.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2020/12/5.
 */
public class HelperUtil {

    private static AtomicInteger serverInteger = new AtomicInteger(0);

    /**
     * 生成server 唯一 key
     *
     * @return int serverkey
     */
    public static int generateServerKey() {
        return serverInteger.incrementAndGet();
    }
}
