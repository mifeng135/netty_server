package com.game.login;

import bean.login.LoginNoticeBean;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import core.annotation.RedisId;
import core.annotation.RedisInfo;
import core.sql.SqlHelper;
import core.sql.SqlTable;
import core.util.Instance;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginSqlBinLog implements BinaryLogClient.EventListener {


    private Map<Long, SqlTable> tableMap = new HashMap<>();

    @Override
    public void onEvent(Event event) {
        EventType eventType = event.getHeader().getEventType();

        switch (eventType) {
            case TABLE_MAP:
                tableMapEvent(event.getData());
                break;
            case UPDATE_ROWS:
            case EXT_UPDATE_ROWS:
                tableUpdateEvent(event.getData());
                break;
        }
    }

    private void tableMapEvent(TableMapEventData eventData) {
        long tableId = eventData.getTableId();
        if (tableMap.containsKey(tableId)) {
            return;
        }
        String tableName = eventData.getTable();
        String dbName = eventData.getDatabase();
        List<String> tableColumn = SqlHelper.getTableStruct(tableName, dbName);
        SqlTable table = new SqlTable();
        table.setTableId(tableId);
        table.setDbName(dbName);
        table.setTableName(tableName);
        table.setTableColumn(tableColumn);
        tableMap.put(tableId, table);
    }

    private void tableUpdateEvent(UpdateRowsEventData eventData) {
        long tableId = eventData.getTableId();
        if (!tableMap.containsKey(tableId)) {
            return;
        }

        String tableName = tableMap.get(tableId).getTableName();
        List<Map.Entry<Serializable[], Serializable[]>> rows = eventData.getRows();
        for (Map.Entry<Serializable[], Serializable[]> row : rows) {
            Serializable[] key = row.getKey();
            Serializable[] value = row.getValue();
        }
    }

    private void assignBean(Serializable[] data, String tableName, String tableId) {
        RedisInfo redisInfo = Instance.redisTable().getRedisInfo(tableName);
        if (redisInfo == null) {
            return;
        }
        SqlTable table = tableMap.get(tableId);
        List<String> tableColumn = table.getTableColumn();
        try {
            Object oc = redisInfo.getCls().newInstance();
            String redisKey = redisInfo.getRedisKey();
            Object key = null;
            for (int i = 0; i < tableColumn.size(); i++) {
                String columnName = tableColumn.get(i);
                Object value = data[i];
                Field field = oc.getClass().getDeclaredField(columnName);
                field.setAccessible(true);
                field.set(oc, value);
                if (columnName.equals(redisKey)) {
                    key = value;
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
