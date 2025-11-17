package ru.CalorieSense.messaging;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public class BotResponse {

    private final Long chatId;
    private final String text;
    private final String parseMode;
    private final ReplyKeyboard replyMarkup;

    public BotResponse(Long chatId, String text, String parseMode, ReplyKeyboard replyMarkup) {
        this.chatId = chatId;
        this.text = text;
        this.parseMode = parseMode;
        this.replyMarkup = replyMarkup;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getText() {
        return text;
    }

    public String getParseMode() {
        return parseMode;
    }

    public ReplyKeyboard getReplyMarkup() {
        return replyMarkup;
    }

    public static BotResponse simple(Long chatId, String text) {
        return new BotResponse(chatId, text, null, null);
    }
}
