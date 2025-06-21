package com.connectinghands.patterns.factory;

public class PostgresConnection implements Connection {

    @Override
    public String getConnectionInfo() {
        return "PostgreSQL Connection";
    }

    @Override
    public void connect() {
        System.out.println("Connecting to PostgreSQL database...");
    }

    @Override
    public void disconnect() {
        System.out.println("Disconnecting from PostgreSQL database.");
    }
}