package com.connectinghands.patterns.factory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FactoryPatternTest {

    @Test
    void testCreatePostgresConnection() {
        Connection connection = ConnectionFactory.createConnection(ConnectionFactory.DatabaseType.POSTGRES);
        assertNotNull(connection);
        assertTrue(connection instanceof PostgresConnection);
        assertEquals("PostgreSQL Connection", connection.getConnectionInfo());
    }

    @Test
    void testCreateMySqlConnection() {
        Connection connection = ConnectionFactory.createConnection(ConnectionFactory.DatabaseType.MYSQL);
        assertNotNull(connection);
        assertTrue(connection instanceof MySqlConnection);
        assertEquals("MySQL Connection", connection.getConnectionInfo());
    }

    @Test
    void testCreateConnectionWithNullType() {
        Connection connection = ConnectionFactory.createConnection(null);
        assertNull(connection);
    }
} 