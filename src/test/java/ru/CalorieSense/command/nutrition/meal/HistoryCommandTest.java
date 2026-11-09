package ru.CalorieSense.command.nutrition.meal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.model.Meal;
import ru.CalorieSense.service.nutrition.MealTrackingService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoryCommandTest {

    @Mock
    private BotApp botApp;
    @Mock
    private MealTrackingService mealTracker;
    @Mock
    private Update update;
    @Mock
    private Message message;

    private HistoryCommand command;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        command = new HistoryCommand(botApp, mealTracker);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(42L);
    }

    private String sentText() throws Exception {
        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(botApp).execute(captor.capture());
        return captor.getValue().getText();
    }

    @Test
    void emptyDayShowsHint() throws Exception {
        when(mealTracker.getTodayMeals(42L)).thenReturn(Collections.emptyList());
        command.execute(update);
        assertTrue(sentText().contains("ещё не было"));
    }

    @Test
    void mealsAreListedInTimeOrderWithTotal() throws Exception {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0);
        Meal lunch = new Meal(2L, 42L, today.withHour(13).withMinute(5), 600, 30, 20, 70, "Обед");
        Meal breakfast = new Meal(1L, 42L, today.withHour(8).withMinute(30), 400, 20, 10, 50, "Каша");
        when(mealTracker.getTodayMeals(42L)).thenReturn(List.of(lunch, breakfast));

        command.execute(update);
        String text = sentText();

        assertTrue(text.indexOf("08:30") < text.indexOf("13:05"), "завтрак должен идти раньше обеда");
        assertTrue(text.contains("Итого:* 1000 ккал (Б 50 / Ж 30 / У 120)"));
    }

    @Test
    void markdownInDescriptionIsEscaped() {
        Meal meal = new Meal(1L, 42L, LocalDateTime.now(), 100, 1, 1, 1, "суп_дня *острый*");
        String text = HistoryCommand.buildText(List.of(meal));
        assertTrue(text.contains("суп\\_дня \\*острый\\*"));
    }
}
