package com.jbm.hazelcast;

import com.hazelcast.core.MapStore;
import com.jbm.model.Twit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * Created by hopeng on 11/09/2016.
 */
public class TwitMapStore implements MapStore<Long, Twit> {

    private final Connection con;
    private final PreparedStatement allKeysStatement;
    private String tableName;

    public TwitMapStore(String tableName) {
        this.tableName = tableName;
        try {
            con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:9001/goldminer", "SA", "");
            con.createStatement().executeUpdate(
                    format("create table if not exists %s (id bigint not null, body clob(100000000), primary key (id))", tableName));
            allKeysStatement = con.prepareStatement("select id from " + tableName);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void delete(Long key) {
        System.out.println("Delete:" + key);
        try {
            con.createStatement().executeUpdate(
                    format("delete from %s where id = %s", tableName, key));

        } catch (SQLException e) {
            throw new RuntimeException("failed to delete by id: " + key, e);
        }
    }

    public synchronized void store(Long key, Twit value) {
        try {
            int updatedCount = con.createStatement().executeUpdate(
                    format("update %s set body='%s' where id=%s", tableName, value.getBody().replaceAll("'", ""), key));

            if (updatedCount == 0) {
                con.createStatement().executeUpdate(
                        format("insert into %s values(%s,'%s')", tableName, key, value.getBody()));
            }

        } catch (SQLException e) {
            throw new RuntimeException("failed to store twit: " + value.getBody(), e);
        }
    }

    public synchronized void storeAll(Map<Long, Twit> map) {
        for (Map.Entry<Long, Twit> entry : map.entrySet()) {
            store(entry.getKey(), entry.getValue());
        }
    }

    public synchronized void deleteAll(Collection<Long> keys) {
        for (Long key : keys) {
            delete(key);
        }
    }

    public synchronized Twit load(Long key) {
        try {
            ResultSet resultSet = con.createStatement().executeQuery(
                    format("select body from %s where id =%s", tableName, key));
            try {
                if (!resultSet.next()) {
                    return null;
                }
                String body = resultSet.getString(1);
                return new Twit(key, body);

            } finally {
                resultSet.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("failed to load by id:" + key, e);
        }
    }

    public synchronized Map<Long, Twit> loadAll(Collection<Long> keys) {
        Map<Long, Twit> result = new HashMap<>();
        for (Long key : keys) {
            result.put(key, load(key));
        }
        return result;
    }

    public Iterable<Long> loadAllKeys() {
        List<Long> result = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = allKeysStatement.executeQuery();
            while (rs.next()) {
                result.add(rs.getLong(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignored) {
                }
            }
        }

        return result;
    }
}
