package ru.CalorieSense.db;
import org.junit.jupiter.api.Test;
import java.util.Properties;
import ru.CalorieSense.db.Database;

import static org.junit.jupiter.api.Assertions.*;
public class DatabaseTest {
    @Test
    void testFromPropertiesCreatesInstance() {
        Properties props = new Properties();
        props.setProperty("db.url", "jdbc:postgresql://localhost:5432/test");
        props.setProperty("db.user", "testuser");
        props.setProperty("db.password", "testpass");
        Database db = Database.fromProperties(props);
        assertNotNull(db);
    }

    @Test
    void testGetConnectionThrowsOnError() {
        Properties props = new Properties();
        props.setProperty("db.url", "jdbc:wrong:///");
        props.setProperty("db.user", "no");
        props.setProperty("db.password", "no");
        Database db = Database.fromProperties(props);
        assertThrows(RuntimeException.class, db::getConnection);
    }
}
