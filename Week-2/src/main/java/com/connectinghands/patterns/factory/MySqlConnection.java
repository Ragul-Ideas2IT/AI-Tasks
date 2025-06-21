package com.connectinghands.patterns.factory;

public class MySqlConnection implements Connection {

    @Override
    public String getConnectionInfo() {
        return "MySQL Connection";
    }

    @Override
    public void connect() {
        System.out.println("Connecting to MySQL database...");
    }

    @Override
    public void disconnect() {
        System.out.println("Disconnecting from MySQL database.");
    }
} 