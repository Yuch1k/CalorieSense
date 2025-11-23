package ru.CalorieSense.messaging;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.messaging.MessageSender;
import ru.CalorieSense.BotApp;
public class MessageSenderTest {
    @Test
    void testSendSimpleTextMessage() throws Exception {
        BotApp bot = Mockito.mock(BotApp.class);
        MessageSender sender = new MessageSender(bot);

        BotResponse response = BotResponse.simple(123L, "Тестовое сообщение");
        sender.sendBotResponse(response);

        Mockito.verify(bot).execute(Mockito.argThat((SendMessage msg) ->
                msg.getChatId().equals("123") &&
                        msg.getText().equals("Тестовое сообщение")));
    }

    @Test
    void testSendMessageWithReplyMarkup() throws Exception {
        BotApp bot = Mockito.mock(BotApp.class);
        MessageSender sender = new MessageSender(bot);

        ReplyKeyboard replyKeyboard = Mockito.mock(ReplyKeyboard.class);
        BotResponse response = new BotResponse(777L, "С клавиатурой", null, replyKeyboard);
        sender.sendBotResponse(response);

        Mockito.verify(bot).execute(Mockito.argThat((SendMessage msg) ->
                msg.getChatId().equals("777") &&
                        msg.getReplyMarkup() == replyKeyboard &&
                        msg.getText().equals("С клавиатурой")));
    }
}
