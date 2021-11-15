package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.config.ConfigFactory;
import edu.bbte.idde.paim1949.backend.config.JdbcConfig;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Deque;
import java.util.LinkedList;

@Slf4j
public final class ConnectionPool {
    private static ConnectionPool instance;
    private static Integer POOL_SIZE;
    private final Deque<Connection> pool = new LinkedList<>();

    private ConnectionPool() {
        try {
            Class.forName(ConfigFactory.getConfig().getDriverClass());
            JdbcConfig jdbcConfig = ConfigFactory.getConfig().getConnection();
            POOL_SIZE = jdbcConfig.getPoolSize();
            for (int i = 0; i < POOL_SIZE; i++) {
                pool.push(DriverManager.getConnection(
                        jdbcConfig.getUrl(),
                        jdbcConfig.getUser(),
                        jdbcConfig.getPassword()));
            }
            log.info("Connection pool of size {} initialized", POOL_SIZE);
        } catch (SQLException e) {
            log.error("Database connection could not be established.");
        } catch (ClassNotFoundException e) {
            log.error("Driver class not found.");
        }
    }

    public synchronized Connection getConnection() throws JdbcException {
        if (pool.isEmpty()) {
            throw new JdbcException("No available connections in pool");
        }
        log.debug("Giving out connection from pool");
        return pool.pop();
    }

    public synchronized void returnConnection(Connection connection) {
        if (pool.size() < POOL_SIZE) {
            log.debug("Returning connection to pool");
            pool.push(connection);
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }
}
