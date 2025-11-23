package ru.CalorieSense.repository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import ru.CalorieSense.repository.JdbcMealRepository;
import ru.CalorieSense.db.Database;
import ru.CalorieSense.model.Meal;

import static org.junit.jupiter.api.Assertions.*;
public class JdbcMealRepositoryTest {
    @Test
    void testAddMealInvokesUpdate() throws SQLException {
        Database db = Mockito.mock(Database.class);
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement ps = Mockito.mock(PreparedStatement.class);
        Mockito.when(db.getConnection()).thenReturn(conn);
        Mockito.when(conn.prepareStatement(Mockito.anyString())).thenReturn(ps);

        JdbcMealRepository repo = new JdbcMealRepository(db);
        Meal meal = new Meal(1L, 123L, LocalDateTime.now(), 400, 25, 10, 50, "Тест");
        repo.addMeal(meal);

        Mockito.verify(ps).executeUpdate();
    }

    @Test
    void testGetTodayMealsReturnsList() throws SQLException {
        Database db = Mockito.mock(Database.class);
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement ps = Mockito.mock(PreparedStatement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);

        Mockito.when(db.getConnection()).thenReturn(conn);
        Mockito.when(conn.prepareStatement(Mockito.anyString())).thenReturn(ps);
        Mockito.when(ps.executeQuery()).thenReturn(rs);

        Mockito.when(rs.next()).thenReturn(true, false);
        Mockito.when(rs.getLong("id")).thenReturn(1L);
        Mockito.when(rs.getLong("chat_id")).thenReturn(123L);
        Mockito.when(rs.getInt("calories")).thenReturn(345);
        Mockito.when(rs.getInt("protein")).thenReturn(30);
        Mockito.when(rs.getInt("fats")).thenReturn(10);
        Mockito.when(rs.getInt("carbs")).thenReturn(40);
        Mockito.when(rs.getString("description")).thenReturn("Test meal");
        Mockito.when(rs.getTimestamp(Mockito.anyString())).thenReturn(java.sql.Timestamp.valueOf(LocalDateTime.now()));

        JdbcMealRepository repo = new JdbcMealRepository(db);

        var meals = repo.getTodayMeals(123L);
        assertFalse(meals.isEmpty());
        assertEquals("Test meal", meals.get(0).getDescription());
        assertEquals(345, meals.get(0).getCalories());
    }
}
