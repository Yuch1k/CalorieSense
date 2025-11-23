import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.CalorieSense.command.user.profile.GoalCommand;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.ui.KeyboardFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoalCommandTest {

    @Test
    void execute_NoArguments_ShowsGoalPrompt() throws Exception {
        Long chatId = 100L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getText()).thenReturn("/goal");

        UserRepository repository = mock(UserRepository.class);
        UserProfile profile = mock(UserProfile.class);
        when(repository.findByChatId(chatId)).thenReturn(profile);
        when(profile.getGoal()).thenReturn("lose");

        ReplyKeyboardMarkup keyboard = mock(ReplyKeyboardMarkup.class);
        try (MockedStatic<KeyboardFactory> keyboardFactory = mockStatic(KeyboardFactory.class)) {
            keyboardFactory.when(KeyboardFactory::getEditParamsKeyboard).thenReturn(keyboard);

            GoalCommand command = new GoalCommand(repository);
            BotResponse response = command.execute(update);

            assertNotNull(response);
            assertEquals(chatId, response.getChatId());
            assertSame(keyboard, response.getReplyMarkup());
            assertTrue(response.getText().contains("Текущая цель"));
            assertTrue(response.getText().contains("Похудение"));
        }
    }

    @Test
    void execute_InvalidGoalInput_ReturnsError() throws Exception {
        Long chatId = 101L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getText()).thenReturn("/goal invalid");

        UserRepository repository = mock(UserRepository.class);

        ReplyKeyboardMarkup keyboard = mock(ReplyKeyboardMarkup.class);
        try (MockedStatic<KeyboardFactory> keyboardFactory = mockStatic(KeyboardFactory.class)) {
            keyboardFactory.when(KeyboardFactory::getEditParamsKeyboard).thenReturn(keyboard);

            GoalCommand command = new GoalCommand(repository);
            BotResponse response = command.execute(update);

            assertNotNull(response);
            assertEquals(chatId, response.getChatId());
            assertSame(keyboard, response.getReplyMarkup());
            assertTrue(response.getText().contains("❌ Некорректный выбор"));
        }
    }

    @Test
    void execute_ValidGoalInput_UpdatesProfile() throws Exception {
        Long chatId = 102L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getText()).thenReturn("/goal 1");

        UserRepository repository = mock(UserRepository.class);
        UserProfile profile = mock(UserProfile.class);
        when(repository.findByChatId(chatId)).thenReturn(profile);

        doNothing().when(profile).setGoal("lose");
        doNothing().when(repository).save(profile);

        ReplyKeyboardMarkup keyboard = mock(ReplyKeyboardMarkup.class);
        try (MockedStatic<KeyboardFactory> keyboardFactory = mockStatic(KeyboardFactory.class)) {
            keyboardFactory.when(KeyboardFactory::getMainKeyboard).thenReturn(keyboard);

            GoalCommand command = new GoalCommand(repository);
            BotResponse response = command.execute(update);

            assertNotNull(response);
            assertEquals(chatId, response.getChatId());
            assertSame(keyboard, response.getReplyMarkup());
            assertTrue(response.getText().contains("✅ Цель обновлена"));
            assertTrue(response.getText().contains("Похудение"));

            verify(profile).setGoal("lose");
            verify(repository).save(profile);
        }
    }
}
