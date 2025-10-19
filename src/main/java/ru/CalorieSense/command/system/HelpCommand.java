package ru.CalorieSense.command.system;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.ui.KeyboardFactory;

public class HelpCommand implements Command {

    private final BotApp sender;

    public HelpCommand(BotApp sender) {
        this.sender = sender;
    }

    private static final String HELP_MESSAGE =
            "📋 *Доступные команды:*\n\n" +
                    "📊 *Мои калории* — показать дневную норму калорий и БЖУ\n" +
                    "📝 *Регистрация* — пройти регистрацию (ввод всех параметров)\n" +
                    "👤 *Профиль* — просмотр и изменение параметров профиля\n" +  // ИЗМЕНЕНО
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
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(HELP_MESSAGE)
                .parseMode("Markdown")
                .replyMarkup(KeyboardFactory.getMainKeyboard())
                .build();

        sender.execute(message);
    }
}
