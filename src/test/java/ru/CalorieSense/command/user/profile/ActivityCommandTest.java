import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.CalorieSense.command.user.profile.ActivityCommand;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.service.nutrition.CalorieCalculatorService;
import ru.CalorieSense.ui.KeyboardFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActivityCommandTest {

    @Test
    void execute_DirectCommandNoArgs_ReturnsActivityPrompt() throws Exception {
        Long chatId = 101L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getText()).thenReturn("/activity");

        UserRepository userRepository = mock(UserRepository.class);
        CalorieCalculatorService calculator = mock(CalorieCalculatorService.class);
        UserProfile profile = mock(UserProfile.class);
        when(userRepository.findByChatId(chatId)).thenReturn(profile);
        when(profile.getActivityLevel()).thenReturn("medium");
        when(calculator.getActivityLevelText("medium")).thenReturn("Средняя активность");

        ReplyKeyboardMarkup mockKeyboard = mock(ReplyKeyboardMarkup.class);
        try (MockedStatic<KeyboardFactory> keyboardMock = mockStatic(KeyboardFactory.class)) {
            keyboardMock.when(KeyboardFactory::getEditParamsKeyboard).thenReturn(mockKeyboard);

            ActivityCommand command = new ActivityCommand(userRepository, calculator);

            BotResponse response = command.execute(update);

            assertNotNull(response);
            assertEquals(chatId, response.getChatId());
            assertTrue(response.getText().contains("Текущий уровень активности"));
            assertTrue(response.getText().contains("Выберите новый уровень"));
            assertSame(mockKeyboard, response.getReplyMarkup());
        }
    }

    @Test
    void execute_InvalidInput_ReturnsErrorMessage() throws Exception {
        Long chatId = 102L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getText()).thenReturn("/activity unknown");

        UserRepository userRepository = mock(UserRepository.class);
        CalorieCalculatorService calculator = mock(CalorieCalculatorService.class);

        ReplyKeyboardMarkup mockKeyboard = mock(ReplyKeyboardMarkup.class);
        try (MockedStatic<KeyboardFactory> keyboardMock = mockStatic(KeyboardFactory.class)) {
            keyboardMock.when(KeyboardFactory::getEditParamsKeyboard).thenReturn(mockKeyboard);

            ActivityCommand command = new ActivityCommand(userRepository, calculator);

            BotResponse response = command.execute(update);

            assertNotNull(response);
            assertEquals(chatId, response.getChatId());
            assertTrue(response.getText().contains("Некорректный выбор"));
            assertSame(mockKeyboard, response.getReplyMarkup());
        }
    }

    @Test
    void execute_UpdateActivity_Successful() throws Exception {
        Long chatId = 103L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getText()).thenReturn("/activity 2");

        UserRepository userRepository = mock(UserRepository.class);
        CalorieCalculatorService calculator = mock(CalorieCalculatorService.class);
        UserProfile profile = mock(UserProfile.class);

        when(userRepository.findByChatId(chatId)).thenReturn(profile);
        doNothing().when(profile).setActivityLevel("medium");
        doNothing().when(userRepository).save(profile);

        ReplyKeyboardMarkup mockKeyboard = mock(ReplyKeyboardMarkup.class);
        try (MockedStatic<KeyboardFactory> keyboardMock = mockStatic(KeyboardFactory.class)) {
            keyboardMock.when(KeyboardFactory::getEditParamsKeyboard).thenReturn(mockKeyboard);

            ActivityCommand command = new ActivityCommand(userRepository, calculator);

            BotResponse response = command.execute(update);

            assertNotNull(response);
            assertEquals(chatId, response.getChatId());
            assertTrue(response.getText().contains("Уровень активности обновлён"));
            assertTrue(response.getText().contains("Возврат в главное меню"));
            assertSame(mockKeyboard, response.getReplyMarkup());
            verify(profile).setActivityLevel("medium");
            verify(userRepository).save(profile);
        }
    }
}
