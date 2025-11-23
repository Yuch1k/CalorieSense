package ru.CalorieSense.repository;

import ru.CalorieSense.db.Database;
import ru.CalorieSense.model.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUserRepository implements UserRepository {
    private final Database database;

    public JdbcUserRepository(Database database) {
        this.database = database;
    }

    @Override
    public UserProfile findByChatId(Long chatId) {
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT chat_id, weight, height, age, sex, goal, activity_level, calories_goal " +
                             "FROM user_profile WHERE chat_id = ?")) {
            ps.setLong(1, chatId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToProfile(rs);
                }
                UserProfile profile = new UserProfile(chatId);
                save(profile);
                return profile;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при загрузке профиля пользователя " + chatId, e);
        }
    }

    @Override
    public void save(UserProfile profile) {
        String sql = """
                INSERT INTO user_profile (chat_id, weight, height, age, sex, goal, activity_level, calories_goal)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT (chat_id) DO UPDATE SET
                weight = EXCLUDED.weight,
                height = EXCLUDED.height,
                age = EXCLUDED.age,
                sex = EXCLUDED.sex,
                goal = EXCLUDED.goal,
                activity_level = EXCLUDED.activity_level,
                calories_goal = EXCLUDED.calories_goal
                """;
        try (Connection conn = database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, profile.getChatId());
            ps.setDouble(2, profile.getWeight());
            ps.setInt(3, profile.getHeight());
            ps.setInt(4, profile.getAge());
            ps.setString(5, profile.getSex());
            ps.setString(6, profile.getGoal());
            ps.setString(7, profile.getActivityLevel());
            ps.setInt(8, profile.getCaloriesGoal());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении профиля пользователя " + profile.getChatId(), e);
        }
    }

    @Override
    public void updateWeight(Long chatId, double weight) {
        UserProfile profile = findByChatId(chatId);
        profile.setWeight(weight);
        save(profile);
    }

    @Override
    public void updateHeight(Long chatId, int height) {
        UserProfile profile = findByChatId(chatId);
        profile.setHeight(height);
        save(profile);
    }

    @Override
    public void updateSex(Long chatId, String sex) {
        UserProfile profile = findByChatId(chatId);
        profile.setSex(sex);
        save(profile);
    }

    @Override
    public void updateAge(Long chatId, int age) {
        UserProfile profile = findByChatId(chatId);
        profile.setAge(age);
        save(profile);
    }

    @Override
    public void updateGoal(Long chatId, String goal) {
        UserProfile profile = findByChatId(chatId);
        profile.setGoal(goal);
        save(profile);
    }

    @Override
    public void updateActivityLevel(Long chatId, String activityLevel) {
        UserProfile profile = findByChatId(chatId);
        profile.setActivityLevel(activityLevel);
        save(profile);
    }

    private UserProfile mapRowToProfile(ResultSet rs) throws SQLException {
        long chatId = rs.getLong("chat_id");
        UserProfile profile = new UserProfile(chatId);

        double weight = rs.getDouble("weight");
        if (!rs.wasNull()) profile.setWeight(weight);

        int height = rs.getInt("height");
        if (!rs.wasNull()) profile.setHeight(height);

        int age = rs.getInt("age");
        if (!rs.wasNull()) profile.setAge(age);

        String sex = rs.getString("sex");
        if (sex != null) profile.setSex(sex);

        String goal = rs.getString("goal");
        if (goal != null) profile.setGoal(goal);

        String activityLevel = rs.getString("activity_level");
        if (activityLevel != null) profile.setActivityLevel(activityLevel);

        int caloriesGoal = rs.getInt("calories_goal");
        if (!rs.wasNull()) profile.setCaloriesGoal(caloriesGoal);

        return profile;
    }
}
