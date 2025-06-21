package com.connectinghands.patterns.factory;

public interface Connection {
    String getConnectionInfo();
    void connect();
    void disconnect();
} 