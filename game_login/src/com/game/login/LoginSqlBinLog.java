package com.game.login;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import core.annotation.RedisInfo;
import core.sql.SqlHelper;
import core.sql.SqlTable;
import core.util.Instance;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static core.redis.RedisStoreType.R_CACHE_MAP;
import static core.redis.RedisStoreType.R_MAP;

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
            assignUpdateBean(value, tableName, tableId);
        }
    }

    private void assignUpdateBean(Serializable[] data, String tableName, long tableId) {
        RedisInfo redisInfo = Instance.redisTable().getRedisInfo(tableName);
        if (redisInfo == null) {
            return;
        }
        SqlTable table = tableMap.get(tableId);
        List<String> tableColumn = table.getTableColumn();
        try {
            Object oc = redisInfo.getCls().newInstance();
            String redisId = redisInfo.getId();
            int storeType = redisInfo.getStoreType();
            Object mapKey = null;
            for (int i = 0; i < tableColumn.size(); i++) {
                String columnName = tableColumn.get(i);
                Object value = data[i];
                Field field = oc.getClass().getDeclaredField(columnName);
                field.setAccessible(true);
                field.set(oc, value);
                if (columnName.equals(redisId)) {
                    mapKey = value;
                }
            }
            storeToRedis(storeType, redisInfo.getKey(), mapKey, oc);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void storeToRedis(int storeType, String redisKe, Object mapKey, Object oc) {
        System.out.println("212222");
        switch (storeType) {
            case R_MAP:
                Instance.redis().mapPut(redisKe, mapKey, oc);
                break;
            case R_CACHE_MAP:
                Instance.redis().cacheMapPut(redisKe, mapKey, oc);
                break;
        }
    }
}
