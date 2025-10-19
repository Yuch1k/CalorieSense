package ru.CalorieSense.command.nutrition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.model.DailyProgress;
import ru.CalorieSense.model.MacronutrientResult;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.service.nutrition.CalorieCalculatorService;
import ru.CalorieSense.service.nutrition.MealTrackingService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CaloriesCommandTest {

    @Mock
    private BotApp botApp;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CalorieCalculatorService calculator;

    @Mock
    private MealTrackingService mealTracker;

    @Mock
    private Update update;

    @Mock
    private Message message;

    private CaloriesCommand caloriesCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        caloriesCommand = new CaloriesCommand(botApp, userRepository, calculator, mealTracker);

        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
    }

    @Test
    void testExecute_IncompleteProfile_ShowsWarning() throws Exception {
        UserProfile profile = new UserProfile(123L);
        MacronutrientResult target = new MacronutrientResult(2200, 165, 61, 248);
        DailyProgress progress = new DailyProgress(0, 0, 0, 0, 2200, 165, 61, 248);

        when(userRepository.findByChatId(123L)).thenReturn(profile);
        when(calculator.calculateWithMacros(profile)).thenReturn(target);
        when(mealTracker.calculateDailyProgress(123L, target)).thenReturn(progress);

        caloriesCommand.execute(update);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(botApp).execute(messageCaptor.capture());

        SendMessage sentMessage = messageCaptor.getValue();
        assertTrue(sentMessage.getText().contains("пройдите регистрацию"));
    }

    @Test
    void testExecute_CompleteProfile_ShowsProgress() throws Exception {
        UserProfile profile = new UserProfile(123L, 75, 180, "М", 30);
        profile.setGoal("maintain");
        profile.setActivityLevel("medium");

        MacronutrientResult target = new MacronutrientResult(2200, 165, 61, 248);
        DailyProgress progress = new DailyProgress(1500, 100, 40, 150, 2200, 165, 61, 248);

        when(userRepository.findByChatId(123L)).thenReturn(profile);
        when(calculator.calculateWithMacros(profile)).thenReturn(target);
        when(mealTracker.calculateDailyProgress(123L, target)).thenReturn(progress);
        when(calculator.getActivityLevelText("medium")).thenReturn("Средняя (1-2 тренировки в неделю)");

        caloriesCommand.execute(update);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(botApp).execute(messageCaptor.capture());

        SendMessage sentMessage = messageCaptor.getValue();
        assertTrue(sentMessage.getText().contains("1500"));
        assertTrue(sentMessage.getText().contains("2200"));
        assertTrue(sentMessage.getText().contains("Осталось"));
    }

    @Test
    void testExecute_NoMealsYet_ShowsZeroProgress() throws Exception {
        UserProfile profile = new UserProfile(123L, 75, 180, "М", 30);
        profile.setGoal("maintain");
        profile.setActivityLevel("low");

        MacronutrientResult target = new MacronutrientResult(2000, 150, 55, 225);
        DailyProgress progress = new DailyProgress(0, 0, 0, 0, 2000, 150, 55, 225);

        when(userRepository.findByChatId(123L)).thenReturn(profile);
        when(calculator.calculateWithMacros(profile)).thenReturn(target);
        when(mealTracker.calculateDailyProgress(123L, target)).thenReturn(progress);
        when(calculator.getActivityLevelText("low")).thenReturn("Малая (без тренировок)");

        caloriesCommand.execute(update);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(botApp).execute(messageCaptor.capture());

        SendMessage sentMessage = messageCaptor.getValue();
        assertTrue(sentMessage.getText().contains("0 / 2000"));
        verify(calculator).calculateWithMacros(profile);
        verify(mealTracker).calculateDailyProgress(123L, target);
    }

    @Test
    void testExecute_OverTarget_ShowsRedStatus() throws Exception {
        UserProfile profile = new UserProfile(123L, 75, 180, "М", 30);
        profile.setGoal("lose");
        profile.setActivityLevel("high");

        MacronutrientResult target = new MacronutrientResult(2000, 150, 55, 225);
        DailyProgress progress = new DailyProgress(2500, 180, 70, 300, 2000, 150, 55, 225);

        when(userRepository.findByChatId(123L)).thenReturn(profile);
        when(calculator.calculateWithMacros(profile)).thenReturn(target);
        when(mealTracker.calculateDailyProgress(123L, target)).thenReturn(progress);
        when(calculator.getActivityLevelText("high")).thenReturn("Большая (более 2 тренировок в неделю)");

        caloriesCommand.execute(update);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(botApp).execute(messageCaptor.capture());

        SendMessage sentMessage = messageCaptor.getValue();
        assertTrue(sentMessage.getText().contains("2500"));
        assertTrue(sentMessage.getText().contains("125%") || sentMessage.getText().contains("🔴"));
    }
}
