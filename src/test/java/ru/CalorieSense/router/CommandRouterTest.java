package ru.CalorieSense.router;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.*;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.command.nutrition.meal.MealEditCommand;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.messaging.MessageSender;
import ru.CalorieSense.model.DailyProgress;
import ru.CalorieSense.model.MacronutrientResult;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.MealRepository;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.service.calendar.CalendarService;
import ru.CalorieSense.service.nutrition.CalorieCalculatorService;
import ru.CalorieSense.service.nutrition.MealTrackingService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandRouterTest {

    private Update buildTextUpdate(long chatId, String text) {
        Message msg = new Message();
        msg.setChat(new Chat(chatId, "private"));
        msg.setText(text);
        Update u = new Update();
        u.setMessage(msg);
        return u;
    }

    @Test
    void handleMessage_routesSlashCommandToRegisteredHandler() throws Exception {
        Command fallback = mock(Command.class);
        MessageSender sender = mock(MessageSender.class);
        CalendarService calServ = mock(CalendarService.class);
        MealTrackingService mtServ = mock(MealTrackingService.class);
        CalorieCalculatorService calc = mock(CalorieCalculatorService.class);
        UserRepository userRepo = mock(UserRepository.class);
        MealRepository mealRepo = mock(MealRepository.class);
        MealEditCommand mealEdit = mock(MealEditCommand.class);

        CommandRouter router = new CommandRouter(
                fallback, sender, calServ, mtServ, calc, userRepo, mealRepo, mealEdit
        );

        Command start = mock(Command.class);
        BotResponse startResp = BotResponse.simple(1L, "ok");
        when(start.execute(any())).thenReturn(startResp);

        router.register("/start", start);

        router.handle(buildTextUpdate(1L, "/start"));

        verify(start).execute(any());
        verify(sender).sendBotResponse(startResp);
        verifyNoInteractions(fallback);
    }

    @Test
    void handleMessage_forNonCommandUsesActiveScenario() throws Exception {
        Command fallback = mock(Command.class);
        MessageSender sender = mock(MessageSender.class);
        CalendarService calServ = mock(CalendarService.class);
        MealTrackingService mtServ = mock(MealTrackingService.class);
        CalorieCalculatorService calc = mock(CalorieCalculatorService.class);
        UserRepository userRepo = mock(UserRepository.class);
        MealRepository mealRepo = mock(MealRepository.class);
        MealEditCommand mealEdit = mock(MealEditCommand.class);

        CommandRouter router = new CommandRouter(
                fallback, sender, calServ, mtServ, calc, userRepo, mealRepo, mealEdit
        );

        // сценарий-команда
        Command scenario = mock(Command.class);
        BotResponse first = BotResponse.simple(1L, "step1");
        BotResponse second = BotResponse.simple(1L, "step2");
        when(scenario.execute(any()))
                .thenReturn(first)   // вызов по /manualinput
                .thenReturn(second); // вызов по обычному тексту

        router.register("/manualinput", scenario);

        // старт сценария
        router.handle(buildTextUpdate(1L, "/manualinput"));
        // продолжение сценария обычным текстом
        router.handle(buildTextUpdate(1L, "some data"));

        verify(scenario, times(2)).execute(any());
        verify(sender).sendBotResponse(first);
        verify(sender).sendBotResponse(second);
        verifyNoInteractions(fallback);
    }

    @Test
    void handleCallback_calDayBuildsSummaryAndSendsMessage() throws Exception {
        Command fallback = mock(Command.class);
        MessageSender sender = mock(MessageSender.class);
        CalendarService calServ = mock(CalendarService.class);
        MealTrackingService mtServ = mock(MealTrackingService.class);
        CalorieCalculatorService calc = mock(CalorieCalculatorService.class);
        UserRepository userRepo = mock(UserRepository.class);
        MealRepository mealRepo = mock(MealRepository.class);
        MealEditCommand mealEdit = mock(MealEditCommand.class);

        CommandRouter router = new CommandRouter(
                fallback, sender, calServ, mtServ, calc, userRepo, mealRepo, mealEdit
        );

        long chatId = 5L;
        LocalDate date = LocalDate.of(2025, 1, 2);

        UserProfile profile = new UserProfile(chatId);
        when(userRepo.findByChatId(chatId)).thenReturn(profile);

        MacronutrientResult target = new MacronutrientResult(1800, 120, 60, 200);
        when(calc.calculateWithMacros(profile)).thenReturn(target);

        DailyProgress progress = new DailyProgress(
                900, 60, 30, 100,
                1800, 120, 60, 200
        );
        when(mtServ.calculateDailyProgress(chatId, date, target)).thenReturn(progress);

        when(mealRepo.getMealsByDate(chatId, date))
                .thenReturn(Collections.emptyList());

        // callback CAL_DAY
        CallbackQuery cb = new CallbackQuery();
        cb.setData("CAL_DAY:" + date);
        Message msg = new Message();
        msg.setChat(new Chat(chatId, "private"));
        cb.setMessage(msg);
        Update update = new Update();
        update.setCallbackQuery(cb);

        router.handle(update);

        verify(sender).sendBotResponse(argThat(resp ->
                resp.getChatId().equals(chatId)
                        && resp.getText().contains("Калории")
                        && resp.getText().contains("900 / 1800")
        ));
    }
}
