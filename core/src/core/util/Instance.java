package core.util;

import core.annotation.CtrlA;
import core.annotation.QueryA;
import core.annotation.TableA;
import core.redis.RedisManager;
import core.sql.SqlDao;
import org.nutz.dao.impl.NutDao;

public class Instance {

    public static RedisManager redis() {
        return RedisManager.getInstance();
    }

    public static CtrlA ctrl() {
        return CtrlA.getInstance();
    }

    public static QueryA query() {
        return QueryA.getInstance();
    }

    public static TableA table() {
        return TableA.getInstance();
    }

    public static NutDao sql() {
        return SqlDao.getInstance().getDao();
    }

    public static NutDao sql(String key) {
        return SqlDao.getInstance().getDao(key);
    }
}
