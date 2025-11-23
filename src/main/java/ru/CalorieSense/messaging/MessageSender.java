package ru.CalorieSense.messaging;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.CalorieSense.BotApp;

public class MessageSender {
    private final BotApp bot;

    public MessageSender(BotApp bot) {
        this.bot = bot;
    }

    public void sendBotResponse(BotResponse response) {
        if (response == null) return;
        SendMessage msg = new SendMessage();
        msg.setChatId(String.valueOf(response.getChatId()));
        msg.setText(response.getText());
        if (response.getParseMode() != null) msg.setParseMode(response.getParseMode());
        if (response.getReplyMarkup() != null) msg.setReplyMarkup(response.getReplyMarkup());
        try {
            bot.execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
