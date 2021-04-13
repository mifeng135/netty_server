package com.game.server.mappers;

import com.game.server.bean.NoticeBean;
import com.game.server.constant.SqlCmdConstant;
import core.annotation.SqlCmd;
import core.sql.SqlConstant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ServerNoticeMapper {

    @SqlCmd(sqlCmd = SqlCmdConstant.NOTICE_LIST_SELECT_ALL, sqlType = SqlConstant.SELECT_LIST)
    @Select("select notice_id, content from game_notice_list")
    List<NoticeBean> getAllNotice();


    @SqlCmd(sqlCmd = SqlCmdConstant.NOTICE_UPDATE_CONTENT, sqlType = SqlConstant.UPDATE)
    @Update("update game_notice_list set content = #{content} where notice_id = #{noticeId}")
    void updateServerIp(NoticeBean noticeBean);

    @SqlCmd(sqlCmd = SqlCmdConstant.NOTICE_DELETE, sqlType = SqlConstant.DELETE)
    @Update("delete from game_notice_list where notice_id = #{noticeId}")
    void delete(NoticeBean noticeBean);

    @SqlCmd(sqlCmd = SqlCmdConstant.NOTICE_DELETE, sqlType = SqlConstant.INSERT)
    @Insert("insert into game_notice_list (notice_id, content)" + "values (#{noticeId}, #{content})")
    void insertServer(NoticeBean serverListBean);

}
