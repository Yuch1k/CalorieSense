package ru.CalorieSense.command.system;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.ui.KeyboardFactory;

public class HelpCommand implements Command {
    private static final String HELP_MESSAGE =
            "📋 *Доступные команды:*\n\n" +
                    "📊 *Мои калории* — показать дневную норму калорий и БЖУ\n" +
                    "📝 *Регистрация* — пройти регистрацию (ввод всех параметров)\n" +
                    "🍽️ *Добавить приём пищи* — записать съеденное (в разработке)\n" +
                    "👤 *Профиль* — просмотр и изменение параметров профиля\n" +
                    "❓ *Помощь* — показать эту справку\n\n" +
                    "━━━━━━━━━━━━━━━━\n\n" +
                    "*В профиле можно изменить:*\n" +
                    "• Вес\n" +
                    "• Рост\n" +
                    "• Возраст\n" +
                    "• Пол\n" +
                    "• Цель (похудение/поддержание/набор)\n" +
                    "• Уровень активности\n\n" +
                    "Вы можете использовать кнопки меню или команды вида `/calories`";

    @Override
    public BotResponse execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        String message = HELP_MESSAGE;
        ReplyKeyboard keyboard = KeyboardFactory.getMainKeyboard();
        return new BotResponse(chatId, message, "Markdown", keyboard);
    }
}
