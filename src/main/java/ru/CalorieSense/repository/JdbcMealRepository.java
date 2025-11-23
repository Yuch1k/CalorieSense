package ru.CalorieSense.repository;

import ru.CalorieSense.db.Database;
import ru.CalorieSense.model.Meal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcMealRepository implements MealRepository {
    private final Database database;

    public JdbcMealRepository(Database database) {
        this.database = database;
    }

    @Override
    public Long generateId() {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 AS next_id FROM meal";
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("next_id");
            }
            return 1L;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при генерации id для meal", e);
        }
    }

    @Override
    public void addMeal(Meal meal) {
        String sql = """
            INSERT INTO meal (id, chat_id, timestamp, calories, protein, fats, carbs, description)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, meal.getId());
            ps.setLong(2, meal.getChatId());
            ps.setTimestamp(3, Timestamp.valueOf(meal.getDateTime()));
            ps.setInt(4, meal.getCalories());
            ps.setInt(5, meal.getProtein());
            ps.setInt(6, meal.getFats());
            ps.setInt(7, meal.getCarbs());
            ps.setString(8, meal.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении приёма пищи " + meal.getId(), e);
        }
    }

    @Override
    public List<Meal> getTodayMeals(Long chatId) {
        LocalDate today = LocalDate.now();
        return getMealsByDate(chatId, today);
    }

    @Override
    public List<Meal> getMealsByDate(Long chatId, LocalDate date) {
        String sql = """
            SELECT id, chat_id, timestamp, calories, protein, fats, carbs, description
            FROM meal
            WHERE chat_id = ?
              AND timestamp::date = ?
            ORDER BY timestamp
        """;
        List<Meal> result = new ArrayList<>();
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, chatId);
            ps.setObject(2, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToMeal(rs));
                }
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при загрузке приёмов пищи для пользователя " + chatId, e);
        }
    }

    @Override
    public void deleteMeal(Long id) {
        String sql = "DELETE FROM meal WHERE id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении приёма пищи " + id, e);
        }
    }

    private Meal mapRowToMeal(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        Long chatId = rs.getLong("chat_id");
        LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
        int calories = rs.getInt("calories");
        int protein = rs.getInt("protein");
        int fats = rs.getInt("fats");
        int carbs = rs.getInt("carbs");
        String description = rs.getString("description");
        return new Meal(id, chatId, timestamp, calories, protein, fats, carbs, description);
    }
}
