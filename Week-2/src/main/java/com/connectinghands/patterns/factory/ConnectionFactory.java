package com.connectinghands.patterns.factory;

public class ConnectionFactory {

    public enum DatabaseType {
        POSTGRES,
        MYSQL
    }

    public static Connection createConnection(DatabaseType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case POSTGRES:
                return new PostgresConnection();
            case MYSQL:
                return new MySqlConnection();
            default:
                throw new IllegalArgumentException("Unknown database type: " + type);
        }
    }
} 