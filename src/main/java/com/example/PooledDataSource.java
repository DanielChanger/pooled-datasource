package com.example;

import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PooledDataSource extends PGSimpleDataSource {
    private final Queue<Connection> dataSourcesPool;

    public PooledDataSource(int poolSize) {
        dataSourcesPool = new ConcurrentLinkedQueue<>();
        initPool(poolSize);
    }

    @SneakyThrows
    private void initPool(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            Connection connection = new ConnectionProxy(super.getConnection(), dataSourcesPool);
            dataSourcesPool.add(connection);
        }
    }

    @Override
    public Connection getConnection() {
        return dataSourcesPool.poll();
    }
}
