package br.com.zenon.configs;

import br.com.zenon.exceptions.DatabaseConstraintsException;
import br.com.zenon.exceptions.ErrorDetail;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLDatabaseFactory {

    private static final String HOST = "localhost";
    private static final int PORT = 3306;
    private static final String DATABASE = "zenon_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "test@123";
    private static final String JDBC_URL_TEMPLATE = "jdbc:mysql://%s:%d/%s";

    private final Connection connection;

    private MySQLDatabaseFactory() {
        try {
            this.connection = DriverManager.getConnection(
                    JDBC_URL_TEMPLATE.formatted(HOST, PORT, DATABASE),
                    USERNAME,
                    PASSWORD
            );
        } catch (Exception e) {
            throw DatabaseConstraintsException.with(ErrorDetail.of("mysql_connection", e.getMessage(), this.getClass()));
        }
    }

    private static class Holder {
        private static final MySQLDatabaseFactory INSTANCE = new MySQLDatabaseFactory();
    }

    public static MySQLDatabaseFactory getInstance() {
        return Holder.INSTANCE;
    }

    public Connection getConnection() {
        return this.connection;
    }


}
