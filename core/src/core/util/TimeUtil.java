package core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2020/7/6.
 */
public class TimeUtil {

    /**
     * get current time second
     *
     * @return
     */
    public static int getCurrentTimeSecond() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static String stampToDate(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return simpleDateFormat.format(date);
    }
}
