package com.game.login;

import org.nutz.dao.DaoException;
import org.nutz.dao.DaoInterceptor;
import org.nutz.dao.DaoInterceptorChain;
import org.nutz.dao.sql.DaoStatement;

public class LoginDaoInterceptor implements DaoInterceptor {
    @Override
    public void filter(DaoInterceptorChain chain) throws DaoException {
        chain.doChain();
    }
}
