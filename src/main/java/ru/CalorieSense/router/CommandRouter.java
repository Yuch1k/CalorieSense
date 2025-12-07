package ru.CalorieSense.router;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.command.nutrition.meal.MealEditCommand;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.messaging.MessageSender;
import ru.CalorieSense.model.DailyProgress;
import ru.CalorieSense.model.MacronutrientResult;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.service.calendar.CalendarService;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import java.time.LocalDate;
import ru.CalorieSense.service.calendar.CalendarService;
import ru.CalorieSense.service.nutrition.CalorieCalculatorService;
import ru.CalorieSense.service.nutrition.MealTrackingService;
import ru.CalorieSense.ui.CalendarKeyboardFactory;
import ru.CalorieSense.model.Meal;
import ru.CalorieSense.repository.MealRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CommandRouter {
    private final Map<String, Command> commands = new HashMap<>();
    private final Command fallback;
    private final Map<Long, Command> activeScenarios = new HashMap<>();
    private final Map<String, String> buttonToCommand = new HashMap<>();
    private final MessageSender sender;
    private final CalendarService calendarService;
    private final MealTrackingService mealTrackingService;
    private final CalorieCalculatorService calorieCalculatorService;
    private final UserRepository userRepository;
    private final MealRepository mealRepository;
    private final MealEditCommand mealEditCommand;

    public CommandRouter(Command fallback,
                         MessageSender sender,
                         CalendarService calendarService,
                         MealTrackingService mealTrackingService,
                         CalorieCalculatorService calorieCalculatorService,
                         UserRepository userRepository,
                         MealRepository mealRepository,
                         MealEditCommand mealEditCommand) {
        this.fallback = fallback;
        this.sender = sender;
        this.calendarService = calendarService;
        this.mealTrackingService = mealTrackingService;
        this.calorieCalculatorService = calorieCalculatorService;
        this.userRepository = userRepository;
        this.mealRepository = mealRepository;
        this.mealEditCommand = mealEditCommand;
        initButtonMapping();
    }

    private void initButtonMapping() {
        // Главное меню
        buttonToCommand.put("📊 Мои калории", "/calories");
        buttonToCommand.put("📝 Регистрация", "/register");
        buttonToCommand.put("👤 Профиль", "/profile");
        buttonToCommand.put("🍽️ Добавить приём пищи", "/addmeal");
        buttonToCommand.put("📅 Календарь питания", "/calendar");
        buttonToCommand.put("❓ Помощь", "/help");

        // Меню редактирования параметров
        buttonToCommand.put("⚖️ Вес", "/weight");
        buttonToCommand.put("📏 Рост", "/height");
        buttonToCommand.put("🎂 Возраст", "/age");
        buttonToCommand.put("👤 Пол", "/sex");
        buttonToCommand.put("🎯 Цель", "/goal");
        buttonToCommand.put("💪 Активность", "/activity");

        // Меню добавления приёма пищи
        buttonToCommand.put("✍️ Ввести вручную", "/manualinput");
        buttonToCommand.put("📸 Ввести по фото", "/photoinput");
        buttonToCommand.put("◀️ Назад в главное меню", "/backtomain");
    }

    public void register(String name, Command handler) {
        if (name == null || handler == null) return;
        commands.put(name.toLowerCase(Locale.ROOT), handler);
    }

    public void handle(Update update) throws Exception {
        if (update == null) return;

        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update);
        } else if (update.hasCallbackQuery()) {
            handleCallback(update);
        }
    }

    private void handleMessage(Update update) throws Exception {
        Message message = update.getMessage();
        String text = message.getText().trim();
        Long chatId = message.getChatId();

        // Преобразуем текст кнопки в команду, если нужно
        if (buttonToCommand.containsKey(text)) {
            text = buttonToCommand.get(text);
        }

        String cmd = text.split("\\s+")[0].toLowerCase(Locale.ROOT);
        BotResponse response;

        if (cmd.startsWith("/")) {
            Command handler = commands.getOrDefault(cmd, fallback);
            boolean isScenario = handler.getClass().getSimpleName().contains("Scenario")
                    || handler.getClass().getSimpleName().equals("ManualInputCommand");

            if (!isScenario) {
                activeScenarios.remove(chatId);
            }

            response = handler.execute(update);

            if (isScenario) {
                activeScenarios.put(chatId, handler);
            }
        } else if (activeScenarios.containsKey(chatId)) {
            Command scenario = activeScenarios.get(chatId);
            response = scenario.execute(update);
        } else {
            response = fallback.execute(update);
        }

        if (sender != null && response != null) {
            sender.sendBotResponse(response);
        }
    }

    private void redrawCalendar(Long chatId) throws Exception {
        LocalDate month = calendarService.getCurrentMonth(chatId);
        var keyboard = CalendarKeyboardFactory.buildMonthKeyboard(month);

        BotResponse response = new BotResponse(
                chatId,
                "📅 Выберите день для просмотра питания:",
                null,
                keyboard
        );

        if (sender != null) {
            sender.sendBotResponse(response);
        }
    }


    private void handleCallback(Update update) throws Exception {
        CallbackQuery callback = update.getCallbackQuery();
        if (callback == null) {
            return;
        }

        String data = callback.getData();
        Long chatId = callback.getMessage().getChatId();

        if (data == null || data.startsWith("CAL_IGNORE")) {
            return;
        }

        if (data.equals("CAL_MONTH_PREV")) {
            calendarService.movePrevMonth(chatId);
            redrawCalendar(chatId);
            return;
        }

        if (data.equals("CAL_MONTH_NEXT")) {
            calendarService.moveNextMonth(chatId);
            redrawCalendar(chatId);
            return;
        }

        if (data.startsWith("CAL_DAY:")) {
            String dateStr = data.substring("CAL_DAY:".length());
            LocalDate date = LocalDate.parse(dateStr);

            UserProfile profile = userRepository.findByChatId(chatId);
            MacronutrientResult target = calorieCalculatorService.calculateWithMacros(profile);
            DailyProgress progress = mealTrackingService.calculateDailyProgress(chatId, date, target);

            String status = progress.getConsumedCalories() <= progress.getTargetCalories()
                    ? "✅ В пределах нормы"
                    : "❌ Превышение нормы";

            StringBuilder sb = new StringBuilder();
            sb.append("📅 Дата: ").append(date)
                    .append("\nКалории: ").append(progress.getConsumedCalories())
                    .append(" / ").append(progress.getTargetCalories())
                    .append("\nБелки: ").append(progress.getConsumedProtein())
                    .append(" / ").append(progress.getTargetProtein())
                    .append("\nЖиры: ").append(progress.getConsumedFats())
                    .append(" / ").append(progress.getTargetFats())
                    .append("\nУглеводы: ").append(progress.getConsumedCarbs())
                    .append(" / ").append(progress.getTargetCarbs())
                    .append("\n\n").append(status)
                    .append("\n\nВыберите приём пищи для редактирования:");

            List<Meal> meals = mealRepository.getMealsByDate(chatId, date);
            var keyboard = CalendarKeyboardFactory.buildMealsKeyboard(date, meals);

            BotResponse response = new BotResponse(
                    chatId,
                    sb.toString(),
                    null,
                    keyboard
            );
            if (sender != null) {
                sender.sendBotResponse(response);
            }
            return;
        }

        if (data.startsWith("CAL_BACK_TO_MONTH:")) {
            String dateStr = data.substring("CAL_BACK_TO_MONTH:".length());
            LocalDate month = LocalDate.parse(dateStr);
            calendarService.setMonth(chatId, month);
            redrawCalendar(chatId);
            return;
        }

        if (data.startsWith("MEAL_EDIT:")) {
            Long mealId = Long.parseLong(data.substring("MEAL_EDIT:".length()));
            BotResponse response = mealEditCommand.startEdit(chatId, mealId);
            if (sender != null && response != null) {
                sender.sendBotResponse(response);
            }

            // помечаем сценарий как активный
            activeScenarios.put(chatId, mealEditCommand);
            return;
        }




    }

}
