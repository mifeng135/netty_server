package core.util;

import core.redis.RedisDao;
import core.sql.SqlDao;
import org.nutz.dao.impl.NutDao;

public class Ins {

    public static RedisDao redis() {
        return RedisDao.getInstance();
    }

    public static NutDao sql() {
        return SqlDao.getInstance().getDao();
    }

    public static NutDao sql(String key) {
        return SqlDao.getInstance().getDao(key);
    }
}
