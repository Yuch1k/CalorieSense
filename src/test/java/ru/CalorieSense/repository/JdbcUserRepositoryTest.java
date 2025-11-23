package ru.CalorieSense.repository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ru.CalorieSense.repository.JdbcUserRepository;
import ru.CalorieSense.db.Database;
import ru.CalorieSense.model.UserProfile;

import static org.junit.jupiter.api.Assertions.*;
public class JdbcUserRepositoryTest {
    @Test
    void testFindByChatIdReturnsProfile() throws SQLException {
        Database db = Mockito.mock(Database.class);
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement ps = Mockito.mock(PreparedStatement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);

        Mockito.when(db.getConnection()).thenReturn(conn);
        Mockito.when(conn.prepareStatement(Mockito.anyString())).thenReturn(ps);
        Mockito.when(ps.executeQuery()).thenReturn(rs);

        long chatId = 123L;
        Mockito.when(rs.next()).thenReturn(true);
        Mockito.when(rs.getLong("chat_id")).thenReturn(chatId);
        Mockito.when(rs.getDouble("weight")).thenReturn(75.0);
        Mockito.when(rs.getInt("height")).thenReturn(180);
        Mockito.when(rs.getInt("age")).thenReturn(30);
        Mockito.when(rs.getString("sex")).thenReturn("male");

        JdbcUserRepository repo = new JdbcUserRepository(db);
        UserProfile found = repo.findByChatId(chatId);

        assertNotNull(found);
        assertEquals(75.0, found.getWeight());
        assertEquals(180, found.getHeight());
        assertEquals(30, found.getAge());
        assertEquals("male", found.getSex());
    }

    @Test
    void testSaveAndUpdate() throws SQLException {
        Database db = Mockito.mock(Database.class);
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement ps = Mockito.mock(PreparedStatement.class);

        Mockito.when(db.getConnection()).thenReturn(conn);
        Mockito.when(conn.prepareStatement(Mockito.anyString())).thenReturn(ps);

        JdbcUserRepository repo = new JdbcUserRepository(db);
        UserProfile profile = new UserProfile(321L);
        profile.setWeight(80.5);
        profile.setHeight(177);
        profile.setAge(27);
        profile.setSex("female");

        repo.save(profile);
        Mockito.verify(ps).executeUpdate(); // Проверяет, что был апдейт/инсерт
    }
}
