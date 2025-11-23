import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.CalorieSense.command.nutrition.meal.ManualInputCommand;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.MealInputState;
import ru.CalorieSense.repository.MealRepository;
import ru.CalorieSense.ui.KeyboardFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManualInputCommandTest {

    @Test
    void execute_noneStep_ReturnsCaloriesPrompt() throws Exception {
        // Arrange
        Long chatId = 1234L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getText()).thenReturn("");

        ReplyKeyboardMarkup mockKeyboard = mock(ReplyKeyboardMarkup.class);
        MealRepository mealRepository = mock(MealRepository.class);

        try (MockedStatic<KeyboardFactory> keyboardFactoryMock = mockStatic(KeyboardFactory.class)) {
            keyboardFactoryMock.when(KeyboardFactory::getMealInputKeyboard).thenReturn(mockKeyboard);

            ManualInputCommand command = new ManualInputCommand(mealRepository);

            BotResponse response = command.execute(update);

            assertNotNull(response);
            assertEquals(chatId, response.getChatId());
            assertEquals("Markdown", response.getParseMode());
            assertSame(mockKeyboard, response.getReplyMarkup());
            assertTrue(response.getText().contains("Введите количество калорий"));
        }
    }

    @Test
    void execute_caloriesStep_InvalidInput_ReturnsError() throws Exception {
        // Arrange
        Long chatId = 1234L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getText()).thenReturn("invalid");

        ReplyKeyboardMarkup mockKeyboard = mock(ReplyKeyboardMarkup.class);
        MealRepository mealRepository = mock(MealRepository.class);

        ManualInputCommand command = new ManualInputCommand(mealRepository);

        var state = new MealInputState();
        state.setStep(MealInputState.Step.CALORIES);
        var stateMapField = ManualInputCommand.class.getDeclaredField("stateMap");
        stateMapField.setAccessible(true);
        ((java.util.Map<Long, MealInputState>)stateMapField.get(command)).put(chatId, state);

        try (MockedStatic<KeyboardFactory> keyboardFactoryMock = mockStatic(KeyboardFactory.class)) {
            keyboardFactoryMock.when(KeyboardFactory::getMealInputKeyboard).thenReturn(mockKeyboard);

            BotResponse response = command.execute(update);

            assertNotNull(response);
            assertEquals(chatId, response.getChatId());
            assertSame(mockKeyboard, response.getReplyMarkup());
            assertTrue(response.getText().contains("Введите число")); // Ошибка ожидается
        }
    }
}
