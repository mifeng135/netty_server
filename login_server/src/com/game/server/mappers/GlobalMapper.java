package com.game.server.mappers;

import com.game.server.core.annotation.SqlCmd;
import com.game.server.constant.SqlCmdConstant;
import com.game.server.core.sql.SqlConstant;
import com.game.server.bean.GlobalBean;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2020/6/4.
 */

public interface GlobalMapper {

    @SqlCmd(sqlCmd = 1111, sqlType = SqlConstant.SELECT_LIST)
    @Select("select value_int,VALUE_STR,PRO_1,PRO_2,PRO_3,PRO_4,PRO_5,PARA_ID,PARA_DESC,ISCLIENT from t_global")
    public List<GlobalBean> selectAll();

    @SqlCmd(sqlCmd = 2222, sqlType = SqlConstant.SELECT_LIST)
    @Select("select * from t_global")
    public List<GlobalBean> selectAll2();

    @SqlCmd(sqlCmd = SqlCmdConstant.GLOBAL_SELECT_BY_ID, sqlType = SqlConstant.SELECT_ONE)
    @Select("select * from t_global where PARA_ID = #{id}")
    public GlobalBean selectById(@Param("id") int id);
}
