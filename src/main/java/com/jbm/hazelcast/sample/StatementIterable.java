package com.jbm.hazelcast.sample;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Created by hopeng on 11/09/2016.
 */
public class StatementIterable<T> implements Iterable<T> {

    private PreparedStatement statement;

    public StatementIterable(PreparedStatement statement) {
        this.statement = statement;
    }

    @Override
    public Iterator<T> iterator() {
        try {
            return new ResultSetIterator<T>(statement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
