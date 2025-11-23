import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.CalorieSense.command.nutrition.CaloriesCommand;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.DailyProgress;
import ru.CalorieSense.model.MacronutrientResult;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.service.nutrition.CalorieCalculatorService;
import ru.CalorieSense.service.nutrition.MealTrackingService;
import ru.CalorieSense.ui.KeyboardFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CaloriesCommandTest {

    @Test
    void execute_IncompleteProfile_ReturnsWarningAndKeyboard() throws Exception {
        Long chatId = 555L;

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);

        UserRepository userRepository = mock(UserRepository.class);
        CalorieCalculatorService calculator = mock(CalorieCalculatorService.class);
        MealTrackingService mealTracker = mock(MealTrackingService.class);
        ReplyKeyboardMarkup mockKeyboard = mock(ReplyKeyboardMarkup.class);

        UserProfile mockProfile = mock(UserProfile.class);
        when(userRepository.findByChatId(chatId)).thenReturn(mockProfile);
        when(mockProfile.isComplete()).thenReturn(false);

        MacronutrientResult macroRes = mock(MacronutrientResult.class);
        when(calculator.calculateWithMacros(mockProfile)).thenReturn(macroRes);

        when(mealTracker.calculateDailyProgress(chatId, macroRes)).thenReturn(mock(DailyProgress.class));

        try (MockedStatic<KeyboardFactory> keyboardFactoryMock = mockStatic(KeyboardFactory.class)) {
            keyboardFactoryMock.when(KeyboardFactory::getMainKeyboard).thenReturn(mockKeyboard);

            CaloriesCommand command = new CaloriesCommand(null, userRepository, calculator, mealTracker);

            BotResponse response = command.execute(update);

            assertNotNull(response);
            assertEquals(chatId, response.getChatId());
            assertEquals("Markdown", response.getParseMode());
            assertSame(mockKeyboard, response.getReplyMarkup());
            assertTrue(response.getText().contains("Для точного расчёта пройдите регистрацию"));
            assertTrue(response.getText().contains("Текущая оценка"));
        }
    }

    @Test
    void execute_CompleteProfile_ReturnsProgressMessageAndKeyboard() throws Exception {
        Long chatId = 777L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);

        UserRepository userRepository = mock(UserRepository.class);
        CalorieCalculatorService calculator = mock(CalorieCalculatorService.class);
        MealTrackingService mealTracker = mock(MealTrackingService.class);
        ReplyKeyboardMarkup mockKeyboard = mock(ReplyKeyboardMarkup.class);

        UserProfile mockProfile = mock(UserProfile.class);
        when(userRepository.findByChatId(chatId)).thenReturn(mockProfile);
        when(mockProfile.isComplete()).thenReturn(true);
        when(mockProfile.getGoal()).thenReturn("lose");
        when(mockProfile.getActivityLevel()).thenReturn("medium");

        MacronutrientResult macroRes = mock(MacronutrientResult.class);
        when(calculator.calculateWithMacros(mockProfile)).thenReturn(macroRes);

        DailyProgress progress = mock(DailyProgress.class);
        when(mealTracker.calculateDailyProgress(chatId, macroRes)).thenReturn(progress);

        when(progress.getCaloriesPercentage()).thenReturn(80.0);
        when(progress.getConsumedCalories()).thenReturn(1600);
        when(progress.getTargetCalories()).thenReturn(2000);
        when(progress.getConsumedProtein()).thenReturn(60);
        when(progress.getTargetProtein()).thenReturn(100);
        when(progress.getConsumedFats()).thenReturn(40);
        when(progress.getTargetFats()).thenReturn(70);
        when(progress.getConsumedCarbs()).thenReturn(180);
        when(progress.getTargetCarbs()).thenReturn(250);
        when(progress.getRemainingCalories()).thenReturn(400);
        when(progress.getRemainingProtein()).thenReturn(40);
        when(progress.getRemainingFats()).thenReturn(30);
        when(progress.getRemainingCarbs()).thenReturn(70);

        when(calculator.getActivityLevelText("medium")).thenReturn("Средний");

        try (MockedStatic<KeyboardFactory> keyboardFactoryMock = mockStatic(KeyboardFactory.class)) {
            keyboardFactoryMock.when(KeyboardFactory::getMainKeyboard).thenReturn(mockKeyboard);

            CaloriesCommand command = new CaloriesCommand(null, userRepository, calculator, mealTracker);

            BotResponse response = command.execute(update);

            assertNotNull(response);
            assertEquals(chatId, response.getChatId());
            assertEquals("Markdown", response.getParseMode());
            assertSame(mockKeyboard, response.getReplyMarkup());
            assertTrue(response.getText().contains("Ваш прогресс за сегодня"));
            assertTrue(response.getText().contains("Калории:"));
            assertTrue(response.getText().contains("БЖУ:"));
            assertTrue(response.getText().contains("Цель: Похудение"));
            assertTrue(response.getText().contains("Активность: Средний"));
        }
    }
}
