package ru.CalorieSense.db;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    @Test
    void fromPropertiesCreatesInstance() {
        Properties props = new Properties();
        props.setProperty("db.url", "jdbc:postgresql://localhost:5432/test");
        props.setProperty("db.user", "testuser");
        props.setProperty("db.password", "testpass");

        Database db = Database.fromProperties(props);

        assertNotNull(db);
    }

    @Test
    void fromPropertiesThrowsWhenUrlOrUserMissing() {
        Properties props = new Properties();
        // нет db.url и db.user

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> Database.fromProperties(props)
        );
        assertTrue(ex.getMessage().contains("db.url"));
        assertTrue(ex.getMessage().contains("db.user"));
    }
}
