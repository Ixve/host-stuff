package de.aggromc.confighubhost.auth;

import java.util.*;
import com.google.common.collect.*;
import java.util.function.*;
import java.sql.*;
import java.util.concurrent.*;
import javax.sql.rowset.*;

public class WeirdoSQL
{
    private static ExecutorService threadPool;
    private static RowSetFactory factory;
    private final String sql;
    private final Connection connection;
    private List<Object> parameters;
    
    public WeirdoSQL(final String sql, final Connection connection) {
        this.parameters = (List<Object>)Lists.newArrayList();
        this.sql = sql;
        this.connection = connection;
    }
    
    public WeirdoSQL addParameter(final Object... parameters) {
        for (final Object parameter : parameters) {
            this.parameters.add(parameter);
        }
        return this;
    }
    
    public void updateSync() {
        this.executeSync(false);
    }
    
    public ResultSet querySync() {
        return this.executeSync(true);
    }
    
    public void updateAsync() {
        this.executeAsync(null, false);
    }
    
    public void updateAsync(final Runnable callback) {
        this.executeAsync(new Consumer<CachedRowSet>() {
            @Override
            public void accept(final CachedRowSet t) {
                callback.run();
            }
        }, false);
    }
    
    public void queryAsync(final Consumer<CachedRowSet> callback) {
        this.executeAsync(callback, true);
    }
    
    private CachedRowSet executeSync(final boolean isQuery) {
        try {
            final PreparedStatement preparedStatement = this.connection.prepareStatement(this.sql);
            ResultSet resultSet = null;
            CachedRowSet cachedRowSet = null;
            for (int i = 0; i < this.parameters.size(); ++i) {
                preparedStatement.setObject(i + 1, this.parameters.get(i));
            }
            if (!isQuery) {
                preparedStatement.executeUpdate();
                this.close(null, preparedStatement);
                return null;
            }
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                cachedRowSet = WeirdoSQL.factory.createCachedRowSet();
                cachedRowSet.populate(resultSet);
                this.close(resultSet, preparedStatement);
                return cachedRowSet;
            }
            this.close(null, preparedStatement);
            return null;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void executeAsync(final Consumer<CachedRowSet> callback, final boolean isQuery) {
        final CachedRowSet cachedRowSet;
        WeirdoSQL.threadPool.execute(() -> {
            cachedRowSet = this.executeSync(isQuery);
            if (callback != null) {
                callback.accept(cachedRowSet);
            }
        });
    }
    
    private void close(final ResultSet resultSet, final PreparedStatement statement) throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }
        if (this.connection != null) {
            this.connection.close();
        }
        if (statement != null) {
            statement.close();
        }
    }
    
    static {
        WeirdoSQL.threadPool = Executors.newFixedThreadPool(50);
        try {
            WeirdoSQL.factory = RowSetProvider.newFactory();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
