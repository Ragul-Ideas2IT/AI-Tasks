package com.connectinghands.patterns.factory;

public class FactoryClient {
    public static void main(String[] args) {
        // Get a PostgreSQL connection
        System.out.println("Requesting PostgreSQL Connection:");
        Connection postgres = ConnectionFactory.createConnection(ConnectionFactory.DatabaseType.POSTGRES);
        if (postgres != null) {
            System.out.println("Connection Type: " + postgres.getConnectionInfo());
            postgres.connect();
            postgres.disconnect();
        }

        System.out.println("\n-----------------------------------\n");

        // Get a MySQL connection
        System.out.println("Requesting MySQL Connection:");
        Connection mysql = ConnectionFactory.createConnection(ConnectionFactory.DatabaseType.MYSQL);
        if (mysql != null) {
            System.out.println("Connection Type: " + mysql.getConnectionInfo());
            mysql.connect();
            mysql.disconnect();
        }
    }
} 