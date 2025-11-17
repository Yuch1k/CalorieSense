package ru.CalorieSense.command;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.messaging.BotResponse;

public interface Command {
    BotResponse execute(Update update) throws Exception;
}
